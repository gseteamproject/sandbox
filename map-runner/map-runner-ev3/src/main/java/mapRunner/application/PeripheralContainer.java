package mapRunner.application;

import jade.Boot;

public class PeripheralContainer {

	public static void main(String[] args) {
		String[] parameters = new String[] {
			"-container",
			"-host", "localhost",
			"-local-host", "localhost",
			ArgumentBuilder.agent("runner", mapRunner.runner.RunnerAgent.class, "debug")+
			ArgumentBuilder.agent("runner2", mapRunner.runner.RunnerAgent.class, "debug")+
			ArgumentBuilder.agent("sniffer", jade.tools.sniffer.Sniffer.class, "runner")
		};
		Boot.main(parameters);
	}
}
