package transportsystem.shuttle;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import transportsystem.TransportSystem;

public class MoveToStationBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -4030745960752872811L;

	private ACLMessage message;

	public MoveToStationBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		ShuttleAgent myShuttleAgent = (ShuttleAgent) myAgent;
		myShuttleAgent.trace(" Выполняю задание: " + message.getContent());
		String targetStationName = "Station_" + message.getContent();
		System.out.println(myShuttleAgent.ownerStationName);
		if (!targetStationName.equalsIgnoreCase(myShuttleAgent.ownerStationName)) {
			myShuttleAgent.trace(TransportSystem.SHUTTLE_ASKS_CAN_I_MOVE);
		} else {
			myShuttleAgent.trace(" Заказ " + message.getContent() + " Выполнен!");
		}
	}
}
