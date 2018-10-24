package servingTea.HomeWork;

import jade.Boot;

public class Robot_main {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = agent("Boiler", servingTea.Robots.Robot_Agent.class);
		Boot.main(parameters);
	}

	private static String agent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}
}
