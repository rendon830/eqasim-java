package org.eqasim.examples.corsica_drt.mode_choice.utilities;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.corsica_drt.mode_choice.parameters.KraussModeParameters;
import org.eqasim.examples.corsica_drt.mode_choice.predictors.KraussBikePredictor;
import org.eqasim.examples.corsica_drt.mode_choice.predictors.KraussPersonPredictor;
import org.eqasim.examples.corsica_drt.mode_choice.variables.KraussBikeVariables;
import org.eqasim.examples.corsica_drt.mode_choice.variables.KraussEqasimPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

public class KraussConstantEstimator implements UtilityEstimator {
    private final KraussModeParameters parameters;
    private final KraussBikePredictor predictor;

    @Inject
    public KraussConstantEstimator(KraussModeParameters parameters, KraussBikePredictor predictor, KraussPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }

    protected double estimateConstantUtility() {
        return parameters.bikeShare.personConstant;
    }
    protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip,List<? extends PlanElement> elements){
        KraussPersonPredictor personPredictor=new KraussPersonPredictor();
        KraussEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
        double ageU=personVariables.age_a*parameters.bike.betaAge;
        double bikeAcc=personVariables.getBikeAcc()*parameters.bike.betaBikeAcc;
        double carAcc=personVariables.getCarAccessibility()*parameters.bike.betaCarAcc;
        double pTAcc=personVariables.getPtPass()*parameters.bike.betaPTPass;
        return(ageU+bikeAcc+carAcc+pTAcc);
    }
    protected double estimateTravelTimeUtility(KraussBikeVariables variables) {
        return parameters.bike.betaTravelTime_u_min * variables.travelTime_u_min;
    }

    protected double estimateAccessTimeUtility(KraussBikeVariables variables) {
        return parameters.bike.betaAccess_Time * variables.access_Time;
    }

    protected double estimateMonetaryCostUtility(KraussBikeVariables variables) {
        return parameters.betaCost_u_MU * variables.cost;
    }

    protected double estimateEgressTimeUtility(KraussBikeVariables variables) {
        return parameters.bike.betaEgress_Time* variables.egress_Time;
    }

    protected double estimateParkingTimeUtility(KraussBikeVariables variables){
        return parameters.bike.betaParkingTime_u_min*variables.parkingTime_u_min;
    }


    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        KraussBikeVariables variables = predictor.predict(person, trip, elements);

        double utility = 1.0;


        return utility;
    }


}

