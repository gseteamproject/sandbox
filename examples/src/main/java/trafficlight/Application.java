package trafficlight;

import application.ArgumentBuilder;
import jade.Boot;

public class Application {
	
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] =
			ArgumentBuilder.agent("traffic-light-1", TrafficLightAgent.class) +
			ArgumentBuilder.agent("traffic-light-2", TrafficLightAgent.class) +
			ArgumentBuilder.agent("car-traffic-1", CarTrafficLightAgent.class) + 
			ArgumentBuilder.agent("car-traffic-2", CarTrafficLightAgent.class);
		Boot.main(parameters);
	}
}
