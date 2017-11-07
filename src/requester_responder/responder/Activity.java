package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import requester_responder.Vocabulary;

public class Activity extends ParallelBehaviour {
	
	public Machine getMachine() {
		return (Machine) getDataStore().get(Vocabulary.MACHINE_OBJECT_KEY);
	}

	public void setResult(ACLMessage result) {
		ActivityResponder parent = (ActivityResponder) getParent();
		getDataStore().put(parent.RESULT_NOTIFICATION_KEY, result);
	}

	public ACLMessage getRequest() {
		ActivityResponder parent = (ActivityResponder) getParent();
		return (ACLMessage) getDataStore().get(parent.REQUEST_KEY);
	}

	public Activity(Agent a) {
		super(a, WHEN_ANY);
		addSubBehaviour(new Work());
	}

	private static final long serialVersionUID = 8349072518409058029L;
}
