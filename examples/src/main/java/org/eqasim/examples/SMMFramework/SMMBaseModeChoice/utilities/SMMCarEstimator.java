package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.utilities;


import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMCarPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMCarVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;
/**
 * Class defines the utility of a trip done by car, based on Krauss et al 2021
 */

public class SMMCarEstimator implements UtilityEstimator {
    private final SMMParameters parameters;
    private final SMMCarPredictor predictor;


    @Inject
    public SMMCarEstimator(SMMParameters parameters, SMMCarPredictor predictor, SMMPersonPredictor personPredictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }
    public SMMCarEstimator(SMMParameters parameters, SMMCarPredictor predictor, SMMPersonPredictor personPredictor, Boolean isStatic) {
        this.parameters = parameters;
        this.predictor = predictor;

    }


    protected double estimateTravelTimeUtility(SMMCarVariables variables) {
        return parameters.car.betaTravelTime_u_min * variables.travelTime_u_min;
    }

    protected double estimateAccessTimeUtility(SMMCarVariables variables) {
        return parameters.car.betaAccess_Time * variables.access_Time;
    }

    protected double estimateMonetaryCostUtility(SMMCarVariables variables) {
        return -Math.exp(parameters.betaCost_u_MU) * variables.cost;
    }

    protected double estimateEgressTimeUtility(SMMCarVariables variables) {
        return parameters.car.betaEgress_Time* variables.egress_Time;
    }

    protected double estimateParkingTimeUtility(SMMCarVariables variables){
        return parameters.car.betaParkingTime_u_min*variables.parkingTime_u_min;
    }


    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        SMMCarVariables variables = predictor.predict(person, trip, elements);

        double utility = 0.0;
        utility += estimateTravelTimeUtility(variables);
        utility += estimateAccessTimeUtility(variables);
        utility += estimateMonetaryCostUtility(variables);
        utility += estimateEgressTimeUtility(variables);
        utility+= estimateParkingTimeUtility(variables);
        utility=utility*parameters.car.pool;

        return utility;
    }
}
