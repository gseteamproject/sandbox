package knowledge.producer;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ProduceFactBehaviour extends Behaviour {

	private static final long serialVersionUID = -4202261991202665752L;

	private AID knowledgeProcessor;
	private String fact;
	private BehaviourState state = BehaviourState.sending_fact;

	private MessageTemplate reply_template;

	private enum BehaviourState {
		sending_fact, waiting_for_reply, done
	};

	public ProduceFactBehaviour(AID knowledgeProcessor, String fact) {
		this.knowledgeProcessor = knowledgeProcessor;
		this.fact = fact;
	}

	@Override
	public void action() {
		KnowledgeProducerAgent myKnowledgeProducerAgent = (KnowledgeProducerAgent) myAgent;
		switch (state) {
		case sending_fact:
			ACLMessage message = new ACLMessage(ACLMessage.INFORM);
			message.addReceiver(knowledgeProcessor);
			message.setContent(fact);
			message.setReplyWith(Long.toString(System.currentTimeMillis()));
			reply_template = MessageTemplate.MatchInReplyTo(message.getReplyWith());
			state = BehaviourState.waiting_for_reply;
			myKnowledgeProducerAgent.send(message);
			break;
		case waiting_for_reply:
			ACLMessage reply = myAgent.receive(reply_template);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.CONFIRM) {
					myKnowledgeProducerAgent.trace("knowledge processor liked my fact");
				} else {
					myKnowledgeProducerAgent.trace("knowledge processor does not liked my fact");
				}
				state = BehaviourState.done;
			} else {
				block();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean done() {
		return state == BehaviourState.done;
	}
}
