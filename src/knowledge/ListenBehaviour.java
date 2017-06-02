package knowledge;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 3401188230499296662L;

	private KnowledgeProcessorAgent myKnowledgeProcessorAgent;

	@Override
	public void onStart() {
		super.onStart();
		myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
	}

	@Override
	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			int performative = msg_received.getPerformative();

			switch (performative) {
			case ACLMessage.INFORM:
				myKnowledgeProcessorAgent.addBehaviour(new StoreFactBehaviour(msg_received));
				break;

			case ACLMessage.REQUEST:
				myKnowledgeProcessorAgent.addBehaviour(new AnswerQuestionBehaviour(msg_received));
				break;

			default:
				replyWithNotUnderstood(msg_received);
				break;
			}
		} else {
			block();
		}
	}

	private void replyWithNotUnderstood(ACLMessage message) {
		myKnowledgeProcessorAgent.trace("not understood (" + message.getContent() + ")");
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		myKnowledgeProcessorAgent.send(reply);
	}
}
