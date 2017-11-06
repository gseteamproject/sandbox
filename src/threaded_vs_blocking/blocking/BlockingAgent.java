package threaded_vs_blocking.blocking;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class BlockingAgent extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new BlockingParallelBehaviour(this, ParallelBehaviour.WHEN_ANY));
	}

	private static final long serialVersionUID = -8492780507880957445L;
}
