package transportsystem.transportline;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import transportsystem.TransportSystem;


public class RegisterOrderBehaviour extends Behaviour {


	private static final long serialVersionUID = 4248106735454710868L;

	private BehaviourState state = BehaviourState.sending_order;

	private MessageTemplate reply_template;

	private enum BehaviourState {
		sending_order, waiting_for_reply, done
	};
	
	@Override
	public void action() {
		TransportLineAgent myTransportLineAgent = (TransportLineAgent) myAgent;
		switch (state){
		case sending_order:
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(myTransportLineAgent.shuttleList.get(0));
			message.setConversationId(TransportSystem.TRANSPORTSYSTEM_SHUTTLE_ORDER);
			message.setContent("2");
			message.setReplyWith(Long.toString(System.currentTimeMillis()));
			reply_template = MessageTemplate.MatchInReplyTo(message.getReplyWith());
			state = BehaviourState.waiting_for_reply;
			myTransportLineAgent.send(message);
			break;
		case waiting_for_reply:
			ACLMessage reply = myAgent.receive(reply_template);
			if (reply != null) {
				if (reply.getPerformative() == ACLMessage.INFORM) {
					myTransportLineAgent.trace("Задание выполнено");
				} else {
					myTransportLineAgent.trace("ОШИБКА!!!");
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
