package servingTea.coffeeMachine;

import jade.Boot;

public class Main {
	
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = agent("kofevarka", servingTea.coffeeMachine.CoffeeMachine.class);
		Boot.main(parameters);
	}

	private static String agent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}
}