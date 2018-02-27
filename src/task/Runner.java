package task;

import jade.Boot;

public class Runner {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "Teller1:task.TellerAgent(Teller2,1000);Teller2:task.TellerAgent(Teller1,1500);Boss:task.WatcherAgent()";
		Boot.main(parameters);
	}

}
