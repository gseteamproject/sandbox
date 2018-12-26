package requester_responder.requester;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class PeriodicActivityRequest extends TickerBehaviour {

	public PeriodicActivityRequest(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		myAgent.addBehaviour(new ActivityRequest(myAgent));
	}

	private static final long serialVersionUID = 1333176766562381467L;
}
