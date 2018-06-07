package servingTea;

import jade.Boot;

public class Main {
	public static void main(String[] args) {
		String[] parameters = new String[] { "-gui", "-host", "localhost",
				// "waitress:waitress.Waitress;customer:TheGreatestCustomerEverrrr.CoolCustomer_Agent;"
				agent("CoffeeMachine", servingTea.coffeeMachine.CoffeeMachine.class)
						+ agent("Boiler", servingTea.Robots.Robot_Boiler.class)
						+ agent("Grinder", servingTea.Robots.Robot_Grinder.class)
						+ agent("Milker", servingTea.Robots.Robot_Milker.class)
						+ agent("Teaer", servingTea.Robots.Robot_Teaer.class) };
		Boot.main(parameters);

	}

	private static String agent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}
}
