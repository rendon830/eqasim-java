package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.utilities;

import com.google.inject.Inject;
import org.eqasim.core.simulation.mode_choice.utilities.UtilityEstimator;
import org.eqasim.core.simulation.mode_choice.utilities.variables.WalkVariables;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMPersonPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.SMMWalkPredictor;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables.SMMEqasimPersonVariables;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contribs.discrete_mode_choice.model.DiscreteModeChoiceTrip;

import java.util.List;
/**
 * Class defines the utility of a trip done by walk, based on Krauss et al 2021
 */
public class SMMWalkEstimator implements UtilityEstimator {


    private final SMMParameters parameters;
    private final SMMWalkPredictor predictor;


    @Inject
    public SMMWalkEstimator(SMMParameters parameters, SMMWalkPredictor predictor) {
        this.parameters = parameters;
        this.predictor = predictor;

    }



    protected double estimateConstantUtility() {
        return parameters.walk.alpha_u;
    }
    protected double estimatePersonalUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements){
        SMMPersonPredictor personPredictor=new SMMPersonPredictor();
        SMMEqasimPersonVariables personVariables=personPredictor.predictVariables(person,trip,elements);
        double ageU=personVariables.age_a*parameters.walk.betaAge;
        double bikeAcc=personVariables.getBikeAcc()*parameters.walk.betaBikeAcc;
        double pTAcc=personVariables.getPtPass()*parameters.walk.betaPTPass;
        double carAcc=personVariables.getCarAccessibility()+parameters.walk.betaCarAcc;
        return(ageU+bikeAcc+pTAcc+carAcc+parameters.walk.sigma);
    }
    protected double estimateTravelTimeUtility(WalkVariables variables) {
        return parameters.walk.betaTravelTime_u_min * variables.travelTime_min;
    }

    @Override
    public double estimateUtility(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
        WalkVariables variables = predictor.predict(person, trip, elements);

        double utility = 0.0;

        utility += estimateConstantUtility();
        utility += estimateTravelTimeUtility(variables);
        utility+=estimatePersonalUtility(person,trip,elements);
        return utility;
    }
}
