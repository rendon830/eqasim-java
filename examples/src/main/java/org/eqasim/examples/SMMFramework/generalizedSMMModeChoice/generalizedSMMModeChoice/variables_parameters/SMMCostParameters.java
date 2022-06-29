package org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.mode_choice.ParameterDefinition;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.estimators.SMMBikeShareEstimator;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.predictors.SMMBikeSharePredictor;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.corsica_drt.Drafts.DGeneralizedMultimodal.sharingPt.SharingPTModeChoiceModule;
import org.matsim.core.config.CommandLine;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.geotools.feature.type.DateUtil.isEqual;

/**
 * Class  is responsible of storing cost structures and add the SMM cosz structures
 */
public class SMMCostParameters implements ParameterDefinition {
    public double carCost_Km;
    public double bookingCostBikeShare;
    public double bikeShareCost_Km;
    public double bookingCostEScooter;
    public double eScooterCost_km;
    public double pTTicketCost;
    public HashMap<String, Double> sharingBookingCosts;
    public HashMap<String, Double> sharingMinCosts;


//Method process the command Line arguments and converts them into cost structures
    public static void applyCommandLineMicromobility(String prefix, CommandLine cmd, ParameterDefinition parameterDefinition) throws Exception {
        Map<String, String> values = new HashMap<>();

        for (String option : cmd.getAvailableOptions()) {
            if (option.startsWith(prefix + ":")) {
                try {
                    values.put(option.split(":")[1], cmd.getOptionStrict(option));
                } catch (CommandLine.ConfigurationException e) {
                    // Should not happen
                }
            }
        }

        // ParameterDefinition.applyMap(parameterDefinition, values);
        // Apply the cost of SMM values
        applyMapMicromobilityCost(parameterDefinition, values);
        // Validates all values are present
        validateSharingCostParameters((SMMCostParameters) parameterDefinition);

    }
    // Method genertes the cost parameters for SMM modes based on command Line arguments
    private static void applyMapMicromobilityCost(ParameterDefinition parameterDefinition, Map<String, String> values) throws Exception {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            String option = entry.getKey();
            String value = entry.getValue();

            try {
                String[] parts = option.split("\\.");
                int numberOfParts = parts.length;
                if (parts[0].equals("sharingBookingCosts") || parts[0].equals("sharingMinCosts")) {
                    Object activeObject = parameterDefinition;

                    // fills the SMM cost structure map with values
                    Field field = activeObject.getClass().getField(parts[0]);
                    if (field.getType() == HashMap.class || field.getType() == Map.class) {
                        HashMap<String, Double> reeplacement = (HashMap<String, Double>) field.get(activeObject);
                        reeplacement.put("sharing:" + parts[1], Double.parseDouble(value));
                    }


                    logger.info(String.format("Set %s = %s", option, value));
                } else {
                    // Fills the traditional mode svalues
                    Object activeObject = parameterDefinition;

                    for (int i = 0; i < parts.length; i++) {
                        Field field = activeObject.getClass().getField(parts[i]);
                        if (i == numberOfParts - 1) {
                            // We need to set the value
                            if (field.getType() == Double.class || field.getType() == double.class) {
                                field.setDouble(activeObject, Double.parseDouble(value));
                            } else if (field.getType() == Float.class || field.getType() == float.class) {
                                field.setFloat(activeObject, Float.parseFloat(value));
                            } else if (field.getType() == Integer.class || field.getType() == int.class) {
                                field.setInt(activeObject, Integer.parseInt(value));
                            } else if (field.getType() == Long.class || field.getType() == long.class) {
                                field.setLong(activeObject, Long.parseLong(value));
                            } else if (field.getType() == String.class) {
                                field.set(activeObject, value);
                            } else if (field.getType().isEnum()) {
                                Class<Enum> enumType = (Class<Enum>) field.getType();
                                field.set(activeObject, Enum.valueOf(enumType, value));
                            } else {
                                throw new IllegalStateException(
                                        String.format("Cannot convert parameter %s because type %s is not supported",
                                                option, field.getType().toString()));
                            }
                        } else {
                            // We need to traverse the objects
                            activeObject = field.get(activeObject);
                        }
                    }

                }
            } catch (NoSuchFieldException e) {
                throw new IllegalStateException(String.format("Parameter %s does not exist", option));
            } catch (SecurityException | IllegalArgumentException e) {
                logger.error("Error while processing option " + option);
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }


    }
    // Default values builder

    public static SMMCostParameters buildDefault() {
        SMMCostParameters parameters = new SMMCostParameters();
        parameters.carCost_Km= 0.5;
        parameters.pTTicketCost=1.8;
        parameters.sharingBookingCosts = new HashMap<String, Double>();
        parameters.sharingBookingCosts.put("Standard BikeShare", 0.25);
        parameters.sharingMinCosts = new HashMap<String, Double>();
        parameters.sharingMinCosts.put("Standard BikeShare", 1.0);
        parameters.sharingBookingCosts.put("Standard eScooter", 0.5);
        parameters.sharingMinCosts.put("Standard eScooter", 0.5);

        return parameters;
    }
    // Validates the SMM modes parameters, need cost per min and booking cost for each mode
    public static void validateSharingCostParameters(SMMCostParameters parameterDefinition) throws Exception {
        Set<String> sharingKMCosts = parameterDefinition.sharingMinCosts.keySet();
        Set<String> sharingBookingCosts = parameterDefinition.sharingBookingCosts.keySet();
        boolean isEqual = isEqual(sharingBookingCosts, sharingKMCosts);
        if (isEqual == false) {
            throw new IllegalArgumentException(" One of the sharing modes does not have booking or km cost");
        }
    }


    @Provides
    @Singleton
    public static SMMCostParameters provideCostParameters(EqasimConfigGroup config, CommandLine commandLine) throws Exception {
        SMMCostParameters parameters = SMMCostParameters.buildDefault();

        if (config.getCostParametersPath() != null) {
            ParameterDefinition.applyFile(new File(config.getCostParametersPath()), parameters);
        }

        SMMCostParameters.applyCommandLineMicromobility("cost-parameter", commandLine, parameters);
        return parameters;
    }


}


