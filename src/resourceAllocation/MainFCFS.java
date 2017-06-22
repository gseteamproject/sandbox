package resourceAllocation;

import jade.Boot;

public class MainFCFS {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = Helpers.addAgent("processor", resourceAllocation.processors.FirstComeFirstServedProcessorAgent.class);
		parameters[1] += Helpers.addAgent("dummy1", resourceAllocation.core.WorkerAgent.class, "5");
		parameters[1] += Helpers.addAgent("dummy2", resourceAllocation.core.WorkerAgent.class, "3");
		parameters[1] += Helpers.addAgent("dummy3", resourceAllocation.core.WorkerAgent.class, "7");
		parameters[1] += Helpers.addAgent("dummy4", resourceAllocation.core.WorkerAgent.class, "1");
		Boot.main(parameters);
	}
}
