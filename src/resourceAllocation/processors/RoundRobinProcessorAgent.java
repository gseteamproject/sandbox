package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.Iterator;

public class RoundRobinProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = 6926436616157396556L;

	private static final long cycleTimePeriod = 4;

	@Override
	public void serve() {
		addBehaviour(new ServerBehaviour());
	}

	private class ServerBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 8318480916613222738L;

		public ServerBehaviour() {
			for (Worker worker : workers) {
				worker.remainingTime = worker.processingTime;
			}
		}

		@Override
		public void action() {
			@SuppressWarnings("unchecked")
			ArrayList<Worker> activeWorkers = (ArrayList<Worker>) workers.clone();

			long time = 0;

			while (!activeWorkers.isEmpty()) {
				Iterator<Worker> iterator = activeWorkers.iterator();
				while (iterator.hasNext()) {
					Worker worker = (Worker) iterator.next();
					if (worker.remainingTime == worker.processingTime) {
						worker.startedAt = time;
					}
					if (worker.remainingTime < cycleTimePeriod) {
						time += worker.remainingTime;
						worker.finishedAt = time;
						worker.remainingTime = 0;
					} else {
						time += cycleTimePeriod;
						worker.remainingTime -= cycleTimePeriod;
					}
					if (worker.remainingTime <= 0) {
						iterator.remove();
					} else {
						worker.processSwitching ++;
					}
				}
			}

			showStatistics();
		}
	}
}
