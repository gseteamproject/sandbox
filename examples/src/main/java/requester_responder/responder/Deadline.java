package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;

public class Deadline extends WakerBehaviour {

	public Deadline(Agent a, long timeout) {
		super(a, timeout);
	}
	
	@Override
	protected void onWake() {
		Activity parent = (Activity) getParent();

		Machine machine = parent.getMachine();
		machine.terminate();

		ACLMessage inform = parent.getRequest().createReply();
		inform.setPerformative(ACLMessage.FAILURE);
		inform.setContent(machine.getReason());

		parent.setResult(inform);		
	}

	private static final long serialVersionUID = 7276757247489520051L;
}
