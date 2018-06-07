package servingTea.HomeWork;

import jade.Boot;

public class Main {

	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "waitress:HomeWork.Waitress;customer:HomeWork.Customer;coffeemaschine:HomeWork.CoffeeMaschine";
		Boot.main(parameters);
	}
}
