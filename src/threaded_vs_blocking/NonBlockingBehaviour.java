package threaded_vs_blocking;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class NonBlockingBehaviour extends TickerBehaviour {

	public NonBlockingBehaviour(Agent a) {
		super(a, 1000);
	}

	@Override
	protected void onTick() {
		System.out.println("tick");
	}

	private static final long serialVersionUID = -6478193925396069986L;
}
