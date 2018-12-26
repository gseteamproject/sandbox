package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
import requester_responder.models.Machine;
import requester_responder.models.Vocabulary;

public class Activity extends ParallelBehaviour {

	public Machine getMachine() {
		return (Machine) getDataStore().get(Vocabulary.MACHINE_OBJECT_KEY);
	}

	ACLMessage result = null;

	public void setResult(ACLMessage result) {
//		ActivityRespond parent = (ActivityRespond) getParent();
//		getDataStore().put(parent.RESULT_NOTIFICATION_KEY, result);
		this.result = result;
	}

	ACLMessage request = null;

	public ACLMessage getRequest() {
//		 ActivityRespond parent = (ActivityRespond) getParent();
//		 return (ACLMessage) getDataStore().get(parent.REQUEST_KEY);
		return this.request;
	}

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	public Activity(Agent a) {
		super(a, WHEN_ANY);
		addSubBehaviour(tbf.wrap(new Work(this)));
		addSubBehaviour(new Status(a));
		addSubBehaviour(new Deadline(a, Machine.DURATION_LIMIT * 1000));
	}

	public Activity(Agent myAgent, ACLMessage request) {
		this(myAgent);
		this.request = request;
	}

	@Override
	public int onEnd() {
		myAgent.send(result);
		return super.onEnd();
	}

	private static final long serialVersionUID = 8349072518409058029L;
}
