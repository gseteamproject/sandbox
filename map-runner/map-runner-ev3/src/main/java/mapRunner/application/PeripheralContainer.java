package mapRunner.application;

import jade.Boot;

public class PeripheralContainer {

	public static void main(String[] args) {
		String[] parameters = new String[] { 
				"-container",
				"-container-name", "Robots",
				"-host", "192.168.1.65", "-local-host", "192.168.1.104",
//				"-host", "localhost", "-local-host", "localhost",
//				ArgumentBuilder.agent("runner", mapRunner.runner.RunnerAgent.class, "debug, 5")
//				+ 
				ArgumentBuilder.agent("runner", mapRunner.runner.RunnerAgent.class, "real, 5")
//				+ 
//				ArgumentBuilder.agent("runner1", mapRunner.runner.RunnerAgent.class, "debug, 1")
//				+ 
//				ArgumentBuilder.agent("runner2", mapRunner.runner.RunnerAgent.class, "debug, 2")
//				+ 
//				ArgumentBuilder.agent("runner3", mapRunner.runner.RunnerAgent.class, "debug, 3")
//				+ 
//				ArgumentBuilder.agent("sniffer", jade.tools.sniffer.Sniffer.class, "runner") 
		};
		Boot.main(parameters);
	}
}
