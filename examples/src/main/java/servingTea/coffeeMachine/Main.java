package servingTea.coffeeMachine;

import jade.Boot;

public class Main {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "kofevarka:coffeeMachine.CoffeeMachine";
		Boot.main(parameters);
	}
}