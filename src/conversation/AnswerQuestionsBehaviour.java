package conversation;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class AnswerQuestionsBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 3401188230499296662L;

	@Override
	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			System.out.println("got question: " + msg_received.getContent());
		} else {
			block();
		}
	}
}
