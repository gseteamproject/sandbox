package knowledge;

import java.util.HashMap;

import jade.core.Agent;

public class KnowledgeProcessorAgent extends Agent {

	private static final long serialVersionUID = 843772225041211988L;

	public HashMap<String, String> knowledge = new HashMap<String, String>();

	protected void setup() {
		addBehaviour(new ListenBehaviour());
	}

	public void trace(String p_message) {
		System.out.println(getAID().getName() + ": " + p_message);
	}
}
