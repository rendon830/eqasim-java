package org.eqasim.examples.SMMFramework;

import com.google.common.io.Resources;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.analysis.EqasimAnalysisModule;
import org.eqasim.core.simulation.mode_choice.EqasimModeChoiceModule;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.ModeChoiceModuleExample;
import org.eqasim.examples.SMMFramework.testScenarios.utils.MicromobilityUtils;
import org.eqasim.ile_de_france.IDFConfigurator;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contribs.discrete_mode_choice.modules.ModelModule;
import org.matsim.contribs.discrete_mode_choice.modules.config.DiscreteModeChoiceConfigGroup;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.ConfigWriter;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class RunSMMFramework {
    static public void main(String[] args) throws CommandLine.ConfigurationException, IllegalAccessException, IOException {
        CommandLine cmd = new CommandLine.Builder(args) //
                .requireOptions("config-path") //
                .allowOptions("use-rejection-constraint") //
                .allowPrefixes("mode-parameter", "cost-parameter", "sharing-mode-name") //
                .build();
        Config config = ConfigUtils.loadConfig(cmd.getOptionStrict("config-path"), IDFConfigurator.getConfigGroups());
        cmd.applyConfiguration(config);

        config.qsim().setFlowCapFactor(1e9);
        config.qsim().setStorageCapFactor(1e9);
        config.controler().setWriteEventsInterval(1);
        config.controler().setWritePlansInterval(1);
        config.controler().setLastIteration(50);

//        // Set up controller
        Controler controller = new Controler(config);

        Scenario scenario = ScenarioUtils.createScenario(config);
        scenario = ScenarioUtils.loadScenario(config);
        IDFConfigurator.configureScenario(scenario);

// Sets SMM Framework choice model
        {
            // Set up choice model
            EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);


            eqasimConfig.setEstimator("bike", "KBike");
            eqasimConfig.setEstimator("pt", "KPT");
            eqasimConfig.setEstimator("car", "KCar");
            eqasimConfig.setEstimator("walk", "KWalk");
            eqasimConfig.setCostModel("pt", "pt");
            eqasimConfig.setCostModel("car", "car");
            // Set analysis interval
            eqasimConfig.setTripAnalysisInterval(1);
        }
        IDFConfigurator.configureController(controller);

        // Sets eqasim as a trip based model
        {
            DiscreteModeChoiceConfigGroup dmcConfig = (DiscreteModeChoiceConfigGroup) config.getModules()
                    .get(DiscreteModeChoiceConfigGroup.GROUP_NAME);
            dmcConfig.setModeAvailability("GENMODE");
            dmcConfig.setModelType(ModelModule.ModelType.Trip);
            Collection<String> tripF = dmcConfig.getTripFilters();
            tripF.removeAll(tripF);
            dmcConfig.setTripFilters(tripF);
        }
        //Add overriding modules
        controller.addOverridingModule(new EqasimAnalysisModule());
        controller.addOverridingModule(new EqasimModeChoiceModule());
        // Add mode choice SMM module
        controller.addOverridingModule(new ModeChoiceModuleExample(cmd, scenario));
        // Add SMM modes
        MicromobilityUtils.addSharingServices(cmd, controller, config, scenario);
        controller.run();

    }
}
//
//    static public HashMap<String,HashMap<String,String>> applyCommandLine(String prefix, CommandLine cmd) {
//        HashMap<String,HashMap<String,String>>  sharingModesInput= new HashMap<>();
//        List<String> sharingModes=indentifySharingModes(prefix,cmd);
//        int i=0;
//        while(i<sharingModes.size()){
//            sharingModesInput.put(sharingModes.get(i), new HashMap<String,String>());
//            i+=1;
//        }
//        buildParameters(prefix,cmd,sharingModesInput);
//        validateInput(sharingModesInput);
//        return sharingModesInput;
//    }
//    static public List<String> indentifySharingModes(String prefix, CommandLine cmd) {
//        List<String> sharingModes= new ArrayList<>();
//        for (String option : cmd.getAvailableOptions()) {
//            if (option.startsWith(prefix + ":")) {
//                try {
//                    String optionPart2=option.split(":")[1];
//                    if (optionPart2.startsWith("Service_Name")) {
//                        sharingModes.add(cmd.getOptionStrict(option));
//                    }
//                } catch (CommandLine.ConfigurationException e) {
//                    //Should not happen
//                }
//            }
//        }
//      return sharingModes;
//    }
//    static public void buildParameters(String prefix,CommandLine cmd, HashMap<String,HashMap<String,String>>services) {
//
//        for (String option : cmd.getAvailableOptions()) {
//            if (option.startsWith(prefix + ":")) {
//                try {
//                    String optionPart2=option.split(":")[1];
//                    String parameter=optionPart2.split("\\.")[0];
//                    String serviceName=optionPart2.split("\\.")[1];
//                    HashMap<String,String>mapService=services.get(serviceName);
//                    mapService.put(parameter,cmd.getOptionStrict(option));
//
//                } catch (CommandLine.ConfigurationException e) {
//                    //Should not happen
//                }
//            }
//        }
//    }
//    static public void validateInput( HashMap<String,HashMap<String,String>> services) {
//        ArrayList<String> obligatoryValues = new ArrayList<>(Arrays.asList("Service_File", "Mode", "Scheme","Service_Name"));
//        for (String key : services.keySet()) {
//                HashMap<String,String>service=(HashMap)services.get(key);
//            if (service.keySet().containsAll(obligatoryValues)== false) {
//
//
//                throw new IllegalArgumentException("Please check the service parameters for the service: "+key+"there must be a GBFS, a Mode file and the Scheme type");
//            }
//        }
//    }
//}
