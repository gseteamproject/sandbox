package requester_responder;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import requester_responder.responder.ActivityResponder;
import requester_responder.responder.Machine;

public class ResponderAgent extends Agent {

	Machine machine = new Machine();

	@Override
	protected void setup() {
		Behaviour main = new ActivityResponder(this);
		main.getDataStore().put(Vocabulary.MACHINE_OBJECT_KEY, machine);

		addBehaviour(main);
	}

	private static final long serialVersionUID = 579752306271997815L;
}
