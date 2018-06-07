package servingTea.HomeWork;

import jade.Boot;

public class Robot_main {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "Boiler:Robots.Robot_Agent;";
		Boot.main(parameters);
	}
}
