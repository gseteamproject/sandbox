package transportsystem.shuttle;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

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

	public void registerOrder(ACLMessage message) {
		addBehaviour(new RegisterOrderBehaviour(message));
	}

	public void notUnderstood(ACLMessage message) {
		addBehaviour(new NotUnderstoodBehaviour(message));
	}
}
