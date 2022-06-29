package org.eqasim.examples.corsica_drt.Drafts.otherDrafts;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;
import org.eqasim.core.simulation.mode_choice.ParameterDefinition;
import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMBikeShareCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMCarCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMEScooterCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMPTCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.*;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters.SMMCostParameters;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters.SMMModeParameters;

import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class KraussConstantModeChoiceModule extends AbstractEqasimExtension {
	private final CommandLine commandLine;

	public static final String MODE_AVAILABILITY_NAME = "IDFModeAvailability";

	public static final String CAR_COST_MODEL_NAME = "IDFCarCostModel";
	public static final String PT_COST_MODEL_NAME = "IDFPtCostModel";

	public static final String CAR_ESTIMATOR_NAME = "IDFCarUtilityEstimator";
	public static final String BIKE_ESTIMATOR_NAME = "ProxyBikeUtilityEstimator";

	public KraussConstantModeChoiceModule(CommandLine commandLine) {
		this.commandLine = commandLine;
	}

	@Override
	protected void installEqasimExtension() {
		//Bind Parameters
		bind(ModeParameters.class).to(SMMModeParameters.class);

		//Bind Walk
		bindUtilityEstimator("KWalk").to( KraussConstantEstimator.class);
		bind(SMMWalkPredictor.class);


		//Bind bike
		bindUtilityEstimator("KBike").to( KraussConstantEstimator.class);
		bind(SMMBikePredictor.class);

		//Bind Car
		bindUtilityEstimator("KCar").to(KraussConstantEstimator.class);
		bindCostModel("car").to(SMMCarCostModel.class);
		bind(SMMCarPredictor.class);

		//Bind PT
		bindUtilityEstimator("KPT").to(KraussConstantEstimator.class);
		bindCostModel("pt").to(SMMPTCostModel.class);
		bind(SMMPTPredictor.class);

		//Bind BikeShare
		bindUtilityEstimator("sharing:bikeShare").to(KraussConstantEstimator.class);
		bindCostModel("sharing:bikeShare").to(SMMBikeShareCostModel.class);
		bind(KraussBikeSharePredictor.class);

		//Bind eScooter
		bindUtilityEstimator("sharing:eScooter").to(KraussConstantEstimator.class);
		bindCostModel("sharing:eScooter").to(SMMEScooterCostModel.class);
		bind(KraussEScooterPredictor.class);
		bindModeAvailability("KModeAvailability").to(KraussModeAvailability.class);


		bind(SMMPersonPredictor.class);
	}

	@Provides
	@Singleton
	public SMMModeParameters provideModeChoiceParameters(EqasimConfigGroup config)
			throws IOException, ConfigurationException {
		SMMModeParameters parameters = SMMModeParameters.buildDefault();

		if (config.getModeParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getModeParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("mode-choice-parameter", commandLine, parameters);
		return parameters;
	}




	@Provides
	@Singleton
	public SMMBikeShareCostModel provideSharingCostModel(org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters) {
		return new SMMBikeShareCostModel(parameters);
	}

	@Provides
	@Singleton
	public SMMCostParameters provideCostParameters(EqasimConfigGroup config) {
		SMMCostParameters parameters = SMMCostParameters.buildDefault();

		if (config.getCostParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getCostParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("cost-parameter", commandLine, parameters);
		return parameters;
	}

	@Provides
	@Singleton
	public SMMCarCostModel provideCarCostModel(org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters) {
		return new SMMCarCostModel(parameters);
	}


	@Provides
	@Singleton
	public SMMPTCostModel providePTCostModel(org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters) {
		return new SMMPTCostModel(parameters);
	}
	@Provides
	@Named("sharing:bikeShare")
	public CostModel provideCostModel(Map<String, Provider<CostModel>> factory, EqasimConfigGroup config) {
		return getCostModel(factory, config, "sharing:bikeShare");
	}
	@Provides
	@Named("sharing:eScooter")
	public CostModel provideEScooterCostModel(Map<String, Provider<CostModel>> factory, EqasimConfigGroup config) {
		return getCostModel(factory, config, "sharing:eScooter");
	}

	@Provides
	@Named("car")
	public CostModel provideKraussCostModel(Map<String, Provider<CostModel>> factory, EqasimConfigGroup config) {
		return getCostModel(factory, config, "car");
	}
	@Provides
	@Named("pt")
	public CostModel providePTCostModel(Map<String, Provider<CostModel>> factory, EqasimConfigGroup config) {
		return getCostModel(factory, config, "pt");
	}
}
