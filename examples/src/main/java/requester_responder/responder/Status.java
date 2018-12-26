package requester_responder.responder;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import requester_responder.models.Machine;

public class Status extends TickerBehaviour {

	public Status(Agent a) {
		super(a, 500);
	}

	@Override
	protected void onTick() {
		Activity parent = (Activity) getParent();
		Machine machine = parent.getMachine();

		System.out.println(String.format("%s: machine counter: %d ", myAgent.getLocalName(), machine.counter));
	}

	private static final long serialVersionUID = 1780877247206128637L;
}
