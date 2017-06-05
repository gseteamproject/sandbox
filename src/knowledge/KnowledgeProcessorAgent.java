package knowledge;

import java.util.HashMap;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

public class KnowledgeProcessorAgent extends Agent {

	private static final long serialVersionUID = 843772225041211988L;

	public HashMap<String, String> knowledge = new HashMap<String, String>();

	protected void setup() {
		addBehaviour(new ListenBehaviour());
	}

	synchronized public void notUnderStood(ACLMessage message) {
		addBehaviour(new NotUnderstoodBehaviour(message));
	}

	public void trace(String p_message) {
		System.out.println(getAID().getName() + ": " + p_message);
	}

	synchronized public void answerQuestion(ACLMessage message) {
		addBehaviour(new AnswerQuestionBehaviour(message));
	}

	synchronized public void storeFact(ACLMessage message) {
		addBehaviour(new StoreFactBehaviour(message));
	}
}
