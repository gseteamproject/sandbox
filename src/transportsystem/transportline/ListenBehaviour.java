package transportsystem.transportline;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import transportsystem.TransportSystem;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -4056102507089855423L;

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();

		if (message != null) {
			TransportLineAgent myTransportLineAgent = (TransportLineAgent) myAgent;
			myTransportLineAgent.trace(" Получен заказ: " + message.getContent());
			String conversationId = message.getConversationId();
			if (conversationId.equals(TransportSystem.TRANSPORTSYSTEM_TRANSPORTLINE_ORDER)) {
				myTransportLineAgent.registerOrder(message);
			}
			if (conversationId.equals(TransportSystem.TRANSPORTSYSTEM_SHUTTLE_ORDER)) {
				myTransportLineAgent.putBack(message);
			}
			if (!conversationId.equalsIgnoreCase(TransportSystem.TRANSPORTSYSTEM_TRANSPORTLINE_ORDER)) {
				if (!conversationId.equals(TransportSystem.TRANSPORTSYSTEM_SHUTTLE_ORDER)) {
					myTransportLineAgent.notUnderstood(message);
				}
			}

		} else {
			block();
		}
	}

}
