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
