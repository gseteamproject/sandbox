package requester_responder;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import requester_responder.initiator.ActivityInitiator;

public class RequesterAgent extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new TickerBehaviour(this, 5000) {
			@Override
			protected void onTick() {
				addBehaviour(new ActivityInitiator(myAgent));
			}

			private static final long serialVersionUID = 1L;
		});
	}

	private static final long serialVersionUID = 683846685759308562L;
}
