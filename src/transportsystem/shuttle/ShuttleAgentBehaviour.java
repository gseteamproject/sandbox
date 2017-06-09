package transportsystem.shuttle;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ShuttleAgentBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 4767506623520438477L;
	public String zapros = "Можно ехать?";

	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			System.out.println(myAgent.getLocalName() + " Выполняю задание: " + msg_received.getContent());

			ShuttleAgent sha = (ShuttleAgent) myAgent;
			String targetStationName = "Station_" + msg_received.getContent();
			System.out.println(sha.ownerStationName);
			if (!targetStationName.equalsIgnoreCase(sha.ownerStationName)) {
				System.out.println(myAgent.getLocalName() + zapros);

			} else {
				System.out.println(myAgent.getLocalName() + " Заказ " + msg_received.getContent() + " Выполнен!");
			}

		} else {
			block();

		}

	}
}