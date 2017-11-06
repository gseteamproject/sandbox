package requester_responder;

import jade.Boot;

public class RunScenario {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = Vocabulary.REQUESTER_AGENT_NAME + ":" + requester_responder.RequesterAgent.class.getName()
				+ ";";
		parameters[1] += Vocabulary.RESPONDER_AGENT_NAME + ":" + requester_responder.ResponderAgent.class.getName()
				+ ";";
		Boot.main(parameters);
	}
}
