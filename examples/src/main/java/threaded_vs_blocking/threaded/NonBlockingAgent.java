package threaded_vs_blocking.threaded;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class NonBlockingAgent extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new NonBlockingParallelBehaviour(this, ParallelBehaviour.WHEN_ANY));
	}

	private static final long serialVersionUID = -1847818545244544693L;
}
