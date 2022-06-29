package org.eqasim.examples.corsica_drt.Drafts.otherDrafts;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMBikeShareVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

public class KraussBikeShareEstimator implements UtilityEstimator {
    private final SMMParameters parameters;
    private final KraussBikeSharePredictor predictor;

    @Inject
    public KraussBikeShareEstimator(SMMParameters parameters, KraussBikeSharePredictor predictor, SMMPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }

    public KraussBikeShareEstimator(SMMParameters parameters, KraussBikeSharePredictor predictor, SMMPersonPredictor personPredictor, String name) {
        this.parameters = parameters;
        this.predictor = predictor;

    }

    protected double estimateConstantUtility() {
        return parameters.bikeShare.alpha_u;
    }
    protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip,List<? extends PlanElement> elements){
        SMMPersonPredictor personPredictor=new SMMPersonPredictor();
        SMMEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
        double ageU=personVariables.age_a*parameters.bikeShare.betaAge;
        double bikeAcc=personVariables.getBikeAcc()*parameters.bikeShare.betaBikeAcc;
        double carAcc=personVariables.getCarAccessibility()*parameters.bikeShare.betaCarAcc;
        double pTAcc=personVariables.getPtPass()*parameters.bikeShare.betaPTPass;
        return(ageU+bikeAcc+carAcc+pTAcc);
    }
    protected double estimateTravelTimeUtility(SMMBikeShareVariables variables) {
        return parameters.bikeShare.betaTravelTime_u_min * variables.travelTime_u_min;
    }

    protected double estimateAccessTimeUtility(SMMBikeShareVariables variables) {
        return parameters.bikeShare.betaAccess_Time * variables.access_Time;
    }

    protected double estimateMonetaryCostUtility(SMMBikeShareVariables variables) {
        return parameters.betaCost_u_MU * variables.cost;
    }

    protected double estimateEgressTimeUtility(SMMBikeShareVariables variables) {
        return parameters.bikeShare.betaEgress_Time* variables.egress_Time;
    }

    protected double estimateParkingTimeUtility(SMMBikeShareVariables variables){
        return parameters.bikeShare.betaParkingTime_u_min*variables.parkingTime_u_min;
    }
    protected double estimatePedelecUtility(SMMBikeShareVariables variables){
        return parameters.bikeShare.betaPedelec*variables.pedelec;
    }


    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        SMMBikeShareVariables variables = predictor.predict(person, trip, elements);

        double utility = 0.0;

        utility += estimateConstantUtility();
        utility += estimateTravelTimeUtility(variables);
        utility += estimateAccessTimeUtility(variables);
        utility += estimateMonetaryCostUtility(variables);
        utility += estimateEgressTimeUtility(variables);
        utility+= estimateParkingTimeUtility(variables);
        utility+=estimatePedelecUtility(variables);
        utility+=estimatePersonalUtility(person,trip,elements);
        return utility;
    }


}

