package requester_responder.responder;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Work extends OneShotBehaviour {

	@Override
	public void action() {
		Activity parent = (Activity) getParent();

		ACLMessage inform = parent.getRequest().createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent("already");

		parent.setResult(inform);
	}

	private static final long serialVersionUID = -7007875180021231044L;
}
