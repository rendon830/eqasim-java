package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.variables;

import org.eqasim.core.simulation.mode_choice.utilities.variables.BaseVariables;

/**
 * Class  acts as container for walk trip variables for utility
 */
public class SMMWalkVariables implements BaseVariables {
    public double travelTime_u_min;

    public SMMWalkVariables(double travelTime_u_min) {
        this.travelTime_u_min = travelTime_u_min;
    }
}
