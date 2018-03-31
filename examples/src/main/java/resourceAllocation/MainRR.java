package resourceAllocation;

import jade.Boot;

public class MainRR {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = Helpers.addAgent("processor", resourceAllocation.processors.RoundRobinProcessorAgent.class);
		parameters[1] += Helpers.addAgent("dummy1", resourceAllocation.workers.WorkerAgent.class, "5");
		parameters[1] += Helpers.addAgent("dummy2", resourceAllocation.workers.WorkerAgent.class, "3");
		parameters[1] += Helpers.addAgent("dummy3", resourceAllocation.workers.WorkerAgent.class, "7");
		parameters[1] += Helpers.addAgent("dummy4", resourceAllocation.workers.WorkerAgent.class, "1");
		Boot.main(parameters);
	}
}
