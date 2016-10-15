package application;

import bookTrading.BookTitle;
import jade.Boot;

public class Application {

	public static void main(String[] p_args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";

		int scenarioId = 3;
		switch (scenarioId) {
		case 1:
			parameters[1] = bookTradingScenario();
			break;
		case 2:
			parameters[1] = employmentScenario();
			break;
		case 3:
			parameters[1] = partyScenario();
			break;
		case 4:
			parameters[1] = yellowPagesScenario();
			break;
		case 5:
			parameters[1] = contractNetScenario();
			break;
		case 6:
			parameters[1] = brokerExample();
			break;
		case 7:
			parameters[1] = fipaRequestExample();
			break;
		case 8:
			parameters[1] = timeServer();
			break;
		case 9:
			parameters[1] = thanksAgent();
			break;
		case 10:
			parameters[1] = palleteRobotCommunication();
			break;
		default:
			parameters[1] = "";
			break;
		}

		Boot.main(parameters);
	}

	public static String bookTradingScenario() {
		String parameters = new String();
		parameters = "reader:bookTrading.BookBuyerAgent(" + BookTitle.LORD_OF_THE_RINGS + ");";
		parameters += "noviceProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JAVA_TUTORIAL + ");";
		parameters += "advancedProgrammer:bookTrading.BookBuyerAgent(" + BookTitle.JADE_PROGRAMMING_TUTORIAL + ");";
		parameters += "seller1:bookTrading.BookSellerAgent;";
		parameters += "seller2:bookTrading.BookSellerAgent;";
		return parameters;
	}

	public static String employmentScenario() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "";
		parameters += "engager:employment.EngagerAgent;";
		parameters += "requester:employment.RequesterAgent;";
		return parameters;
	}

	public static String partyScenario() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "host:party.HostAgent;";
		return parameters;
	}

	public static String yellowPagesScenario() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "provider-1:yellowPages.DFRegisterAgent(my-forecast);";
		parameters += "searcher:yellowPages.DFSearchAgent;";
		parameters += "subscriber:yellowPages.DFSubscribeAgent;";
		parameters += "provider-2:yellowPages.DFRegisterAgent(forecast-1);";
		return parameters;
	}

	public static String contractNetScenario() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "r1:protocols.ContractNetResponderAgent;";
		parameters += "r2:protocols.ContractNetResponderAgent;";
		parameters += "i:protocols.ContractNetInitiatorAgent(r1,r2);";
		return parameters;
	}

	public static String brokerExample() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "r:protocols.FIPARequestResponderAgent;";
		parameters += "b:protocols.BrokerAgent(r);";
		parameters += "i:protocols.FIPARequestInitiatorAgent(b)";
		return parameters;
	}

	public static String fipaRequestExample() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "r1:protocols.FIPARequestResponderAgent;";
		parameters += "r2:protocols.FIPARequestResponderAgent;";
		parameters += "i:protocols.FIPARequestInitiatorAgent(r1,r2);";
		return parameters;
	}

	public static String timeServer() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "";
		parameters += "server:ontology.ontologyServer.TimeServerAgent;";
		parameters += "client:ontology.ontologyServer.TimeClientAgent;";
		return parameters;
	}

	public static String thanksAgent() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "thanksAgent:thanksAgent.ThanksAgent;";
		return parameters;
	}

	public static String palleteRobotCommunication() {
		String parameters = new String();
		parameters = "-gui";
		parameters = "sourcePallete:palleteRobotCommunication.SourcePallete(3);";
		parameters += "simpleRobot:palleteRobotCommunication.SimpleRobot;";
		return parameters;
	}
}
