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

			long time = 0;

			for (Worker worker : workers) {
				worker.waitingTime = time;
				worker.startedAt = time;
				time += worker.processingTime;
				worker.finishedAt = time;
			}

			showStatistics();
		}
	}
}
