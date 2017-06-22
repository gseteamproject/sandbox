package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.OneShotBehaviour;

import java.util.List;

public class ShortestJobFirstProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -7482656135774231087L;

	@Override
	public void serve(List<Worker> agents) {
		addBehaviour(new ServerBehaviour());
	}

	private class ServerBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -6262507239330173556L;

		@Override
		public void action() {
			workers.sort(Worker.compareByTime);

			for (int i = 0; i < workers.size(); ++i) {
				Worker currentWorker = workers.get(i);

				for (int j = i + 1; j < workers.size(); ++j) {
					workers.get(j).waitingTime += currentWorker.processingTime;
				}
			}

			showStatistics();
		}
	}
}
