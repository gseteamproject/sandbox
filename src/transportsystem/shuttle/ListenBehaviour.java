package transportsystem.shuttle;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import transportsystem.TransportSystem;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 4767506623520438477L;

	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null) {
			ShuttleAgent myShuttleAgent = (ShuttleAgent) myAgent;

			String conversation = message.getConversationId();
			if(conversation.equals(TransportSystem.TRANSPORTSYSTEM_SHUTTLE_ORDER)) {
				myShuttleAgent.registerOrder(message);				
			} else {
			
				myShuttleAgent.notUnderstood(message);
			}
		} else {
			block();
		}
	}
}