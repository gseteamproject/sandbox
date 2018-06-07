package servingTea;

import jade.Boot;

public class Main {
	public static void main(String[] args) {
				//String[] parameters = new String[2];
				//parameters[0] = "-gui";
				//parameters[1] = "waitress:waitress.Waitress;";
				
				String[] parameters = new String[] { "-gui",
						 "-host", "localhost",
						//"waitress:waitress.Waitress;customer:TheGreatestCustomerEverrrr.CoolCustomer_Agent;" 
						 "CoffeeMachine:servingTea.coffeeMachine.CoffeeMachine;"
						 + "Boiler:servingTea.Robots.Robot_Boiler;"
						 + "Grinder:servingTea.Robots.Robot_Grinder;"
						 + "Milker:servingTea.Robots.Robot_Milker;"
						 + "Teaer:servingTea.Robots.Robot_Teaer;"};
				Boot.main(parameters);
				
		
			}
}
