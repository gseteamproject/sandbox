package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
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

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	public Activity(Agent a) {
		super(a, WHEN_ANY);
		addSubBehaviour(tbf.wrap(new Work(this)));
		addSubBehaviour(new Status(a));
		addSubBehaviour(new Deadline(a, Machine.DURATION_LIMIT * 1000));
	}

	private static final long serialVersionUID = 8349072518409058029L;
}
