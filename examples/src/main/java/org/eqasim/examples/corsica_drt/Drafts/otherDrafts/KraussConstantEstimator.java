package org.eqasim.examples.corsica_drt.Drafts.otherDrafts;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters.SMMModeParameters;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMBikePredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMBikeVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

public class KraussConstantEstimator implements UtilityEstimator {
    private final SMMModeParameters parameters;
    private final SMMBikePredictor predictor;

    @Inject
    public KraussConstantEstimator(SMMModeParameters parameters, SMMBikePredictor predictor, SMMPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }

    protected double estimateConstantUtility() {
        return parameters.bikeShare.personConstant;
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
        return parameters.betaCost_u_MU * variables.cost;
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

        double utility = 1.0;


        return utility;
    }


}

