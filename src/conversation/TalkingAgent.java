package conversation;

import jade.core.Agent;

public class TalkingAgent extends Agent {

	private static final long serialVersionUID = 843772225041211988L;

	protected void setup() {
		addBehaviour(new AnswerQuestionsBehaviour());
	}
}
