package threaded_vs_blocking.blocking;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import threaded_vs_blocking.BlockingBehaviour;
import threaded_vs_blocking.NonBlockingBehaviour;

public class BlockingParallelBehaviour extends ParallelBehaviour {

	public BlockingParallelBehaviour(Agent a, int endCondition) {
		super(a, endCondition);

		addSubBehaviour(new BlockingBehaviour());
		addSubBehaviour(new NonBlockingBehaviour(a));
	}

	private static final long serialVersionUID = 2237160835332572264L;
}
