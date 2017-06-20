package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Comparator;

public class ShortestJobFirstProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -7482656135774231087L;


	@Override
    public void Serve(Worker[] agents) {
        addBehaviour(new ServerBehaviour(this, agents));
    }

    public void ShowStatistics(WorkerContainer[] workerContainers){
        float loadTime = 0;

        float leadTime = 0;
        float leadTimeAverage = 0;

        float waitingTime = 0;
        float waitingTimeAverage = 0;

        for (WorkerContainer workerContainer : workerContainers){
            loadTime += workerContainer.Worker.getTime();
            leadTime += workerContainer.Worker.getTime() + workerContainer.WaitingTime;
            waitingTime += workerContainer.WaitingTime;
        }

        waitingTimeAverage = waitingTime / workerContainers.length;
        leadTimeAverage = leadTime / workerContainers.length;

        System.out.println("\nВремя обработки составило " + loadTime + " секунд.");
        System.out.println("\nСреднее полное время ожидания составило " + waitingTimeAverage + " секунд.");
        System.out.println("\nСреднее полное время выполнения составило " + leadTimeAverage + " секунд.");
    }

    private class ServerBehaviour extends Behaviour {

		private static final long serialVersionUID = -6262507239330173556L;
		private boolean _isDone = false;
        private ShortestJobFirstProcessorAgent _agent;
        private ArrayList<WorkerContainer> _workerContainersList;

        public ServerBehaviour(ShortestJobFirstProcessorAgent agent, Worker[] workers){
            _agent = agent;
            _workerContainersList = new ArrayList<WorkerContainer>();

			for (Worker worker : workers) {
				_workerContainersList.add(new WorkerContainer(worker));
			}

            _workerContainersList.sort(WorkerContainer.compareByWorkerTime);
        }

        @Override
        public void action() {

            for (int i = 0; i < _workerContainersList.size(); ++i) {
                WorkerContainer currentWorkerContainer = _workerContainersList.get(i);
                Worker currentWorker = currentWorkerContainer.Worker;

                for (int j = i + 1; j < _workerContainersList.size(); ++j) {
                    _workerContainersList.get(j).WaitingTime += currentWorker.getTime();
                }

                System.out.println("Агент " + currentWorker.getAgent().getName() + " был обслужен за " + currentWorker.getTime() + " секунд.");
            }

            _agent.ShowStatistics(_workerContainersList.toArray(new WorkerContainer[0]));
            _isDone = true;
        }

        @Override
        public boolean done() {
            return _isDone;
        }
    }

	private static class WorkerContainer {
		public float WaitingTime = 0;
		public Worker Worker;

		public WorkerContainer(Worker worker) {
			this.Worker = worker;
		}

		public final static Comparator<WorkerContainer> compareByWorkerTime = new Comparator<WorkerContainer>() {
			@Override
			public int compare(WorkerContainer o1, WorkerContainer o2) {
				if (o1.Worker.getTime() < o2.Worker.getTime()) {
					return -1;
				} else if (o1.Worker.getTime() > o2.Worker.getTime()) {
					return 1;
				} else {
					return 0;
				}
			}
		};
	}
}
