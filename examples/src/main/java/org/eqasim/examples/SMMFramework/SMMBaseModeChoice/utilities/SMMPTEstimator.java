package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.utilities;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPTPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMPTVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;
/**
 * Class defines the utility of a trip done by PT, based on Krauss et al 2021
 */
public class SMMPTEstimator implements UtilityEstimator {
    private final SMMParameters parameters;
    private final SMMPTPredictor predictor;


    @Inject
    public SMMPTEstimator(SMMParameters parameters, SMMPTPredictor predictor, SMMPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }



    protected double estimateConstantUtility() {
        return parameters.pt.alpha_u;
    }
    protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements){
        SMMPersonPredictor personPredictor=new SMMPersonPredictor();
        SMMEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
        double ageU=personVariables.age_a*parameters.pt.betaAge;
        double bikeAcc=personVariables.getBikeAcc()*parameters.pt.betaBikeAcc;
        double pTAcc=personVariables.getPtPass()*parameters.pt.betaPTPass;
        double carAcc=personVariables.getCarAccessibility()+parameters.pt.betaCarAcc;
        return(ageU+bikeAcc+pTAcc+carAcc);
    }
    protected double estimateTravelTimeUtility(SMMPTVariables variables) {
        return parameters.pt.betaTravelTime_u_min * variables.travelTime_u_min;
    }

    protected double estimateAccessTimeUtility(SMMPTVariables variables) {
        return parameters.pt.betaAccess_Time * variables.access_Time;
    }

    protected double estimateMonetaryCostUtility(SMMPTVariables variables) {
        return -Math.exp(parameters.betaCost_u_MU) * variables.cost;
    }

    protected double estimateEgressTimeUtility(SMMPTVariables variables) {
        return parameters.pt.betaEgress_Time* variables.egress_Time;
    }
    protected double estimateChangeLineUtility(SMMPTVariables variables){
        return parameters.pt.betaTransfers*variables.transfers;
    }


    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        SMMPTVariables variables = predictor.predict(person, trip, elements);

        double utility = 0.0;

        utility += estimateConstantUtility();
        utility += estimateTravelTimeUtility(variables);
        utility += estimateAccessTimeUtility(variables);
        utility += estimateMonetaryCostUtility(variables);
        utility += estimateEgressTimeUtility(variables);
        utility +=estimateChangeLineUtility(variables);
        utility+=estimatePersonalUtility(person,trip,elements);
        return utility;
    }
}
