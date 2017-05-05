package transportsystem;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ShuttleAgentBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 4767506623520438477L;
	

	@Override
	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			System.out.println(myAgent.getLocalName()+" Выполнено задание: " + msg_received.getContent());
		} else {
			block();

		}
	}
}