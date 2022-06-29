package org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters;

import org.eqasim.core.simulation.mode_choice.ParameterDefinition;

import java.util.HashMap;

/**
 * Class stores the cost values for all modes
 */
public class SMMCostParameters implements ParameterDefinition {
    public double carCost_Km;
    public double bookingCostBikeShare;
    public double bikeShareCost_Km;
    public double bookingCostEScooter;
    public double eScooterCost_km;
    public double pTTicketCost;
    public HashMap<String, Double> sharingBookingCosts;
    public HashMap<String, Double> sharingKMCost;

    /**
     * Builds default values
     * @return returns SMMCost parameters default
     */
    public  static SMMCostParameters buildDefault(){
        SMMCostParameters parameters = new SMMCostParameters();

        parameters.carCost_Km= 0.15;
        parameters.bookingCostBikeShare = 0.25;
        parameters.bikeShareCost_Km=1;
        parameters.bookingCostEScooter = 0.5;
        parameters.eScooterCost_km=0.5;
        parameters.pTTicketCost=1.8;

        return parameters;
    }

}
