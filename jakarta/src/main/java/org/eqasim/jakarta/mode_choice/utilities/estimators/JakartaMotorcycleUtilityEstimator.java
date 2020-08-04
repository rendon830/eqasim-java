package org.eqasim.jakarta.mode_choice.utilities.estimators;

import java.util.List;

import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.estimators.EstimatorUtils;
import org.eqasim.jakarta.mode_choice.parameters.JakartaModeParameters;
import org.eqasim.jakarta.mode_choice.utilities.predictors.JakartaMotorcyclePredictor;
import org.eqasim.jakarta.mode_choice.utilities.variables.MotorcycleVariables;
import org.eqasim.jakarta.mode_choice.utilities.variables.JakartaPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;

import com.google.inject.Inject;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class JakartaMotorcycleUtilityEstimator implements UtilityEstimator {
	private final JakartaModeParameters parameters;
	private final JakartaMotorcyclePredictor predictor;

	@Inject
	public JakartaMotorcycleUtilityEstimator(JakartaModeParameters parameters, JakartaMotorcyclePredictor predictor) {
		this.parameters = parameters;
		this.predictor = predictor;
	}

	protected double estimateConstantUtility() {
		return parameters.jMotorcycle.alpha_u;
	}

	protected double estimateTravelTimeUtility(MotorcycleVariables variables) {
		return parameters.jMotorcycle.beta_TravelTime_u_min * variables.travelTime_min;
	}

	protected double estimateAccessEgressTimeUtility(MotorcycleVariables variables) {
		return parameters.walk.betaTravelTime_u_min * variables.accessEgressTime_min;
	}

	protected double estimateMonetaryCostUtility(MotorcycleVariables variables) {
		return parameters.betaCost_u_MU * EstimatorUtils.interaction(variables.euclideanDistance_km,
				parameters.referenceEuclideanDistance_km, parameters.lambdaCostEuclideanDistance) * variables.cost_MU;
	}

	@Override
	public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		MotorcycleVariables variables = predictor.predictVariables(person, trip, elements);

		double utility = 0.0;

		utility += estimateConstantUtility();
		utility += estimateTravelTimeUtility(variables);
		utility += estimateAccessEgressTimeUtility(variables);
		utility += estimateMonetaryCostUtility(variables);
		//if (variables.hhlIncome == 0.0)
		//	utility += estimateMonetaryCostUtility(variables)
		//	* (parameters.jAvgHHLIncome.avg_hhl_income / 1.0);
		//else
		//	utility += estimateMonetaryCostUtility(variables)
		//		* (parameters.jAvgHHLIncome.avg_hhl_income / variables.hhlIncome);

		return utility;
	}
}
