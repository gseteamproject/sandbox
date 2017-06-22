package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;

import java.util.List;

import jade.core.behaviours.OneShotBehaviour;

public class FirstComeFirstServedProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -6478478046825056806L;

	@Override
	public void serve(List<Worker> agents) {
		addBehaviour(new ServerBehaviour());
	}

	private class ServerBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = -1575698282170078514L;

		@Override
		public void action() {
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
