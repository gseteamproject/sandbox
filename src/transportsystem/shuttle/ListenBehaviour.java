package transportsystem.shuttle;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 4767506623520438477L;

	public void action() {
		ShuttleAgent myShuttleAgent = (ShuttleAgent) myAgent;
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			myShuttleAgent.moveToStation(msg_received);
		} else {
			block();
		}
	}
}