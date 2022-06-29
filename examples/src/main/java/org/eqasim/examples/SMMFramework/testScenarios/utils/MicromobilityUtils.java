package org.eqasim.examples.SMMFramework.testScenarios.utils;

import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.mode_choice.ParameterDefinition;
import org.eqasim.examples.SMMFramework.GBFSUtils.ReadFreeFloatingGBFS;
import org.eqasim.examples.SMMFramework.GBFSUtils.ReadStationBasedGBFS;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedMultimodalRoutingSMM.SMMSharingPTModule;
import org.eqasim.examples.corsica_drt.Drafts.DScripts.MicroMobilityModeEqasimModeChoiceModule;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.SMMEqasimModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.sharing.run.SharingConfigGroup;
import org.matsim.contrib.sharing.run.SharingModule;
import org.matsim.contrib.sharing.run.SharingServiceConfigGroup;
import org.matsim.contrib.sharing.service.SharingUtils;
import org.matsim.contribs.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.controler.Controler;

import java.io.IOException;
import java.util.*;

/**
 * Class provides the utilities for  installinng SMM modes in MATSim
 */

public class MicromobilityUtils implements ParameterDefinition {

    public String mode;
    public String serviceScheme;
    public String serviceArea=null;
    public Double accessDist;

    /**
     * Master method of the class; recieves commandLine arguments and configures all services into the config and controller
     * @param cmd commandLine arguments
     * @param controller controller
     * @param config config MATSim
     * @param scenario Scenario matsin´m
     * @throws IllegalAccessException
     */
    public static void  addSharingServices(CommandLine cmd,Controler controller, Config config,Scenario scenario) throws IllegalAccessException {
        HashMap<String,HashMap<String,String>> sharingServicesInput=applyCommandLineServices("sharing-mode-name",cmd);
        //  Creates the Services in a HashMap
        try {
            generateServiceFile(sharingServicesInput,config,scenario);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Iterates through Services Map
        for( String key :sharingServicesInput.keySet()) {
            HashMap<String,String>service=sharingServicesInput.get(key);
            // Temporal specification of service
                String serviceFile=service.get("Service_File");
                String mode=service.get("Mode");
                String name=service.get("Service_Name");
                Double accessEgress=Double.parseDouble(service.get("AccessEgress_Distance"));
                String scheme=service.get("Scheme");
                String multimodal=service.get("Multimodal");
                String serviceArea="";

                if(service.keySet().contains("Service_Area")){
                    serviceArea=service.get("Service_Area");
                }
                // Adds SMM sharing service to the Config
                addSharingService(controller,config,mode,scheme,accessEgress,serviceArea,serviceFile,name,multimodal);
                // Adds SMM sharing service to Eqasim mode choice
                addSharingServiceToEqasim(controller,config,cmd,scenario,service);
                // Adds Eqasim+ SMM multimodal
                if(multimodal.equals("Yes")){
                    controller.addOverridingModule(new SMMSharingPTModule(scenario,name));
                }
        }
        // Adds the Sharing Contrib
        controller.addOverridingModule(new SharingModule());
        // Sets Sharing Contrib simulation
        controller.configureQSimComponents(SharingUtils.configureQSim((SharingConfigGroup) config.getModules().get("sharing")));

    }


    /**
     * Master method that links the sharing service specifiction into Eqasim, MATSim and Eqasim+SMM
     * @param controller MATsim controller
     * @param config MATSim config
     * @param mode routing  mode
     * @param serviceScheme service scheme of mode
     * @param accessDist access/ egress distance to mode
     * @param serviceArea Service area shp file
     * @param serviceFile Service files( created from GBFS)
     * @param name     name of the service
     * @param multimodal  is connector to PT?
     * @throws IllegalAccessException
     */

    public static void addSharingService(Controler controller, Config config, String mode, String serviceScheme, Double accessDist, String serviceArea, String serviceFile, String name, String multimodal) throws IllegalAccessException {

        // creates the Sharing contrib config
        SharingConfigGroup sharingConfigGroup = (SharingConfigGroup) config.getModules().get("sharing");
        if (sharingConfigGroup == null) {
            sharingConfigGroup = new SharingConfigGroup();
            config.addModule(sharingConfigGroup);
        }
        // Creates the routing parameters of SMM modes
        Map<String, PlansCalcRouteConfigGroup.ModeRoutingParams> routingParams = config.plansCalcRoute().getModeRoutingParams();
        if(routingParams.containsKey("Shared-Bike")== false && routingParams.containsKey("eScooter")==false) {
            addSharedModes(config);
        }
        // Verify that the service has the right service type and routing type
        String serviceSchemes[] = new String[]{"Station-Based", "Free-floating"};
        String possibleModes[] = new String[]{"Shared-Bike", "eScooter"};
        boolean contains = Arrays.stream(possibleModes).anyMatch(mode::equals);
        if (contains == false) {
            {
                throw new IllegalArgumentException(" The sharing mode is invalid; please insert Shared-Bike or eScooter");
            }
        } else {
            boolean serviceSchemeVerification = Arrays.stream(serviceSchemes).anyMatch(serviceScheme::equals);
            if (serviceSchemeVerification == false) {
                {
                    throw new IllegalArgumentException(" The service scheme is invalid; please insert Station Based or Free-floating");
                }
                // if the values re correct, configures it into Sharing contrib config and base MATSin
            } else {
                if (serviceScheme.equals("Station-Based")) {
                    addSharedStationBasedService(config, sharingConfigGroup, accessDist, name, mode, serviceFile);
                } else if (serviceScheme.equals("Free-floating")) {
                    addSharedFreeFloatingService(config, sharingConfigGroup, serviceArea, accessDist, name, mode, serviceFile);
                }
            }
        }

        // Scores Sharing contrib Activities
        PlanCalcScoreConfigGroup.ActivityParams pickupParams = new PlanCalcScoreConfigGroup.ActivityParams(SharingUtils.PICKUP_ACTIVITY);
        pickupParams.setScoringThisActivityAtAll(false);
        config.planCalcScore().addActivityParams(pickupParams);

        PlanCalcScoreConfigGroup.ActivityParams dropoffParams = new PlanCalcScoreConfigGroup.ActivityParams(SharingUtils.DROPOFF_ACTIVITY);
        dropoffParams.setScoringThisActivityAtAll(false);
        config.planCalcScore().addActivityParams(dropoffParams);

        PlanCalcScoreConfigGroup.ActivityParams bookingParams = new PlanCalcScoreConfigGroup.ActivityParams(SharingUtils.BOOKING_ACTIVITY);
        bookingParams.setScoringThisActivityAtAll(false);
        config.planCalcScore().addActivityParams(bookingParams);


    }






    /**
    / Method adds the routing parameters for Shared Bike mode & eScooter ( Based on speeds from Hamad et al ,2020)
     **/
    public static void addSharedModes(Config config){
        PlansCalcRouteConfigGroup.ModeRoutingParams bikeRoutingParams = new PlansCalcRouteConfigGroup.ModeRoutingParams("Shared-Bike");
        bikeRoutingParams.setTeleportedModeSpeed(3.44);
        bikeRoutingParams.setBeelineDistanceFactor(1.3);
        config.plansCalcRoute().addModeRoutingParams(bikeRoutingParams);


        PlansCalcRouteConfigGroup.ModeRoutingParams eScooterRoutingParams = new PlansCalcRouteConfigGroup.ModeRoutingParams("eScooter");
        eScooterRoutingParams.setTeleportedModeSpeed(2.78);
        eScooterRoutingParams.setBeelineDistanceFactor(1.3);
        config.plansCalcRoute().addModeRoutingParams(eScooterRoutingParams);


        // We need to score bike
        PlanCalcScoreConfigGroup.ModeParams bikeScoringParams = new PlanCalcScoreConfigGroup.ModeParams("Shared-Bike");
        config.planCalcScore().addModeParams(bikeScoringParams);
        PlanCalcScoreConfigGroup.ModeParams eScooterScoringParams = new PlanCalcScoreConfigGroup.ModeParams("eScooter");
      config.planCalcScore().addModeParams(eScooterScoringParams);

    }

    /**
     * Adds  the SMM service as  free floating service in the sharing contrib
     * @param config MATSIm config
     * @param configGroup Sharing Contrib config
     * @param serviceArea Service area shp file
     * @param accessDistance  access/ egress distance to mode
     * @param name name of the service
     * @param mode routing  mode
     * @param serviceFile Service files( created from GBFS)

     */
    public static void addSharedFreeFloatingService(Config config,SharingConfigGroup configGroup, String serviceArea,Double accessDistance, String name, String mode,String serviceFile) {

        SharingServiceConfigGroup serviceConfig = new SharingServiceConfigGroup();
        configGroup.addService(serviceConfig);

        // Sets id of service
        serviceConfig.setId(name);

        // Sets  freefloating characteristics
        serviceConfig.setMaximumAccessEgressDistance(accessDistance);
        serviceConfig.setServiceScheme(SharingServiceConfigGroup.ServiceScheme.Freefloating);
        serviceConfig.setServiceAreaShapeFile(serviceArea);
        serviceConfig.setServiceInputFile(serviceFile);




        // Set the routing mode
        serviceConfig.setMode(mode);

        // considered in mode choice.
        List<String> modes = new ArrayList<>(Arrays.asList(config.subtourModeChoice().getModes()));
        modes.add(SharingUtils.getServiceMode(serviceConfig));
        config.subtourModeChoice().setModes(modes.toArray(new String[modes.size()]));
    }

    /**
     *  Adds a service  to the Shairng contrib as station based
     *@param config MATSIm config
     * @param configGroup Sharing Contrib config
     * @param accessDistance  access/ egress distance to mode
     * @param name name of the service
     * @param mode routing  mode
     * @param serviceFile Service files( created from GBFS)
     */
    public static void addSharedStationBasedService(Config config, SharingConfigGroup configGroup, Double accessDistance, String name, String mode, String serviceFile) {

        SharingServiceConfigGroup serviceConfig = new SharingServiceConfigGroup();
        configGroup.addService(serviceConfig);

        // Sets id of service
        serviceConfig.setId(name);

        // Sets as Station based
        serviceConfig.setMaximumAccessEgressDistance(accessDistance);
        serviceConfig.setServiceScheme(SharingServiceConfigGroup.ServiceScheme.StationBased);
        serviceConfig.setServiceInputFile(serviceFile);
        // Sets routing mode
        serviceConfig.setMode(mode);

        // considered in mode choice.
        List<String> modes = new ArrayList<>(Arrays.asList(config.subtourModeChoice().getModes()));
        modes.add(SharingUtils.getServiceMode(serviceConfig));
        config.subtourModeChoice().setModes(modes.toArray(new String[modes.size()]));
    }
//

    public static void addSharingServiceToEqasim(Controler controller,Config config, CommandLine cmd, Scenario scenario,String name,String serviceFile){
        EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);
// Scoring config definition to add the mode cat_pt parameters
        PlanCalcScoreConfigGroup scoringConfig = config.planCalcScore();
        PlanCalcScoreConfigGroup.ModeParams sharingPTParams = new PlanCalcScoreConfigGroup.ModeParams(name+"_PT");
        PlanCalcScoreConfigGroup.ModeParams pTSharingParams = new PlanCalcScoreConfigGroup.ModeParams("PT_"+name);
        PlanCalcScoreConfigGroup.ModeParams SharingPTSharingParams = new PlanCalcScoreConfigGroup.ModeParams(name+"_PT_"+name);
        scoringConfig.addModeParams(SharingPTSharingParams);
        scoringConfig.addModeParams(sharingPTParams);
        scoringConfig.addModeParams(pTSharingParams);

        // "car_pt interaction" definition
        PlanCalcScoreConfigGroup.ActivityParams paramsSharingPTInterAct = new PlanCalcScoreConfigGroup.ActivityParams("SharingPT interaction");
        paramsSharingPTInterAct.setTypicalDuration(100.0);
        paramsSharingPTInterAct.setScoringThisActivityAtAll(false);

        // "pt_car interaction" definition
        PlanCalcScoreConfigGroup.ActivityParams paramsPTSharingInterAct = new PlanCalcScoreConfigGroup.ActivityParams("PTSharing interaction");
        paramsPTSharingInterAct.setTypicalDuration(100.0);
        paramsPTSharingInterAct.setScoringThisActivityAtAll(false);

        // Adding "car_pt interaction" to the scoring
        scoringConfig.addActivityParams(paramsSharingPTInterAct);
        scoringConfig.addActivityParams(paramsPTSharingInterAct);


        // Adding "car_pt interaction" to the scoring
        scoringConfig.addActivityParams(paramsPTSharingInterAct);
        scoringConfig.addActivityParams(paramsSharingPTInterAct);

        //Key Apart from modifying the  binders , add the neww estimators, etc etc
        DiscreteModeChoiceConfigGroup dmcConfig = DiscreteModeChoiceConfigGroup.getOrCreate(config);

        Set<String> cachedModes = new HashSet<>();
        cachedModes.addAll(dmcConfig.getCachedModes());
        cachedModes.add("sharing:"+name);
        dmcConfig.setCachedModes(cachedModes);

        eqasimConfig.setCostModel("sharing:"+name,"sharing:"+name);
        eqasimConfig.setEstimator("sharing:"+name,"sharing:"+name);

        eqasimConfig.setEstimator("PT_bikeShare","PT_bikeShare");
        dmcConfig.setModeAvailability("ModeAvailability");


        controller.addOverridingModule(new MicroMobilityModeEqasimModeChoiceModule(cmd,scenario,name,serviceFile));


    }

    public static void addSharingServiceToEqasim(Controler controller,Config config, CommandLine cmd, Scenario scenario,HashMap<String,String> service){
        EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);
// Scoring config definition to add the mode cat_pt parameters
        String name=service.get("Service_Name");
        PlanCalcScoreConfigGroup scoringConfig = config.planCalcScore();
        PlanCalcScoreConfigGroup.ModeParams sharingPTParams = new PlanCalcScoreConfigGroup.ModeParams(name+"_PT");
        PlanCalcScoreConfigGroup.ModeParams pTSharingParams = new PlanCalcScoreConfigGroup.ModeParams("PT_"+name);
        PlanCalcScoreConfigGroup.ModeParams SharingPTSharingParams = new PlanCalcScoreConfigGroup.ModeParams(name+"_PT_"+name);
        scoringConfig.addModeParams(SharingPTSharingParams);
        scoringConfig.addModeParams(sharingPTParams);
        scoringConfig.addModeParams(pTSharingParams);

        // "car_pt interaction" definition
        PlanCalcScoreConfigGroup.ActivityParams paramsSharingPTInterAct = new PlanCalcScoreConfigGroup.ActivityParams("SharingPT interaction");
        paramsSharingPTInterAct.setTypicalDuration(100.0);
        paramsSharingPTInterAct.setScoringThisActivityAtAll(false);

        // "pt_car interaction" definition
        PlanCalcScoreConfigGroup.ActivityParams paramsPTSharingInterAct = new PlanCalcScoreConfigGroup.ActivityParams("PTSharing interaction");
        paramsPTSharingInterAct.setTypicalDuration(100.0);
        paramsPTSharingInterAct.setScoringThisActivityAtAll(false);

        // Adding "car_pt interaction" to the scoring
        scoringConfig.addActivityParams(paramsSharingPTInterAct);
        scoringConfig.addActivityParams(paramsPTSharingInterAct);


        // Adding "car_pt interaction" to the scoring
        scoringConfig.addActivityParams(paramsPTSharingInterAct);
        scoringConfig.addActivityParams(paramsSharingPTInterAct);

        //Key Apart from modifying the  binders , add the neww estimators, etc etc
        DiscreteModeChoiceConfigGroup dmcConfig = DiscreteModeChoiceConfigGroup.getOrCreate(config);

        Set<String> cachedModes = new HashSet<>();
        cachedModes.addAll(dmcConfig.getCachedModes());
        cachedModes.add("sharing:"+name);
        dmcConfig.setCachedModes(cachedModes);

        eqasimConfig.setCostModel("sharing:"+name,"sharing:"+name);
        eqasimConfig.setEstimator("sharing:"+name,"sharing:"+name);
        Set<String> tripConstraints = new HashSet<>(dmcConfig.getTripConstraints());
        tripConstraints.add(name);
        dmcConfig.setTripConstraints(tripConstraints);

        if(service.get("Multimodal").equals("Yes")){
            eqasimConfig.setEstimator("PT_"+name,"PT_"+name);
            eqasimConfig.setEstimator(name+"_PT_"+name,name+"_PT_"+name);
            eqasimConfig.setEstimator(name+"_PT",name+"_PT");
            cachedModes.add("PT_"+name);
            cachedModes.add(name+"_PT_"+name);
            cachedModes.add(name+"_PT");
            dmcConfig.setCachedModes(cachedModes);
            tripConstraints = new HashSet<>(dmcConfig.getTripConstraints());
            tripConstraints.add(name+"_PT_CONSTRAINT");
           dmcConfig.setTripConstraints(tripConstraints);
        }
//        eqasimConfig.setEstimator("PT_bikeShare","PT_bikeShare");
        dmcConfig.setModeAvailability(service.get("Service_Name")+"ModeAvailability");

        controller.addOverridingModule(new SMMEqasimModule(cmd,scenario,service));



    }
    static public HashMap<String,HashMap<String,String>> applyCommandLineServices(String prefix, CommandLine cmd) {
        HashMap<String,HashMap<String,String>>  sharingModesInput= new HashMap<>();
        List<String> sharingModes=indentifySharingModes(prefix,cmd);
        int i=0;
        while(i<sharingModes.size()){
            sharingModesInput.put(sharingModes.get(i), new HashMap<String,String>());
            i+=1;
        }
        buildParameters(prefix,cmd,sharingModesInput);
        validateInputGBFS(sharingModesInput);
        return sharingModesInput;
    }
    static public List<String> indentifySharingModes(String prefix, CommandLine cmd) {
        List<String> sharingModes= new ArrayList<>();
        for (String option : cmd.getAvailableOptions()) {
            if (option.startsWith(prefix + ":")) {
                try {
                    String optionPart2=option.split(":")[1];
                    if (optionPart2.startsWith("Service_Name")) {
                        sharingModes.add(cmd.getOptionStrict(option));
                    }
                } catch (CommandLine.ConfigurationException e) {
                    //Should not happen
                }
            }
        }
        return sharingModes;
    }
/**
 * The following methods are in charge of translating the command line inputs into SMM services
 */
    /**
     * Method process the command line arguments  given one prefix("sharing-service-name")
     * @param prefix prefix that id's the values
     * @param cmd command Line values
     * @param services Empty hashmap of services ; will be filled with the values
     */
    static public void buildParameters(String prefix,CommandLine cmd, HashMap<String,HashMap<String,String>>services) {
        // Iterates through options
        for (String option : cmd.getAvailableOptions()) {
            if (option.startsWith(prefix + ":")) {
                try {
                    String optionPart2=option.split(":")[1];
                    String parameter=optionPart2.split("\\.")[0];
                    String serviceName=optionPart2.split("\\.")[1];
                    HashMap<String,String>mapService=services.get(serviceName);
                    mapService.put(parameter,cmd.getOptionStrict(option));

                } catch (CommandLine.ConfigurationException e) {
                    //Should not happen
                }
            }
        }
    }

    /**
     * Method  validates that each SMM service has the minimum parameters of SMM Framework
     * @param services data of services
     */
    static public void validateInput( HashMap<String,HashMap<String,String>> services) {
        ArrayList<String> obligatoryValues = new ArrayList<>(Arrays.asList("Service_File", "Mode", "Scheme","Service_Name","Multimodal","AccessEgress_Distance"));
        for (String key : services.keySet()) {
            HashMap<String,String>service=(HashMap)services.get(key);
            if (service.keySet().containsAll(obligatoryValues)== false) {

                throw new IllegalArgumentException("Please check the service parameters for the service: "+key+"there must be a GBFS, a Mode file , Scheme type and if its multimodal");
            }
        }
    }

    /**
     *  Method validates that the GBFS files  exist
     * @param services service specification parameters
     */
    static public void validateInputGBFS( HashMap<String,HashMap<String,String>> services) {
        ArrayList<String> obligatoryValues = new ArrayList<>(Arrays.asList("Mode", "Scheme","Service_Name","Multimodal","AccessEgress_Distance"));
        for (String key : services.keySet()) {
            HashMap<String,String>service=(HashMap)services.get(key);
            if(service.get("Scheme").equals("Station-Based")){
                 obligatoryValues = new ArrayList<>(Arrays.asList("Mode", "Scheme","Service_Name","Multimodal","AccessEgress_Distance","StationsGBFS","StationsStatusGBFS"));
               if (service.keySet().containsAll(obligatoryValues)== false) {


                    throw new IllegalArgumentException("Please check the service parameters for the service: "+key+"there must be  two GBFS, a Mode file , Scheme type and if its multimodal");
                }

            }else{
                obligatoryValues = new ArrayList<>(Arrays.asList("Mode", "Scheme","Service_Name","Multimodal","AccessEgress_Distance","FreeVehiclesGBFS"));
                if (service.keySet().containsAll(obligatoryValues)== false) {


                    throw new IllegalArgumentException("Please check the service parameters for the service: "+key+"there must be a GBFS, a Mode file , Scheme type and if its multimodal");
                }

            }

        }
    }

    /**
     * Transform a HashMap of values to severl services  with specifications of their owmn
     * @param services values of several services
     * @param config MATSim Config
     * @param scenario MATSim scenario
     * @throws IOException
     */
    public static void generateServiceFile(HashMap<String,HashMap<String,String>>services, Config config,Scenario scenario) throws IOException {
        for (String key : services.keySet()) {
            HashMap<String, String> service = (HashMap) services.get(key);
            if(service.get("Scheme").equals("Station-Based")){
                Network network=scenario.getNetwork();
               service.put("Service_File",ReadStationBasedGBFS.readGBFSStationBased(service.get("StationsGBFS"),"",network,service.get("StationsStatusGBFS"),service.get("Service_Name"))) ;
            }
            if(service.get("Scheme").equals("Free-floating")){
                Network network=scenario.getNetwork();
                service.put("Service_File", ReadFreeFloatingGBFS.readGBFSFreeFloating(service.get("FreeVehiclesGBFS"),"",network,service.get("Service_Name"))) ;
            }
        }
    }
}


