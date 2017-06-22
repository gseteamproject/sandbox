package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;

public class ShortestJobFirstProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -7482656135774231087L;

	@Override
	public void Serve(Worker[] agents) {
		addBehaviour(new ServerBehaviour(this, agents));
	}

	private class ServerBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -6262507239330173556L;
		private ShortestJobFirstProcessorAgent _agent;
		private ArrayList<Worker> _workerContainersList;

		public ServerBehaviour(ShortestJobFirstProcessorAgent agent, Worker[] workers) {
			_agent = agent;
			_workerContainersList = new ArrayList<Worker>();

			for (Worker worker : workers) {
				_workerContainersList.add(worker);
			}

			_workerContainersList.sort(Worker.compareByTime);
		}

		@Override
		public void action() {

			for (int i = 0; i < _workerContainersList.size(); ++i) {
				Worker currentWorker = _workerContainersList.get(i);

				for (int j = i + 1; j < _workerContainersList.size(); ++j) {
					_workerContainersList.get(j).waitingTime += currentWorker.processingTime;
				}

				System.out.println("Agent " + currentWorker._agent.getName() + " was processed in "
						+ currentWorker.processingTime + " seconds.");
			}

			_agent.ShowStatistics(_workerContainersList.toArray(new Worker[0]));
		}
	}
}
