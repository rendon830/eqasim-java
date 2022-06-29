package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors;

import org.eqasim.core.simulation.mode_choice.utilities.predictors.CachedVariablePredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters.SMMModeParameters;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMBikeVariables;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;

/**
 * Class predicts the elements of a trip if done by bike; based on bikesharing variables of Krauss
 */
public class SMMBikePredictor extends CachedVariablePredictor<SMMBikeVariables> {


    @Override
    public SMMBikeVariables predict(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        double travelTime_min = ((Leg) elements.get(0)).getTravelTime().seconds() / 60.0;
        SMMModeParameters params= SMMModeParameters.buildDefault();

        return new SMMBikeVariables(travelTime_min,params.bike.accessTime,params.bike.egressTime,0,0);
    }
}
