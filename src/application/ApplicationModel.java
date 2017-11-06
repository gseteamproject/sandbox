package application;

import bookTrading.BookTitle;
import requester_responder.Vocabulary;

public class ApplicationModel {
	public String bookTrading() {
		return addAgent("reader", bookTrading.BookBuyerAgent.class, BookTitle.LORD_OF_THE_RINGS)
				+ addAgent("noviceProgrammer", bookTrading.BookBuyerAgent.class, BookTitle.JAVA_TUTORIAL)
				+ addAgent("advancedProgrammer", bookTrading.BookBuyerAgent.class, BookTitle.JADE_PROGRAMMING_TUTORIAL)
				+ addAgent("seller1", bookTrading.BookSellerAgent.class)
				+ addAgent("seller2", bookTrading.BookSellerAgent.class);
	}

	public String employment() {
		return addAgent("engager", employment.EngagerAgent.class)
				+ addAgent("requester", employment.RequesterAgent.class);
	}

	public String party() {
		return addAgent("host", party.HostAgent.class);
	}

	public String yellowPages() {
		return addAgent("provider-1", yellowPages.DFRegisterAgent.class, "my-forecast")
				+ addAgent("searcher", yellowPages.DFSearchAgent.class)
				+ addAgent("subscriber", yellowPages.DFSubscribeAgent.class)
				+ addAgent("provider-2", yellowPages.DFRegisterAgent.class, "forecast-1");
	}

	public String contractNet() {
		return addAgent("r1", protocols.ContractNetResponderAgent.class)
				+ addAgent("r2", protocols.ContractNetResponderAgent.class)
				+ addAgent("i", protocols.ContractNetInitiatorAgent.class, "r1,r2");
	}

	public String broker() {
		return addAgent("r", protocols.FIPARequestResponderAgent.class)
				+ addAgent("b", protocols.BrokerAgent.class, "r")
				+ addAgent("i", protocols.FIPARequestInitiatorAgent.class, "b");
	}

	public String fipaRequest() {
		return addAgent("r1", protocols.FIPARequestResponderAgent.class)
				+ addAgent("r2", protocols.FIPARequestResponderAgent.class)
				+ addAgent("i", protocols.FIPARequestInitiatorAgent.class, "r1,r2");
	}

	public String timeServer() {
		return addAgent("server", timeServer.TimeServerAgent.class)
				+ addAgent("client", timeServer.TimeClientAgent.class);
	}

	public String thanksAgent() {
		return addAgent("thanksAgent", thanksAgent.ThanksAgent.class);
	}

	public String palleteRobotCommunication() {
		return addAgent("sourcePallete", palleteRobotCommunication.SimpleRobotAgent.class, "3")
				+ addAgent("simpleRobot", palleteRobotCommunication.SimpleRobotAgent.class);
	}

	public String knowledge() {
		return addAgent("kp", knowledge.processor.KnowledgeProcessorAgent.class)
				+ addAgent("p1", knowledge.producer.KnowledgeProducerAgent.class, "animal=cat,cat=Lussy")
				+ addAgent("c1", knowledge.consumer.KnowledgeConsumerAgent.class, "animal,cat")
				+ addAgent("c2", knowledge.consumer.KnowledgeConsumerAgent.class, "cat,animal");
	}

	public String transportline() {
		return addAgent("line", transportsystem.transportline.TransportLineAgent.class);
	}

	public String requestResponder() {
		return addAgent(Vocabulary.REQUESTER_AGENT_NAME, requester_responder.RequesterAgent.class)
				+ addAgent(Vocabulary.RESPONDER_AGENT_NAME, requester_responder.ResponderAgent.class);
	}

	private String addAgent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}

	private String addAgent(String agentName, Class<?> agentClass, String parameters) {
		return agentName + ":" + agentClass.getName() + "(" + parameters + ");";
	}
}
