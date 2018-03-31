package requester_responder.responder;

import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class Work extends SimpleBehaviour {

	private Activity owner = null;

	public Work(Activity ownerActivity) {
		this.owner = ownerActivity;
	}

	@Override
	public void action() {
		Machine machine = owner.getMachine();
		machine.execute();

		ACLMessage inform = owner.getRequest().createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent(machine.getDuration());

		owner.setResult(inform);
	}

	private static final long serialVersionUID = -7007875180021231044L;

	@Override
	public boolean done() {
		return owner.getMachine().executed;
	}
}
