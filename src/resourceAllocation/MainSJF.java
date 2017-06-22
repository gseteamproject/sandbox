package resourceAllocation;

import jade.Boot;

public class MainSJF {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = Helpers.addAgent("processor", resourceAllocation.processors.ShortestJobFirstProcessorAgent.class);
		parameters[1] += Helpers.addAgent("dummy1", resourceAllocation.DummyAgent.class, "5");
		parameters[1] += Helpers.addAgent("dummy2", resourceAllocation.DummyAgent.class, "3");
		parameters[1] += Helpers.addAgent("dummy3", resourceAllocation.DummyAgent.class, "7");
		parameters[1] += Helpers.addAgent("dummy4", resourceAllocation.DummyAgent.class, "1");
		Boot.main(parameters);
	}
}
