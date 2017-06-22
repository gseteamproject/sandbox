package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;

import java.util.List;

import jade.core.behaviours.Behaviour;

public class FirstComeFirstServedProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -6478478046825056806L;

	@Override
    public void serve(List<Worker> agents) {
        addBehaviour(new ServerBehaviour(this, agents));
    }

    public void ShowStatistics(List<Worker> agents){
        float totalWaitingTime = 0;
        float waitingTime = 0;
        float waitingAverageTime = 0;

        float workingTime = 0;
        float workingAverageTime = 0;

        for (int i = 0; i < agents.size(); ++i){
            Worker worker = agents.get(i);

            workingTime += waitingTime + worker.processingTime;

            System.out.println("Время ожидания для агента " + worker._agent.getName() + " составило " + waitingTime + " секунд.");

            if (i < agents.size() - 1){
                waitingTime += worker.processingTime;
                totalWaitingTime += waitingTime;
            }

        }

        waitingAverageTime = totalWaitingTime / agents.size();
        workingAverageTime = workingTime / agents.size();

        System.out.println("\nВремя обработки составило " + waitingTime + " секунд.");
        System.out.println("\nСреднее полное время ожидания составило " + waitingAverageTime + " секунд.");
        System.out.println("\nСреднее полное время выполнения составило " + workingAverageTime + " секунд.");
    }
    

    private class ServerBehaviour extends Behaviour {
		private static final long serialVersionUID = -1575698282170078514L;
		private FirstComeFirstServedProcessorAgent _processorAgent;
        private boolean _isDone = false;
        private List<Worker> _agents;

        public ServerBehaviour(FirstComeFirstServedProcessorAgent processorAgent, List<Worker> agents){
            _processorAgent = processorAgent;
            _agents = agents;
        }

        @Override
        public void action() {
            for(Worker worker : _agents){
                System.out.println("Работник " + worker._agent.getName() + " был обслужен за " + worker.processingTime + " секунд.");
            }

            _processorAgent.ShowStatistics(_agents);
            _isDone = true;
        }

        @Override
        public boolean done() {
            return _isDone;
        }
    }
}
