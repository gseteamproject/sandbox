package application;

import bookTrading.BookTitle;

public class ApplicationModel {
	public String bookTrading() {
		String scenario = new String();
		scenario = "reader:bookTrading.BookBuyerAgent(" + BookTitle.LORD_OF_THE_RINGS + ");";
		scenario += "noviceProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JAVA_TUTORIAL + ");";
		scenario += "advancedProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JADE_PROGRAMMING_TUTORIAL + ");";
		scenario += "seller1:bookTrading.BookSellerAgent;";
		scenario += "seller2:bookTrading.BookSellerAgent;";
		return scenario;
	}

	public String employment() {
		String scenario = new String();		
		scenario = "engager:employment.EngagerAgent;";
		scenario += "requester:employment.RequesterAgent;";
		return scenario;
	}

	public String party() {
		String scenario = new String();
		scenario = "host:party.HostAgent;";
		return scenario;
	}

	public String yellowPages() {
		String scenario = new String();
		scenario = "provider-1:yellowPages.DFRegisterAgent(my-forecast);";
		scenario += "searcher:yellowPages.DFSearchAgent;";
		scenario += "subscriber:yellowPages.DFSubscribeAgent;";
		scenario += "provider-2:yellowPages.DFRegisterAgent(forecast-1);";
		return scenario;
	}

	public String contractNet() {
		String scenario = new String();
		scenario = "r1:protocols.ContractNetResponderAgent;";
		scenario += "r2:protocols.ContractNetResponderAgent;";
		scenario += "i:protocols.ContractNetInitiatorAgent(r1,r2);";
		return scenario;
	}

	public String broker() {
		String scenario = new String();
		scenario = "r:protocols.FIPARequestResponderAgent;";
		scenario += "b:protocols.BrokerAgent(r);";
		scenario += "i:protocols.FIPARequestInitiatorAgent(b)";
		return scenario;
	}

	public String fipaRequest() {
		String scenario = new String();
		scenario = "r1:protocols.FIPARequestResponderAgent;";
		scenario += "r2:protocols.FIPARequestResponderAgent;";
		scenario += "i:protocols.FIPARequestInitiatorAgent(r1,r2);";
		return scenario;
	}

	public String timeServer() {
		String scenario = new String();
		scenario = "server:ontology.ontologyServer.TimeServerAgent;";
		scenario += "client:ontology.ontologyServer.TimeClientAgent;";
		return scenario;
	}

	public String thanksAgent() {
		String scenario = new String();
		scenario = "thanksAgent:thanksAgent.ThanksAgent;";
		return scenario;
	}

	public String palleteRobotCommunication() {
		String scenario = new String();
		scenario = "sourcePallete:palleteRobotCommunication.SourcePalleteAgent(3);";
		scenario += "simpleRobot:palleteRobotCommunication.SimpleRobotAgent;";
		return scenario;
	}

	public String conversation() {
		String scenario = new String();
		scenario = "speaker:conversation.TalkingAgent;";
		return scenario;
	}

	public String transportline() {
		String scenario = new String();
		scenario = "line1:transportsystem.TransportLineAgent;";
		return scenario;
	}
}
