package mapRunner;

import application.ArgumentBuilder;
import jade.Boot;

public class RunScenario {

	public static void main(String args[]) {
		String[] parameters = new String[] { "-gui",
				ArgumentBuilder.agent("runner", mapRunner.runner.RunnerAgent.class)
						+ ArgumentBuilder.agent("map", mapRunner.map.MapAgent.class)
						+ ArgumentBuilder.agent("customer", mapRunner.customer.CustomerAgent.class) };
		Boot.main(parameters);
	}
}
