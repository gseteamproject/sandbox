package resourceAllocation.processors;

import com.kmu.core.ProcessorAgent;
import com.kmu.core.Worker;
import jade.core.behaviours.Behaviour;

import java.util.ArrayList;

public class ShortestJobFirstProcessorAgent extends ProcessorAgent {

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

        for (int i = 0; i < workerContainers.length; ++i){
            loadTime += workerContainers[i].Worker.getTime();
            leadTime += workerContainers[i].Worker.getTime() + workerContainers[i].WaitingTime;
            waitingTime += workerContainers[i].WaitingTime;
        }

        waitingTimeAverage = waitingTime / workerContainers.length;
        leadTimeAverage = leadTime / workerContainers.length;

        System.out.println("\nВремя обработки составило " + loadTime + " секунд.");
        System.out.println("\nСреднее полное время ожидания составило " + waitingTimeAverage + " секунд.");
        System.out.println("\nСреднее полное время выполнения составило " + leadTimeAverage + " секунд.");
    }

    private class ServerBehaviour extends Behaviour {

        private boolean _isDone = false;
        private ShortestJobFirstProcessorAgent _agent;
        private ArrayList<WorkerContainer> _workerContainersList;

        public ServerBehaviour(ShortestJobFirstProcessorAgent agent, Worker[] agents){
            _agent = agent;
            _workerContainersList = new ArrayList<WorkerContainer>();

            for (int i = 0; i < agents.length; ++i){
                Worker currentWorker = agents[i];
                WorkerContainer workerContainer = new WorkerContainer();
                workerContainer.Worker = currentWorker;

                _workerContainersList.add(workerContainer);
            }

            _workerContainersList.sort((o1, o2) -> {
                if (o1.Worker.getTime() < o2.Worker.getTime()){
                    return -1;
                } else if (o1.Worker.getTime() > o2.Worker.getTime()){
                    return 1;
                }
                else{
                    return 0;
                }
            });
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


    private class WorkerContainer{
        public float WaitingTime = 0;
        public Worker Worker;
    }
}
