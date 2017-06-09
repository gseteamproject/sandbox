package transportsystem.transportline;


import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class NotUnderstoodBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 1926694260052654089L;
	private ACLMessage message;

	public NotUnderstoodBehaviour(ACLMessage message) {
		this.message = message;
	}

	@Override
	public void action() {
		TransportLineAgent myTransportLineAgent = (TransportLineAgent) myAgent;
		myTransportLineAgent.trace("not understood (" + message.getContent() + ")");
		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
		myTransportLineAgent.send(reply);
	}
}
