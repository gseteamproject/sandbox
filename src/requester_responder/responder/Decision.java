package requester_responder.responder;

import java.util.Random;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class Decision extends OneShotBehaviour {

	private final Random randomizer = new Random();

	@Override
	public void action() {
		ActivityResponder parent = (ActivityResponder) getParent();
		ACLMessage request = (ACLMessage) getDataStore().get(parent.REQUEST_KEY);

		int decision = randomizer.nextInt(100);

		ACLMessage response = request.createReply();
		if (decision > 50) {
			response.setPerformative(ACLMessage.AGREE);
			response.setContent("now");
		} else {
			response.setPerformative(ACLMessage.REFUSE);
			response.setContent("busy doing another job");
		}

		getDataStore().put(parent.RESPONSE_KEY, response);
	}

	private static final long serialVersionUID = 8554746504981566315L;
}
