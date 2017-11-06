package requester_responder.responder;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Decision extends OneShotBehaviour {

	@Override
	public void action() {
		ActivityResponder parent = (ActivityResponder) getParent();
		
		ACLMessage request = (ACLMessage) getDataStore().get(parent.REQUEST_KEY);
		
		ACLMessage agree = request.createReply();
		agree.setPerformative(ACLMessage.AGREE);
		agree.setContent("now");
		
		getDataStore().put(parent.RESPONSE_KEY, agree);
	}
	
	private static final long serialVersionUID = 8554746504981566315L;
}
