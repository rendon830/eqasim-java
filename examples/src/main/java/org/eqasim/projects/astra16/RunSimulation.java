package org.eqasim.projects.astra16;

import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.analysis.EqasimAnalysisModule;
import org.eqasim.core.simulation.calibration.CalibrationModule;
import org.eqasim.core.simulation.mode_choice.EqasimModeChoiceModule;
import org.eqasim.projects.astra16.convergence.ConvergenceModule;
import org.eqasim.projects.astra16.travel_time.SmoothingTravelTimeModule;
import org.eqasim.projects.astra16.travel_time.TravelTimeComparisonModule;
import org.eqasim.switzerland.SwitzerlandConfigurator;
import org.eqasim.switzerland.mode_choice.SwissModeChoiceModule;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;

public class RunSimulation {
	static public void main(String[] args) throws ConfigurationException {
		CommandLine cmd = new CommandLine.Builder(args) //
				.requireOptions("config-path") //
				.allowPrefixes("av-mode-parameter", "mode-parameter", "cost-parameter") //
				.build();
		
		double a = 5.23;
		double b = Math.floor(a) + 1.0;
		double c = Math.floor(b) + 1.0;
		
		System.out.println(a + " " + b + " " + c);
		System.exit(1);

		Config config = ConfigUtils.loadConfig(cmd.getOptionStrict("config-path"), AstraConfigurator.getConfigGroups());
		AstraConfigurator.configure(config);
		cmd.applyConfiguration(config);
		AstraConfigurator.adjustOperator(config);

		Scenario scenario = ScenarioUtils.createScenario(config);

		SwitzerlandConfigurator.configureScenario(scenario);
		ScenarioUtils.loadScenario(scenario);
		SwitzerlandConfigurator.adjustScenario(scenario);
		AstraConfigurator.adjustScenario(scenario);

		EqasimConfigGroup eqasimConfig = EqasimConfigGroup.get(config);

		for (Link link : scenario.getNetwork().getLinks().values()) {
			double maximumSpeed = link.getFreespeed();
			boolean isMajor = true;

			for (Link other : link.getToNode().getInLinks().values()) {
				if (other.getCapacity() >= link.getCapacity()) {
					isMajor = false;
				}
			}

			if (!isMajor && link.getToNode().getInLinks().size() > 1) {
				double travelTime = link.getLength() / maximumSpeed;
				travelTime += eqasimConfig.getCrossingPenalty();
				link.setFreespeed(link.getLength() / travelTime);
			}
		}

		// EqasimLinkSpeedCalcilator deactivated!

		Controler controller = new Controler(scenario);
		SwitzerlandConfigurator.configureController(controller);
		controller.addOverridingModule(new EqasimAnalysisModule());
		controller.addOverridingModule(new EqasimModeChoiceModule());
		controller.addOverridingModule(new SwissModeChoiceModule(cmd));
		controller.addOverridingModule(new CalibrationModule());
		controller.addOverridingModule(new AstraModule(cmd));
		controller.addOverridingModule(new TravelTimeComparisonModule());
		controller.addOverridingModule(new ConvergenceModule());
		AstraConfigurator.configureController(controller, cmd);

		controller.addOverridingModule(new SmoothingTravelTimeModule());

		controller.run();
	}
}
