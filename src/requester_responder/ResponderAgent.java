package requester_responder;

import jade.core.Agent;
import requester_responder.responder.ActivityResponder;

public class ResponderAgent extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new ActivityResponder(this));
	}

	private static final long serialVersionUID = 579752306271997815L;
}
