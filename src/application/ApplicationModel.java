package application;

import bookTrading.BookTitle;

public class ApplicationModel {
	public String bookTrading() {
		String scenario = new String();
		scenario = addAgent("reader", bookTrading.BookBuyerAgent.class, BookTitle.LORD_OF_THE_RINGS);
		scenario += addAgent("noviceProgrammer", bookTrading.BookBuyerAgent.class, BookTitle.JAVA_TUTORIAL);
		scenario += addAgent("advancedProgrammer", bookTrading.BookBuyerAgent.class, BookTitle.JADE_PROGRAMMING_TUTORIAL);
		scenario += addAgent("seller1", bookTrading.BookSellerAgent.class);
		scenario += addAgent("seller2", bookTrading.BookSellerAgent.class);
		return scenario;
	}

	public String employment() {
		String scenario = new String();
		scenario = addAgent("engager", employment.EngagerAgent.class);
		scenario += addAgent("requester", employment.RequesterAgent.class);
		return scenario;
	}

	public String party() {
		String scenario = new String();
		scenario = addAgent("host", party.HostAgent.class);
		return scenario;
	}

	public String yellowPages() {
		String scenario = new String();
		scenario = addAgent("provider-1", yellowPages.DFRegisterAgent.class, "my-forecast");
		scenario += addAgent("searcher", yellowPages.DFSearchAgent.class);
		scenario += addAgent("subscriber", yellowPages.DFSubscribeAgent.class);
		scenario += addAgent("provider-2", yellowPages.DFRegisterAgent.class, "forecast-1");
		return scenario;
	}

	public String contractNet() {
		String scenario = new String();
		scenario = addAgent("r1", protocols.ContractNetResponderAgent.class);
		scenario += addAgent("r2", protocols.ContractNetResponderAgent.class);
		scenario += addAgent("i", protocols.ContractNetInitiatorAgent.class, "r1,r2");
		return scenario;
	}

	public String broker() {
		String scenario = new String();
		scenario = addAgent("r", protocols.FIPARequestResponderAgent.class);
		scenario += addAgent("b", protocols.BrokerAgent.class, "r");
		scenario += addAgent("i", protocols.FIPARequestInitiatorAgent.class, "b");
		return scenario;
	}

	public String fipaRequest() {
		String scenario = new String();
		scenario = addAgent("r1", protocols.FIPARequestResponderAgent.class);
		scenario += addAgent("r2", protocols.FIPARequestResponderAgent.class);
		scenario += addAgent("i", protocols.FIPARequestInitiatorAgent.class, "r1,r2");
		return scenario;
	}

	public String timeServer() {
		String scenario = new String();
		scenario = addAgent("server", timeServer.TimeServerAgent.class);
		scenario += addAgent("client", timeServer.TimeClientAgent.class);
		return scenario;
	}

	public String thanksAgent() {
		String scenario = new String();
		scenario = addAgent("thanksAgent", thanksAgent.ThanksAgent.class);
		return scenario;
	}

	public String palleteRobotCommunication() {
		String scenario = new String();
		scenario = addAgent("sourcePallete", palleteRobotCommunication.SimpleRobotAgent.class, "3");
		scenario += addAgent("simpleRobot", palleteRobotCommunication.SimpleRobotAgent.class);
		return scenario;
	}

	public String knowledge() {
		String scenario = new String();
		scenario = addAgent("kp", knowledge.processor.KnowledgeProcessorAgent.class);
		scenario += addAgent("p1", knowledge.producer.KnowledgeProducerAgent.class, "animal=cat,cat=Lussy");
		scenario += addAgent("c1", knowledge.consumer.KnowledgeConsumerAgent.class, "animal,cat");
		scenario += addAgent("c2", knowledge.consumer.KnowledgeConsumerAgent.class, "cat,animal");
		return scenario;
	}

	public String transportline() {
		String scenario = new String();
		scenario = addAgent("line", transportsystem.transportline.TransportLineAgent.class);
		return scenario;
	}

	private String addAgent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}

	private String addAgent(String agentName, Class<?> agentClass, String parameters) {
		return agentName + ":" + agentClass.getName() + "(" + parameters + ");";
	}
}
