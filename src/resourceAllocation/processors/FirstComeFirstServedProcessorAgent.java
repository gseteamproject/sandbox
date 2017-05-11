package resourceAllocation.processors;

import resourceAllocation.core.ProcessorAgent;
import resourceAllocation.core.Worker;
import jade.core.behaviours.Behaviour;

public class FirstComeFirstServedProcessorAgent extends ProcessorAgent {

	private static final long serialVersionUID = -6478478046825056806L;

	@Override
    public void Serve(Worker[] agents) {
        addBehaviour(new ServerBehaviour(this, agents));
    }

    public void ShowStatistics(Worker[] agents){
        float totalWaitingTime = 0;
        float waitingTime = 0;
        float waitingAverageTime = 0;

        float workingTime = 0;
        float workingAverageTime = 0;

        for (int i = 0; i < agents.length; ++i){
            Worker worker = agents[i];

            workingTime += waitingTime + worker.getTime();

            System.out.println("Время ожидания для агента " + worker.getAgent().getName() + " составило " + waitingTime + " секунд.");

            if (i < agents.length - 1){
                waitingTime += worker.getTime();
                totalWaitingTime += waitingTime;
            }

        }

        waitingAverageTime = totalWaitingTime / agents.length;
        workingAverageTime = workingTime / agents.length;

        System.out.println("\nВремя обработки составило " + waitingTime + " секунд.");
        System.out.println("\nСреднее полное время ожидания составило " + waitingAverageTime + " секунд.");
        System.out.println("\nСреднее полное время выполнения составило " + workingAverageTime + " секунд.");
    }

    private class ServerBehaviour extends Behaviour {
		private static final long serialVersionUID = -1575698282170078514L;
		private FirstComeFirstServedProcessorAgent _processorAgent;
        private boolean _isDone = false;
        private Worker[] _agents;

        public ServerBehaviour(FirstComeFirstServedProcessorAgent processorAgent, Worker[] agents){
            _processorAgent = processorAgent;
            _agents = agents;
        }

        @Override
        public void action() {
            for(int i = 0; i < _agents.length; ++i){
                Worker worker = _agents[i];
                System.out.println("Работник " + worker.getAgent().getName() + " был обслужен за " + worker.getTime() + " секунд.");
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
