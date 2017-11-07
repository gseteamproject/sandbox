package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

public class Activity extends ParallelBehaviour {

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

		DataStore thisDataStore = getDataStore();

		Behaviour work = new Work();
		work.setDataStore(thisDataStore);
		addSubBehaviour(work);
	}

	private static final long serialVersionUID = 8349072518409058029L;
}
