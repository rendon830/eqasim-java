package org.eqasim.jakarta.mode_choice.costs;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eqasim.core.simulation.mode_choice.cost.AbstractCostModel;
import org.eqasim.jakarta.mode_choice.parameters.JakartaCostParameters;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;

import com.google.inject.Inject;

import ch.ethz.matsim.discrete_mode_choice.model.DiscreteModeChoiceTrip;

public class JakartaMotorcycleCostModel extends AbstractCostModel {
	private final JakartaCostParameters costParameters;

	@Inject
	public JakartaMotorcycleCostModel(JakartaCostParameters costParameters) {
		super("motorcycle");

		this.costParameters = costParameters;
	}

	public double getkmLink(List<? extends PlanElement> elements) {
		double total_km = 0;
        Set<Id<Link>> link = new HashSet<>();
        link.add(Id.createLinkId(764355));
        link.add(Id.createLinkId(100559));
        link.add(Id.createLinkId(205335));
        link.add(Id.createLinkId(205336));
        link.add(Id.createLinkId(227817));
        link.add(Id.createLinkId(238727));
        link.add(Id.createLinkId(238728));
        link.add(Id.createLinkId(238729));
        link.add(Id.createLinkId(238730));
        link.add(Id.createLinkId(238731));
        link.add(Id.createLinkId(238732));
        link.add(Id.createLinkId(238733));
        link.add(Id.createLinkId(238734));
        link.add(Id.createLinkId(238735));
        link.add(Id.createLinkId(238736));
        link.add(Id.createLinkId(238737));
        link.add(Id.createLinkId(238738));
        link.add(Id.createLinkId(238739));
        link.add(Id.createLinkId(274978));
        link.add(Id.createLinkId(274985));
        link.add(Id.createLinkId(274986));
        link.add(Id.createLinkId(274987));
        link.add(Id.createLinkId(274988));
        link.add(Id.createLinkId(274989));
        link.add(Id.createLinkId(274990));
        link.add(Id.createLinkId(274991));
        link.add(Id.createLinkId(274992));
        link.add(Id.createLinkId(274993));
        link.add(Id.createLinkId(274994));
        link.add(Id.createLinkId(274995));
        link.add(Id.createLinkId(275002));
        link.add(Id.createLinkId(275005));
        link.add(Id.createLinkId(298520));
        link.add(Id.createLinkId(298521));
        link.add(Id.createLinkId(307742));
        link.add(Id.createLinkId(307751));
        link.add(Id.createLinkId(308054));
        link.add(Id.createLinkId(412006));
        link.add(Id.createLinkId(412007));
        link.add(Id.createLinkId(412008));
        link.add(Id.createLinkId(618830));
        link.add(Id.createLinkId(618831));
        link.add(Id.createLinkId(618832));
        link.add(Id.createLinkId(618924));
        link.add(Id.createLinkId(619029));
        link.add(Id.createLinkId(619035));
        link.add(Id.createLinkId(619036));
        link.add(Id.createLinkId(123000));
        link.add(Id.createLinkId(123001));
        link.add(Id.createLinkId(123002));
        link.add(Id.createLinkId(122999));
        link.add(Id.createLinkId(122998));
        link.add(Id.createLinkId(122997));
        link.add(Id.createLinkId(67368));
        link.add(Id.createLinkId(1090609));
        link.add(Id.createLinkId(13735));
        link.add(Id.createLinkId(13734));
        link.add(Id.createLinkId(13733));
        link.add(Id.createLinkId(13732));
        link.add(Id.createLinkId(13731));
        link.add(Id.createLinkId(13730));
        link.add(Id.createLinkId(1116158));
        link.add(Id.createLinkId(1116157));
        link.add(Id.createLinkId(123003));
        link.add(Id.createLinkId(123004));
        link.add(Id.createLinkId(123005));
        link.add(Id.createLinkId(923677));
        link.add(Id.createLinkId(923623));
        link.add(Id.createLinkId(923626));
        link.add(Id.createLinkId(923675));
        link.add(Id.createLinkId(923676));
        link.add(Id.createLinkId(955861));
        link.add(Id.createLinkId(955862));
        link.add(Id.createLinkId(955863));
        link.add(Id.createLinkId(140841));
        link.add(Id.createLinkId(955860));
        link.add(Id.createLinkId(140839));
        link.add(Id.createLinkId(140840));
        link.add(Id.createLinkId(140838));
        link.add(Id.createLinkId(140837));
        link.add(Id.createLinkId(140836));
        link.add(Id.createLinkId(1116155));
        link.add(Id.createLinkId(1116156));
        link.add(Id.createLinkId(1116152));
        link.add(Id.createLinkId(1116153));
        link.add(Id.createLinkId(1116154));
        link.add(Id.createLinkId(1116151));
        link.add(Id.createLinkId(1116150));
        link.add(Id.createLinkId(1116149));
        link.add(Id.createLinkId(1116148));
        link.add(Id.createLinkId(205331));
        link.add(Id.createLinkId(281882));
        link.add(Id.createLinkId(281881));
        link.add(Id.createLinkId(561705));
        link.add(Id.createLinkId(561698));
        link.add(Id.createLinkId(561699));
        link.add(Id.createLinkId(561700));
        link.add(Id.createLinkId(561701));
        link.add(Id.createLinkId(561702));
        link.add(Id.createLinkId(561703));
        link.add(Id.createLinkId(561704));
        link.add(Id.createLinkId(281880));
        link.add(Id.createLinkId(281886));
        link.add(Id.createLinkId(281887));
        link.add(Id.createLinkId(281888));
        link.add(Id.createLinkId(281889));
        link.add(Id.createLinkId(281890));
        link.add(Id.createLinkId(281891));
        link.add(Id.createLinkId(281892));
        link.add(Id.createLinkId(281893));
        link.add(Id.createLinkId(319439));
        link.add(Id.createLinkId(471363));
        link.add(Id.createLinkId(471364));
        link.add(Id.createLinkId(471365));
        link.add(Id.createLinkId(551534));
        link.add(Id.createLinkId(608389));
        link.add(Id.createLinkId(608390));
        link.add(Id.createLinkId(608391));
        link.add(Id.createLinkId(608392));
        link.add(Id.createLinkId(608393));
        link.add(Id.createLinkId(608394));
        link.add(Id.createLinkId(608395));
        link.add(Id.createLinkId(608396));
        link.add(Id.createLinkId(608397));
        link.add(Id.createLinkId(608398));
        link.add(Id.createLinkId(608399));
        link.add(Id.createLinkId(608400));
        link.add(Id.createLinkId(608401));
        link.add(Id.createLinkId(608402));
        link.add(Id.createLinkId(608403));
        link.add(Id.createLinkId(608404));
        link.add(Id.createLinkId(608405));
        link.add(Id.createLinkId(608406));
        link.add(Id.createLinkId(608407));
        link.add(Id.createLinkId(608408));
        link.add(Id.createLinkId(611149));
        link.add(Id.createLinkId(611150));
        link.add(Id.createLinkId(611151));
        link.add(Id.createLinkId(611152));
        link.add(Id.createLinkId(611153));
        link.add(Id.createLinkId(611154));
        link.add(Id.createLinkId(611155));
        link.add(Id.createLinkId(611156));
        link.add(Id.createLinkId(795149));
        link.add(Id.createLinkId(795150));
        link.add(Id.createLinkId(795151));
        link.add(Id.createLinkId(795152));
        link.add(Id.createLinkId(795153));
        link.add(Id.createLinkId(795154));
        link.add(Id.createLinkId(795155));
        link.add(Id.createLinkId(795156));
        link.add(Id.createLinkId(796481));
        link.add(Id.createLinkId(796482));
        link.add(Id.createLinkId(796483));
        link.add(Id.createLinkId(796484));
        link.add(Id.createLinkId(796485));
        link.add(Id.createLinkId(796486));
        link.add(Id.createLinkId(796487));
        link.add(Id.createLinkId(796674));
        link.add(Id.createLinkId(796675));
        link.add(Id.createLinkId(796676));
        link.add(Id.createLinkId(796678));
        link.add(Id.createLinkId(796679));
        link.add(Id.createLinkId(796680));
        link.add(Id.createLinkId(796681));
        link.add(Id.createLinkId(796682));
        link.add(Id.createLinkId(796683));
        link.add(Id.createLinkId(796684));
        link.add(Id.createLinkId(796685));
        link.add(Id.createLinkId(796686));
        link.add(Id.createLinkId(796687));
        link.add(Id.createLinkId(796688));
        link.add(Id.createLinkId(796689));
        link.add(Id.createLinkId(796690));
        link.add(Id.createLinkId(796691));
        link.add(Id.createLinkId(914482));
        link.add(Id.createLinkId(1206667));
        link.add(Id.createLinkId(796677));
        link.add(Id.createLinkId(199490));
        link.add(Id.createLinkId(199491));
        link.add(Id.createLinkId(113011));
        link.add(Id.createLinkId(113012));
        link.add(Id.createLinkId(113013));
        link.add(Id.createLinkId(113014));
        link.add(Id.createLinkId(113015));
        link.add(Id.createLinkId(199490));
        link.add(Id.createLinkId(199491));
        link.add(Id.createLinkId(113016));
        link.add(Id.createLinkId(113017));
        link.add(Id.createLinkId(113018));
        link.add(Id.createLinkId(113019));
        link.add(Id.createLinkId(113020));
        link.add(Id.createLinkId(113021));
        link.add(Id.createLinkId(113022));
        link.add(Id.createLinkId(602435));
        link.add(Id.createLinkId(602436));
        link.add(Id.createLinkId(602437));
        link.add(Id.createLinkId(602438));
        link.add(Id.createLinkId(602439));
        link.add(Id.createLinkId(959181));
        link.add(Id.createLinkId(959182));
        link.add(Id.createLinkId(959180));
        link.add(Id.createLinkId(808129));
        link.add(Id.createLinkId(1206640));
        link.add(Id.createLinkId(1206639));
        link.add(Id.createLinkId(300920));
        link.add(Id.createLinkId(300919));
        link.add(Id.createLinkId(808070));
        link.add(Id.createLinkId(48641));
        link.add(Id.createLinkId(808132));
        link.add(Id.createLinkId(808311));
        link.add(Id.createLinkId(238759));
        link.add(Id.createLinkId(238580));
        link.add(Id.createLinkId(238579));
        link.add(Id.createLinkId(238578));
        link.add(Id.createLinkId(808314));
        link.add(Id.createLinkId(808105));
        link.add(Id.createLinkId(1206641));
        link.add(Id.createLinkId(48638));
        link.add(Id.createLinkId(307749));
        link.add(Id.createLinkId(1206641));
        link.add(Id.createLinkId(1206642));
        link.add(Id.createLinkId(48633));
        link.add(Id.createLinkId(808293));
        link.add(Id.createLinkId(808067));
        link.add(Id.createLinkId(808294));
        link.add(Id.createLinkId(808311));
        link.add(Id.createLinkId(1206666));
        link.add(Id.createLinkId(1206665));
        link.add(Id.createLinkId(481621));
        link.add(Id.createLinkId(481622));
        link.add(Id.createLinkId(244819));
        link.add(Id.createLinkId(281754));
        link.add(Id.createLinkId(281755));
        link.add(Id.createLinkId(281756));
        link.add(Id.createLinkId(281757));
        link.add(Id.createLinkId(281758));
        link.add(Id.createLinkId(281759));
        link.add(Id.createLinkId(281760));
        link.add(Id.createLinkId(281761));
        link.add(Id.createLinkId(281762));
        link.add(Id.createLinkId(454994));
        link.add(Id.createLinkId(454995));
        link.add(Id.createLinkId(149870));
        link.add(Id.createLinkId(454981));
        link.add(Id.createLinkId(454993));
        link.add(Id.createLinkId(149871));
        link.add(Id.createLinkId(63741));
        link.add(Id.createLinkId(63742));
        link.add(Id.createLinkId(63743));
        link.add(Id.createLinkId(63744));
        link.add(Id.createLinkId(63745));
        link.add(Id.createLinkId(63746));
        link.add(Id.createLinkId(63747));
        link.add(Id.createLinkId(63748));
        link.add(Id.createLinkId(63749));
        link.add(Id.createLinkId(455000));
        link.add(Id.createLinkId(454998));
        link.add(Id.createLinkId(63750));
        link.add(Id.createLinkId(281952));
        link.add(Id.createLinkId(281951));
        link.add(Id.createLinkId(281950));
        link.add(Id.createLinkId(281949));
        link.add(Id.createLinkId(454982));
        link.add(Id.createLinkId(454979));
        link.add(Id.createLinkId(454875));
        link.add(Id.createLinkId(454980));
        link.add(Id.createLinkId(454978));
        link.add(Id.createLinkId(454999));
        link.add(Id.createLinkId(454997));
        link.add(Id.createLinkId(1150756));
        link.add(Id.createLinkId(1150757));
        link.add(Id.createLinkId(1150759));
        link.add(Id.createLinkId(1150758));
        link.add(Id.createLinkId(1150759));
        link.add(Id.createLinkId(1150755));
        link.add(Id.createLinkId(1150754));
        link.add(Id.createLinkId(1150753));
        link.add(Id.createLinkId(1150752));
        link.add(Id.createLinkId(1150766));
        link.add(Id.createLinkId(493582));
        link.add(Id.createLinkId(199410));
        link.add(Id.createLinkId(283956));
        link.add(Id.createLinkId(283955));
        link.add(Id.createLinkId(227369));
        link.add(Id.createLinkId(227368));
        link.add(Id.createLinkId(227367));
        link.add(Id.createLinkId(227366));
        link.add(Id.createLinkId(199348));
        link.add(Id.createLinkId(199364));
        link.add(Id.createLinkId(199363));
        link.add(Id.createLinkId(199362));
        link.add(Id.createLinkId(199361));
        link.add(Id.createLinkId(199360));
        link.add(Id.createLinkId(199359));
        link.add(Id.createLinkId(199358));
        link.add(Id.createLinkId(199357));
        link.add(Id.createLinkId(199356));
        link.add(Id.createLinkId(199355));
        link.add(Id.createLinkId(199354));
        link.add(Id.createLinkId(199353));
        link.add(Id.createLinkId(199352));
        link.add(Id.createLinkId(199351));
        link.add(Id.createLinkId(199350));
        link.add(Id.createLinkId(199349));
        link.add(Id.createLinkId(199403));
        link.add(Id.createLinkId(199402));
        link.add(Id.createLinkId(199401));
        link.add(Id.createLinkId(199400));
        link.add(Id.createLinkId(199399));
        link.add(Id.createLinkId(199398));
        link.add(Id.createLinkId(199397));
        link.add(Id.createLinkId(199391));
        link.add(Id.createLinkId(199389));
        link.add(Id.createLinkId(199390));
        link.add(Id.createLinkId(199395));
        link.add(Id.createLinkId(281945));
        link.add(Id.createLinkId(199394));
        link.add(Id.createLinkId(199393));
        link.add(Id.createLinkId(199392));
        link.add(Id.createLinkId(281946));
        link.add(Id.createLinkId(284185));
        link.add(Id.createLinkId(281944));
        link.add(Id.createLinkId(281943));
        link.add(Id.createLinkId(988501));
        link.add(Id.createLinkId(988502));
        link.add(Id.createLinkId(988503));
        link.add(Id.createLinkId(988504));
        link.add(Id.createLinkId(988505));
        link.add(Id.createLinkId(988506));
        link.add(Id.createLinkId(988515));
        link.add(Id.createLinkId(988520));
        link.add(Id.createLinkId(988521));
        link.add(Id.createLinkId(988522));
        link.add(Id.createLinkId(988523));
        link.add(Id.createLinkId(988524));
        link.add(Id.createLinkId(990464));
        link.add(Id.createLinkId(1023925));
        link.add(Id.createLinkId(1048718));
        link.add(Id.createLinkId(1048719));
        link.add(Id.createLinkId(1048720));
        link.add(Id.createLinkId(1048721));
        link.add(Id.createLinkId(1048722));
        link.add(Id.createLinkId(1048723));
        link.add(Id.createLinkId(1048724));
        link.add(Id.createLinkId(1048725));
        link.add(Id.createLinkId(1048726));
        link.add(Id.createLinkId(1048727));
        link.add(Id.createLinkId(1048728));
        link.add(Id.createLinkId(1048729));
        link.add(Id.createLinkId(1048730));
        link.add(Id.createLinkId(1048731));
        link.add(Id.createLinkId(1048732));
        link.add(Id.createLinkId(1048741));
        link.add(Id.createLinkId(1049082));
        link.add(Id.createLinkId(1049083));
        link.add(Id.createLinkId(1049084));
        link.add(Id.createLinkId(1049085));
        link.add(Id.createLinkId(1049086));
        link.add(Id.createLinkId(1123213));
        link.add(Id.createLinkId(281861));
        link.add(Id.createLinkId(281810));
        link.add(Id.createLinkId(284438));
        link.add(Id.createLinkId(281861));
        link.add(Id.createLinkId(1120375));
        link.add(Id.createLinkId(1120374));
        link.add(Id.createLinkId(1120538));
        link.add(Id.createLinkId(1120539));
        link.add(Id.createLinkId(1120571));
        link.add(Id.createLinkId(281810));
        link.add(Id.createLinkId(1120572));
        link.add(Id.createLinkId(1120573));
        link.add(Id.createLinkId(1120574));
        link.add(Id.createLinkId(1120575));
        link.add(Id.createLinkId(1120576));
        link.add(Id.createLinkId(1120577));
        link.add(Id.createLinkId(1120578));
        link.add(Id.createLinkId(1120579));
        link.add(Id.createLinkId(1120580));
        link.add(Id.createLinkId(1120581));
        link.add(Id.createLinkId(1120582));
        link.add(Id.createLinkId(284071));
        link.add(Id.createLinkId(284072));
        link.add(Id.createLinkId(1113413));
        link.add(Id.createLinkId(284080));
        link.add(Id.createLinkId(1113412));
        link.add(Id.createLinkId(284080));
        link.add(Id.createLinkId(284079));
        link.add(Id.createLinkId(284071));
        link.add(Id.createLinkId(284072));
        link.add(Id.createLinkId(988512));
        link.add(Id.createLinkId(988511));
        link.add(Id.createLinkId(1120604));
        link.add(Id.createLinkId(1120605));
        link.add(Id.createLinkId(1120606));
        link.add(Id.createLinkId(1120607));
        link.add(Id.createLinkId(1120608));
        link.add(Id.createLinkId(1120609));
        link.add(Id.createLinkId(1120643));
        link.add(Id.createLinkId(1120644));
        link.add(Id.createLinkId(1120645));
        link.add(Id.createLinkId(1120646));
        link.add(Id.createLinkId(1120647));
        link.add(Id.createLinkId(1120648));
        link.add(Id.createLinkId(1120673));
        link.add(Id.createLinkId(1120676));
        link.add(Id.createLinkId(1120679));
        link.add(Id.createLinkId(284435));
        link.add(Id.createLinkId(988513));
        link.add(Id.createLinkId(284436));
        link.add(Id.createLinkId(284437));
        link.add(Id.createLinkId(988514));
        link.add(Id.createLinkId(282041));
        link.add(Id.createLinkId(1016183));
        link.add(Id.createLinkId(282042));
        link.add(Id.createLinkId(281801));
        link.add(Id.createLinkId(281802));
        link.add(Id.createLinkId(281803));
        link.add(Id.createLinkId(282027));
        link.add(Id.createLinkId(282028));
        link.add(Id.createLinkId(282035));
        link.add(Id.createLinkId(282036));
        link.add(Id.createLinkId(282037));
        link.add(Id.createLinkId(282038));
        link.add(Id.createLinkId(282039));
        link.add(Id.createLinkId(282040));
        link.add(Id.createLinkId(283947));
        link.add(Id.createLinkId(283948));
        link.add(Id.createLinkId(284350));
        link.add(Id.createLinkId(284351));
        link.add(Id.createLinkId(284352));
        link.add(Id.createLinkId(284354));
        link.add(Id.createLinkId(284355));
        link.add(Id.createLinkId(284356));
        link.add(Id.createLinkId(284357));
        link.add(Id.createLinkId(284358));
        link.add(Id.createLinkId(284359));
        link.add(Id.createLinkId(284360));
        link.add(Id.createLinkId(284361));
        link.add(Id.createLinkId(284362));
        link.add(Id.createLinkId(284363));
        link.add(Id.createLinkId(284388));
        link.add(Id.createLinkId(284389));
        link.add(Id.createLinkId(284390));
        link.add(Id.createLinkId(284442));
        link.add(Id.createLinkId(284443));
        link.add(Id.createLinkId(284444));
        link.add(Id.createLinkId(561523));
        link.add(Id.createLinkId(561538));
        link.add(Id.createLinkId(672776));
        link.add(Id.createLinkId(988509));
        link.add(Id.createLinkId(988510));
        link.add(Id.createLinkId(100559));
        link.add(Id.createLinkId(100560));
        link.add(Id.createLinkId(100667));
        link.add(Id.createLinkId(100672));
        link.add(Id.createLinkId(100673));
        link.add(Id.createLinkId(100680));
        link.add(Id.createLinkId(102023));
        link.add(Id.createLinkId(102024));
        link.add(Id.createLinkId(102033));
        link.add(Id.createLinkId(102034));
        link.add(Id.createLinkId(199411));
        link.add(Id.createLinkId(199412));
        link.add(Id.createLinkId(199415));
        link.add(Id.createLinkId(281771));
        link.add(Id.createLinkId(281873));
        link.add(Id.createLinkId(281874));
        link.add(Id.createLinkId(282099));
        link.add(Id.createLinkId(284353));
        link.add(Id.createLinkId(762679));
        link.add(Id.createLinkId(762680));
        link.add(Id.createLinkId(762724));
        link.add(Id.createLinkId(764355));
        link.add(Id.createLinkId(764356));
        link.add(Id.createLinkId(788188));
        link.add(Id.createLinkId(788189));
        link.add(Id.createLinkId(868225));
        link.add(Id.createLinkId(199429));
        link.add(Id.createLinkId(199404));
        link.add(Id.createLinkId(199405));
        link.add(Id.createLinkId(199406));
        link.add(Id.createLinkId(199407));
        link.add(Id.createLinkId(199430));
        link.add(Id.createLinkId(199413));
        link.add(Id.createLinkId(956770));
        link.add(Id.createLinkId(956776));
        link.add(Id.createLinkId(956777));
        link.add(Id.createLinkId(199417));
        link.add(Id.createLinkId(199418));
        link.add(Id.createLinkId(199419));
        link.add(Id.createLinkId(199420));
        link.add(Id.createLinkId(199421));
        link.add(Id.createLinkId(199422));
        link.add(Id.createLinkId(199423));
        link.add(Id.createLinkId(381374));
        link.add(Id.createLinkId(454867));
        link.add(Id.createLinkId(454868));
        link.add(Id.createLinkId(454869));
        link.add(Id.createLinkId(454870));
        link.add(Id.createLinkId(454996));
        link.add(Id.createLinkId(455073));
        link.add(Id.createLinkId(455074));
        link.add(Id.createLinkId(455077));
        link.add(Id.createLinkId(199414));
        link.add(Id.createLinkId(281776));
        link.add(Id.createLinkId(199424));
        link.add(Id.createLinkId(199425));
        link.add(Id.createLinkId(455001));
        link.add(Id.createLinkId(455002));
        link.add(Id.createLinkId(199416));
        link.add(Id.createLinkId(381373));
        link.add(Id.createLinkId(284188));	
		for (PlanElement element : elements) {
			if (element instanceof Leg) {
				Leg leg = (Leg) element;
				double departureTime = leg.getDepartureTime();
				if (link.contains(leg.getRoute().getStartLinkId()) && ((departureTime> 7 *3600 && departureTime < 10* 3600)
						|| (departureTime < 19* 3600 && departureTime > 16 *3600))) {
					total_km = total_km + leg.getRoute().getDistance() * 1e-3  ;
				}
				//System.out.println(leg.getRoute().getStartLinkId());
			}
		}
		return total_km;
	}
	
	
	@Override
	public double calculateCost_MU(Person person, DiscreteModeChoiceTrip trip, List<? extends PlanElement> elements) {
		return costParameters.motorcycleCost_KIDR_km * getInVehicleDistance_km(elements) + costParameters.mcCharging_KIDR_km * getkmLink(elements);
	}
}