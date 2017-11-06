package threaded_vs_blocking.threaded;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import threaded_vs_blocking.BlockingBehaviour;
import threaded_vs_blocking.NonBlockingBehaviour;

public class NonBlockingParallelBehaviour extends ParallelBehaviour {

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	public NonBlockingParallelBehaviour(Agent a, int endCondition) {
		super(a, endCondition);

		addSubBehaviour(tbf.wrap(new BlockingBehaviour()));
		addSubBehaviour(new NonBlockingBehaviour(a));
	}

	private static final long serialVersionUID = 5666117344890748746L;
}
