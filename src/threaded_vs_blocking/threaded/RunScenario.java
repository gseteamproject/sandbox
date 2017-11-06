package threaded_vs_blocking.threaded;

import jade.Boot;

public class RunScenario {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "agent:" + threaded_vs_blocking.threaded.NonBlockingAgent.class.getName();
		Boot.main(parameters);
	}
}
