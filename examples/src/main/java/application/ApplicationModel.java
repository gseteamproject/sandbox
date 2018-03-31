package application;

import bookTrading.BookTitle;
import requester_responder.Vocabulary;

public class ApplicationModel {
	public String bookTrading() {
		return ArgumentBuilder.agent("reader", bookTrading.BookBuyerAgent.class, BookTitle.LORD_OF_THE_RINGS)
				+ ArgumentBuilder.agent("noviceProgrammer", bookTrading.BookBuyerAgent.class, BookTitle.JAVA_TUTORIAL)
				+ ArgumentBuilder.agent("advancedProgrammer", bookTrading.BookBuyerAgent.class,
						BookTitle.JADE_PROGRAMMING_TUTORIAL)
				+ ArgumentBuilder.agent("seller1", bookTrading.BookSellerAgent.class)
				+ ArgumentBuilder.agent("seller2", bookTrading.BookSellerAgent.class);
	}

	public String employment() {
		return ArgumentBuilder.agent("engager", employment.EngagerAgent.class)
				+ ArgumentBuilder.agent("requester", employment.RequesterAgent.class);
	}

	public String party() {
		return ArgumentBuilder.agent("host", party.HostAgent.class);
	}

	public String yellowPages() {
		return ArgumentBuilder.agent("provider-1", yellowPages.DFRegisterAgent.class, "my-forecast")
				+ ArgumentBuilder.agent("searcher", yellowPages.DFSearchAgent.class)
				+ ArgumentBuilder.agent("subscriber", yellowPages.DFSubscribeAgent.class)
				+ ArgumentBuilder.agent("provider-2", yellowPages.DFRegisterAgent.class, "forecast-1");
	}

	public String contractNet() {
		return ArgumentBuilder.agent("r1", protocols.ContractNetResponderAgent.class)
				+ ArgumentBuilder.agent("r2", protocols.ContractNetResponderAgent.class)
				+ ArgumentBuilder.agent("i", protocols.ContractNetInitiatorAgent.class, "r1,r2");
	}

	public String broker() {
		return ArgumentBuilder.agent("r", protocols.FIPARequestResponderAgent.class)
				+ ArgumentBuilder.agent("b", protocols.BrokerAgent.class, "r")
				+ ArgumentBuilder.agent("i", protocols.FIPARequestInitiatorAgent.class, "b");
	}

	public String fipaRequest() {
		return ArgumentBuilder.agent("r1", protocols.FIPARequestResponderAgent.class)
				+ ArgumentBuilder.agent("r2", protocols.FIPARequestResponderAgent.class)
				+ ArgumentBuilder.agent("i", protocols.FIPARequestInitiatorAgent.class, "r1,r2");
	}

	public String timeServer() {
		return ArgumentBuilder.agent("server", timeServer.TimeServerAgent.class)
				+ ArgumentBuilder.agent("client", timeServer.TimeClientAgent.class);
	}

	public String thanksAgent() {
		return ArgumentBuilder.agent("thanksAgent", thanksAgent.ThanksAgent.class);
	}

	public String palleteRobotCommunication() {
		return ArgumentBuilder.agent("sourcePallete", palleteRobotCommunication.SimpleRobotAgent.class, "3")
				+ ArgumentBuilder.agent("simpleRobot", palleteRobotCommunication.SimpleRobotAgent.class);
	}

	public String knowledge() {
		return ArgumentBuilder.agent("kp", knowledge.processor.KnowledgeProcessorAgent.class)
				+ ArgumentBuilder.agent("p1", knowledge.producer.KnowledgeProducerAgent.class, "animal=cat,cat=Lussy")
				+ ArgumentBuilder.agent("c1", knowledge.consumer.KnowledgeConsumerAgent.class, "animal,cat")
				+ ArgumentBuilder.agent("c2", knowledge.consumer.KnowledgeConsumerAgent.class, "cat,animal");
	}

	public String transportline() {
		return ArgumentBuilder.agent("line", transportsystem.transportline.TransportLineAgent.class);
	}

	public String requestResponder() {
		return ArgumentBuilder.agent(Vocabulary.REQUESTER_AGENT_NAME, requester_responder.RequesterAgent.class)
				+ ArgumentBuilder.agent(Vocabulary.RESPONDER_AGENT_NAME, requester_responder.ResponderAgent.class);
	}
}
