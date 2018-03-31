package requester_responder.responder;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import requester_responder.Vocabulary;

public class Decision extends OneShotBehaviour {

	@Override
	public void action() {
		ActivityResponder parent = (ActivityResponder) getParent();
		ACLMessage request = (ACLMessage) getDataStore().get(parent.REQUEST_KEY);

		Machine machine = (Machine) getDataStore().get(Vocabulary.MACHINE_OBJECT_KEY);

		ACLMessage response = request.createReply();
		if (machine.willExecute()) {
			response.setPerformative(ACLMessage.AGREE);
			response.setContent(machine.getDurationEstimated());
		} else {
			response.setPerformative(ACLMessage.REFUSE);
			response.setContent(machine.getReason());
		}

		getDataStore().put(parent.RESPONSE_KEY, response);
	}

	private static final long serialVersionUID = 8554746504981566315L;
}
