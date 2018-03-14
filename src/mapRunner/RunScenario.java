package mapRunner;

import jade.Boot;

public class RunScenario {

	public static void main(String args[]) {
		String[] parameters = new String[] { "-gui",
				addAgent("runner", mapRunner.runner.RunnerAgent.class) + addAgent("map", mapRunner.map.MapAgent.class)
						+ addAgent("customer", mapRunner.customer.CustomerAgent.class) };
		Boot.main(parameters);
	}

	public static String addAgent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}
}
