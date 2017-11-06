package requester_responder.responder;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Work extends OneShotBehaviour {

	@Override
	public void action() {
		ActivityResponder parent = (ActivityResponder) getParent();

		ACLMessage request = (ACLMessage) getDataStore().get(parent.REQUEST_KEY);

		ACLMessage inform = request.createReply();
		inform.setPerformative(ACLMessage.INFORM);
		inform.setContent("already");
		
		getDataStore().put(parent.RESULT_NOTIFICATION_KEY, inform);
	}

	private static final long serialVersionUID = -7007875180021231044L;
}
