package application;

import jade.Boot;

public class ApplicationController {

	public void executeScenario(String scenario) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = scenario;
		Boot.main(parameters);
	}	
}
