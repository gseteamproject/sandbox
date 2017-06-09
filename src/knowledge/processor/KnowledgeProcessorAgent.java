package knowledge.processor;

import java.util.HashMap;

import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;

public class KnowledgeProcessorAgent extends KnowledgeAgent {

	private static final long serialVersionUID = 843772225041211988L;

	public HashMap<String, String> knowledge = new HashMap<String, String>();

	@Override
	protected void initializeBehaviours() {
		addBehaviour(new ListenBehaviour());
	}

	@Override
	protected void initializeData() {
	}

	@Override
	protected ServiceDescription[] getAgentServiceDescriptions() {
		ServiceDescription agentServices[] = new ServiceDescription[1];
		agentServices[0] = new ServiceDescription();
		agentServices[0].setName(Knowledge.KNOWLEDGE_SERVICES);
		agentServices[0].setType(Knowledge.KNOWLEDGE_SERVICE_PROCESSING);
		return agentServices;
	}

	synchronized public void answerQuestion(ACLMessage message) {
		MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt = MessageTemplate.and(mt, MessageTemplate.MatchConversationId(message.getConversationId()));
		addBehaviour(new AnswerQuestionBehaviour(this, mt));
		message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		putBack(message);
	}

	synchronized public void storeFact(ACLMessage message) {
		MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		mt = MessageTemplate.and(mt, MessageTemplate.MatchConversationId(message.getConversationId()));
		addBehaviour(new StoreFactBehaviour(this, mt));
		message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		putBack(message);
	}
}
