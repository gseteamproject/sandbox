package knowledge.processor;

import java.util.HashMap;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
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

	synchronized public void notUnderStood(ACLMessage message) {
		addBehaviour(new NotUnderstoodBehaviour(message));
	}

	synchronized public void answerQuestion(ACLMessage message) {
		addBehaviour(new AnswerQuestionBehaviour(message));
	}

	synchronized public void storeFact(ACLMessage message) {
		addBehaviour(new StoreFactBehaviour(message));
	}
}
