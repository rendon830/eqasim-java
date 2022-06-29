package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.utilities;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMBikePredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMBikeVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;
/**
 * Class defines the utility of a trip done by bike, based on Krauss et al 2021
 */

public class SMMBikeEstimator implements UtilityEstimator {
    private final SMMParameters parameters;
    private final SMMBikePredictor predictor;

    @Inject
    public SMMBikeEstimator(SMMParameters parameters, SMMBikePredictor predictor, SMMPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }

    protected double estimateConstantUtility() {
        return parameters.bikeShare.alpha_u;
    }
    protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip,List<? extends PlanElement> elements){
        SMMPersonPredictor personPredictor=new SMMPersonPredictor();
        SMMEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
        double ageU=personVariables.age_a*parameters.bike.betaAge;
        double bikeAcc=personVariables.getBikeAcc()*parameters.bike.betaBikeAcc;
        double carAcc=personVariables.getCarAccessibility()*parameters.bike.betaCarAcc;
        double pTAcc=personVariables.getPtPass()*parameters.bike.betaPTPass;
        return(ageU+bikeAcc+carAcc+pTAcc);
    }
    protected double estimateTravelTimeUtility(SMMBikeVariables variables) {
        return parameters.bike.betaTravelTime_u_min * variables.travelTime_u_min;
    }

    protected double estimateAccessTimeUtility(SMMBikeVariables variables) {
        return parameters.bike.betaAccess_Time * variables.access_Time;
    }

    protected double estimateMonetaryCostUtility(SMMBikeVariables variables) {
        return -Math.exp(parameters.betaCost_u_MU) * variables.cost;
    }

    protected double estimateEgressTimeUtility(SMMBikeVariables variables) {
        return parameters.bike.betaEgress_Time* variables.egress_Time;
    }

    protected double estimateParkingTimeUtility(SMMBikeVariables variables){
        return parameters.bike.betaParkingTime_u_min*variables.parkingTime_u_min;
    }


    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        SMMBikeVariables variables = predictor.predict(person, trip, elements);

        double utility = 0.0;

        utility += estimateConstantUtility();
        utility += estimateTravelTimeUtility(variables);
        utility += estimateAccessTimeUtility(variables);
        utility += estimateMonetaryCostUtility(variables);
        utility += estimateEgressTimeUtility(variables);
        utility+= estimateParkingTimeUtility(variables);
        utility+=estimatePersonalUtility(person,trip,elements);
        return utility;
    }


}

