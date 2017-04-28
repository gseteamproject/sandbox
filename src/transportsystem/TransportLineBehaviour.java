package transportsystem;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class TransportLineBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -4056102507089855423L;

	@Override
	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			System.out.println("got order: " + msg_received.getContent());
		} else {
			block();
		}
	}

}
