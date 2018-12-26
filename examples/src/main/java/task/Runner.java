package task;

import jade.Boot;

public class Runner {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = application.ArgumentBuilder.agent("Teller1", task.TellerAgent.class, "Teller2,1000")
				+ application.ArgumentBuilder.agent("Teller2", task.TellerAgent.class, "Teller1,1000")
				+ application.ArgumentBuilder.agent("Boss", task.WatcherAgent.class);
		Boot.main(parameters);
	}

}
