package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors;

import org.eqasim.core.simulation.mode_choice.utilities.predictors.CachedVariablePredictor;
import org.eqasim.core.simulation.mode_choice.utilities.predictors.PredictorUtils;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

/**
 *  Class  determines bike accessibility , car accessibility and pt subscription of a person
 */
public class SMMPersonPredictor extends CachedVariablePredictor<SMMEqasimPersonVariables> {
	/**
	 * Method established the car, bike and pt accessibility based on socidemographics
	 * @param person MATSim person object
	 * @param trip trip ( not used)
	 * @param elements trip elements( not used)
	 * @return Person Variables with accessibilities
	 */
	@Override

	public SMMEqasimPersonVariables predict(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		int age_a = PredictorUtils.getAge(person);
		int bikeAcc=0;
		int carAcc=0;
		int ptPass=0;
		// If always or sometime has bike availavke
		String attBAcc= (String) person.getAttributes().getAttribute("bikeAvailability");
		if(attBAcc=="all" || attBAcc=="some"){
			bikeAcc=1;
		}
		// If always or sometimes has a car available
		String attCAcc= (String) person.getAttributes().getAttribute("carAvailability");
		if(attBAcc=="all" || attBAcc=="some"){
			carAcc=1;
		}
		// If has pt pass
		Boolean attPtPass=(Boolean) person.getAttributes().getAttribute("hasPtSubscription");
		if(attPtPass){
			ptPass=1;
		}

		return(new SMMEqasimPersonVariables(age_a,bikeAcc,carAcc,ptPass,0));
	}
}
