package trafficlight;

import jade.Boot;

public class Application {
	
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "tl-1:jade.core.Agent";
		Boot.main(parameters);
	}
}
