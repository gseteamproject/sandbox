package mapRunner;

import application.ArgumentBuilder;
import jade.Boot;

public class PeripheralContainer {

	public static void main(String[] args) {
		String[] parameters = new String[] {
			"-container",
			"-host", "localhost",
			"-local-host", "localhost",
			ArgumentBuilder.agent("runner", mapRunner.runner.RunnerAgent.class)
		};
		Boot.main(parameters);
	}
}
