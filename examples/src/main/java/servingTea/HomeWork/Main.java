package servingTea.HomeWork;

import jade.Boot;

public class Main {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = agent("waitress", servingTea.HomeWork.Waitress.class)
				+ agent("customer", servingTea.HomeWork.Customer.class)
				+ agent("coffeemaschine", servingTea.HomeWork.CoffeeMaschine.class);
		Boot.main(parameters);
	}

	private static String agent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}
}
