package transportsystem.shuttle;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import transportsystem.TransportSystem;

public class ShuttleAgent extends Agent {

	private static final long serialVersionUID = -5137534167080817246L;
	public String ownerStationName;

	protected void setup() {
		addBehaviour(new ListenBehaviour());
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			ownerStationName = args[0].toString();
		} else {
			ownerStationName = null;
		}
	}

	public void trace(String text) {
		System.out.println(getLocalName() + ": " + text);
	}

	public void moveToStation(ACLMessage message) {

		trace(" Выполняю задание: " + message.getContent());
		String targetStationName = "Station_" + message.getContent();
		System.out.println(ownerStationName);
		if (!targetStationName.equalsIgnoreCase(ownerStationName)) {
			trace(TransportSystem.zapros);
		} else {
			trace(" Заказ " + message.getContent() + " Выполнен!");
		}
	}
}
