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

	public void ShowStatistics(Worker[] workers) {
		float loadTime = 0;

		float leadTime = 0;
		float leadTimeAverage = 0;

		float waitingTime = 0;
		float waitingTimeAverage = 0;

		for (Worker worker : workers) {
			loadTime += worker.getTime();
			leadTime += worker.getTime() + worker.waitingTime;
			waitingTime += worker.waitingTime;
		}

		waitingTimeAverage = waitingTime / workers.length;
		leadTimeAverage = leadTime / workers.length;

		System.out.println("\nFull processing time is " + loadTime + " seconds.");
		System.out.println("\nAverage waiting time is " + waitingTimeAverage + " seconds.");
		System.out.println("\nAverage processing time is " + leadTimeAverage + " seconds.");
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
					_workerContainersList.get(j).waitingTime += currentWorker.getTime();
				}

				System.out.println("Agent " + currentWorker.getAgent().getName() + " was processed in "
						+ currentWorker.getTime() + " seconds.");
			}

			_agent.ShowStatistics(_workerContainersList.toArray(new Worker[0]));
		}
	}
}
