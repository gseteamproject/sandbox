package transportsystem.shuttle;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class NotUnderstoodBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = -7559957030132976011L;

	private ACLMessage message;

	public NotUnderstoodBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		ShuttleAgent myShuttleAgent = (ShuttleAgent) myAgent;
		myShuttleAgent.trace("not understood (" + message.getContent() + ")");
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		myShuttleAgent.send(reply);
	}
}
