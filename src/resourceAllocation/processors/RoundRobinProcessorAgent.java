package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;

public class RoundRobinProcessorAgent extends ProcessorAgent{

	private static final long serialVersionUID = 6926436616157396556L;
	private float _quantumTime;

    @Override
    public void setup(){
        super.setup();

        _quantumTime = 4;
    }

    public void ShowStatistics(WorkerContainer[] workerContainers) {
        float loadTime = 0;

        float leadTime = 0;
        float leadTimeAverage = 0;

        float waitingTime = 0;
        float waitingTimeAverage = 0;

        for (int i = 0; i < workerContainers.length; ++i){
            loadTime += workerContainers[i].Worker._time;
            leadTime += workerContainers[i].LeadTime;
            waitingTime += workerContainers[i].WaitingTime;
        }

        waitingTimeAverage = waitingTime / workerContainers.length;
        leadTimeAverage = leadTime / workerContainers.length;

        System.out.println("\nВремя обработки составило " + loadTime + " секунд.");
        System.out.println("\nСреднее полное время ожидания составило " + waitingTimeAverage + " секунд.");
        System.out.println("\nСреднее полное время выполнения составило " + leadTimeAverage + " секунд.");
    }

    @Override
    public void Serve(Worker[] agents) {
        addBehaviour(new ServerBehaviour(this, agents, _quantumTime));
    }

    private class WorkerContainer{
        public float TimeRemaining = 0;
        public float LeadTime = 0;
        public float WaitingTime = 0;
        public Worker Worker;
    }

    private class ServerBehaviour extends Behaviour {
		private static final long serialVersionUID = 8318480916613222738L;
		private RoundRobinProcessorAgent _processorAgent;
        private boolean _isDone = false;
//        private Worker[] _agents;
        private ArrayList<WorkerContainer> _workersList;

        public ServerBehaviour(RoundRobinProcessorAgent processorAgent, Worker[] agents, float quantumTime){
            _processorAgent = processorAgent;
//            _agents = agents;
            _workersList = new ArrayList<WorkerContainer>(agents.length);

            for(int i = 0; i < agents.length; ++i){
                WorkerContainer wc = new WorkerContainer();
                wc.Worker = agents[i];
                wc.TimeRemaining = agents[i]._time;
                _workersList.add(wc);
            }
        }

        @Override
        public void action() {
            @SuppressWarnings("unchecked")
			ArrayList<WorkerContainer> workersList = (ArrayList<WorkerContainer>)_workersList.clone();
            while(!workersList.isEmpty()) {
                ArrayList<WorkerContainer> workersToDelete = new ArrayList<WorkerContainer>();

                for (int i = 0; i < workersList.size(); ++i) {
                    WorkerContainer currentWorkerContainer = workersList.get(i);
                    Worker currentWorker = currentWorkerContainer.Worker;

                    float timeInWork = 0;
                    if (currentWorkerContainer.TimeRemaining > _quantumTime){
                        timeInWork = _quantumTime;
                    }
                    else{
                        timeInWork = currentWorkerContainer.TimeRemaining;
                    }

                    currentWorkerContainer.TimeRemaining -= timeInWork;

                    for (int j = 0; j < workersList.size(); ++j){
                        if (workersToDelete.contains(workersList.get(j))){
                            continue;
                        }
                        workersList.get(j).LeadTime += timeInWork;

                        if (i == j){
                            continue;
                        }

                        workersList.get(j).WaitingTime += timeInWork;
                    }

                    System.out.println("Агент " + currentWorker._agent.getName() + " был обслужен за " + timeInWork + " секунд, осталось " + currentWorkerContainer.TimeRemaining + " секунд.");

                    if (currentWorkerContainer.TimeRemaining <= 0){
                        workersToDelete.add(currentWorkerContainer);
                    }
                }

                workersList.removeAll(workersToDelete);
            }


            _processorAgent.ShowStatistics(_workersList.toArray(new WorkerContainer[0]));
            _isDone = true;
        }

        @Override
        public boolean done() {
            return _isDone;
        }
    }
}
