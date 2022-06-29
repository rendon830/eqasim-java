package org.eqasim.examples.corsica_drt.Drafts.otherDrafts;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEScooterVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

public class KraussEScooterEstimator implements UtilityEstimator {
	private final SMMParameters parameters;
	private final KraussEScooterPredictor predictor;


	@Inject
	public KraussEScooterEstimator(SMMParameters parameters, KraussEScooterPredictor predictor, SMMPersonPredictor personPredictor) {
		this.parameters = parameters;
		this.predictor = predictor;

	}

	protected double estimateConstantUtility() {
		return parameters.eScooter.alpha_u;
	}
	protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip,List<? extends PlanElement> elements){
		SMMPersonPredictor personPredictor=new SMMPersonPredictor();
		SMMEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
		double ageU=personVariables.age_a*parameters.eScooter.betaAge;
		double bikeAcc=personVariables.getBikeAcc()*parameters.eScooter.betaBikeAcc;
		double carAcc=personVariables.getCarAccessibility()*parameters.eScooter.betaCarAcc;
		double pTAcc=personVariables.getPtPass()*parameters.eScooter.betaPTPass;
		return(ageU+bikeAcc+carAcc+pTAcc);
	}
	protected double estimateTravelTimeUtility(SMMEScooterVariables variables) {
		return parameters.eScooter.betaTravelTime_u_min * variables.travelTime_u_min;
	}

	protected double estimateAccessTimeUtility(SMMEScooterVariables variables) {
		return parameters.eScooter.betaAccess_Time * variables.access_Time;
	}

	protected double estimateMonetaryCostUtility(SMMEScooterVariables variables) {
		return parameters.betaCost_u_MU * variables.cost;
	}

	protected double estimateEgressTimeUtility(SMMEScooterVariables variables) {
		return parameters.eScooter.betaEgress_Time* variables.egress_Time;
	}

	protected double estimateParkingTimeUtility(SMMEScooterVariables variables){
		return parameters.eScooter.betaParkingTime_u_min*variables.parkingTime_u_min;
	}


	@Override
	public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		SMMEScooterVariables variables = predictor.predict(person, trip, elements);

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
