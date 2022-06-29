package org.eqasim.examples.SMMFramework.SMMBaseModeChoice;

import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.eqasim.core.components.config.EqasimConfigGroup;
import org.eqasim.core.simulation.mode_choice.AbstractEqasimExtension;
import org.eqasim.core.simulation.mode_choice.ParameterDefinition;
import org.eqasim.core.simulation.mode_choice.cost.CostModel;
import org.eqasim.core.simulation.mode_choice.parameters.ModeParameters;

import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.predictors.*;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.utilities.*;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.estimators.SMMBikeShareEstimator;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.predictors.SMMBikeSharePredictor;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMModeAvailability;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMBikeShareCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMCarCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.cost.SMMPTCostModel;
import org.eqasim.examples.SMMFramework.SMMBaseModeChoice.parameters.SMMCostParameters;
import org.eqasim.examples.corsica_drt.Drafts.DGeneralizedMultimodal.sharingPt.SharingPTModeChoiceModule;
import org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMParameters;
import org.eqasim.examples.corsica_drt.Drafts.otherDrafts.KraussBikeShareEstimator;
import org.eqasim.examples.corsica_drt.Drafts.otherDrafts.KraussBikeSharePredictor;
import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.CommandLine;
import org.matsim.core.config.CommandLine.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.geotools.feature.type.DateUtil.isEqual;

/**
 * Class overrides the SMM base mode choice ( no SMM modes)
 */
public class SMMBaseModeChoice extends AbstractEqasimExtension {
	private final CommandLine commandLine;
	public Scenario scenario;


	public SMMBaseModeChoice(CommandLine commandLine, Scenario scenario) {
		this.commandLine = commandLine;
		this.scenario = scenario;
	}

	@Override
	protected void installEqasimExtension() {
		//Bind Parameters
			bind(ModeParameters.class).to(SMMParameters.class);
		// Create Cost Parameters
		SMMCostParameters SMMCostParameters = null;

		//Bind Walk
		bindUtilityEstimator("KWalk").to( SMMWalkEstimator.class);
		bind(SMMWalkPredictor.class);


		//Bind bike
		bindUtilityEstimator("KBike").to( SMMBikeEstimator.class);
		bind(SMMBikePredictor.class);

		//Bind Car
		bindUtilityEstimator("KCar").to(SMMCarEstimator.class);
		bindCostModel("car").to(SMMCarCostModel.class);
		bind(SMMCarPredictor.class);

		//Bind PT
		bindUtilityEstimator("KPT").to(SMMPTEstimator.class);
		bindCostModel("pt").to(SMMPTCostModel.class);
		bind(SMMPTPredictor.class);
		bind(SMMPersonPredictor.class);
		bindModeAvailability("GENMODE").to(SMMModeAvailability.class);
	}

	@Provides
	@Singleton
	public SMMParameters provideModeChoiceParameters(EqasimConfigGroup config)
			throws IOException, ConfigurationException {
		SMMParameters parameters = SMMParameters.buildDefault();

		if (config.getModeParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getModeParametersPath()), parameters);
		}

		ParameterDefinition.applyCommandLine("mode-parameter", commandLine, parameters);
		return parameters;

	}

	public KraussBikeSharePredictor provideBikeSharePredictor(SMMBikeShareCostModel costModel, String name){
		KraussBikeSharePredictor temporalPredictor= new KraussBikeSharePredictor(costModel, name);
		return temporalPredictor;

	}

	public KraussBikeShareEstimator provideBikeShareEstimator(String name, SMMParameters modeParameters, KraussBikeSharePredictor predictor, SMMPersonPredictor personPredictor){
		KraussBikeShareEstimator temporalEstimator=new KraussBikeShareEstimator(modeParameters,predictor, personPredictor, name);
		return temporalEstimator;

	}


   public org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel provideBikeShareCostModel(EqasimConfigGroup config, String name) throws Exception {
		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters costParameters= provideCostParameters(config);
	   org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel bikeShareCostModel= new org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel(name, costParameters);
	   return(bikeShareCostModel);
   }





	@Provides
	@Singleton
	public SMMBikeShareCostModel provideSharingCostModel(org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters) {
		return new SMMBikeShareCostModel(parameters);
	}


	@Provides
	@Singleton
	public org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters provideCostParameters(EqasimConfigGroup config) throws Exception {
		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters = org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters.buildDefault();

		if (config.getCostParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getCostParametersPath()), parameters);
		}

		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters.applyCommandLineMicromobility("cost-parameter",commandLine,parameters);
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


	public static void validateSharingCostParameters(org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameterDefinition) throws Exception {
		Set<String> sharingKMCosts= parameterDefinition.sharingMinCosts.keySet();
		Set<String> sharingBookingCosts=  parameterDefinition.sharingBookingCosts.keySet();
		boolean isEqual = isEqual(sharingBookingCosts,sharingKMCosts);
		if (isEqual==false) {
			throw new  IllegalArgumentException(" One of the sharing modes does not have booking or km cost");
		}
	}


//	@Provides
//	@Singleton
	public static org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters provideCostParameters(EqasimConfigGroup config, CommandLine commandLine) throws Exception {
		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters parameters = org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters.buildDefault();

		if (config.getCostParametersPath() != null) {
			ParameterDefinition.applyFile(new File(config.getCostParametersPath()), parameters);
		}

		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters.applyCommandLineMicromobility("cost-parameter",commandLine,parameters);
		return parameters;
	}
	private SMMBikeShareEstimator addSharingServiceToEqasim(EqasimConfigGroup config, CommandLine commandLine, String name) throws Exception {
		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters costParameters= org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.variables_parameters.SMMCostParameters.provideCostParameters(config,commandLine);
		org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel costModel=new org.eqasim.examples.SMMFramework.generalizedSMMModeChoice.generalizedSMMModeChoice.costModels.SMMBikeShareCostModel(name,costParameters);
		SMMBikeSharePredictor bikePredictor=new SMMBikeSharePredictor(costModel,name);
		SMMPersonPredictor personPredictor=new SMMPersonPredictor();
		SMMParameters modeParams= new SharingPTModeChoiceModule(commandLine,null).provideModeChoiceParameters(config);
		SMMBikeShareEstimator bikeEstimator=new SMMBikeShareEstimator(modeParams,bikePredictor,personPredictor,name);
		return bikeEstimator;
	}


}
