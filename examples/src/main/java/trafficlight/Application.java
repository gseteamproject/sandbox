package trafficlight;

import application.ArgumentBuilder;
import jade.Boot;

public class Application {
	
	public static void main(String[] args) {
		String[] parameters = new String[2];
                
                
		parameters[0] = "-gui";
		parameters[1] =
			ArgumentBuilder.agent("traffic-light-1", TrafficLightAgent.class,"10000, 0, 5") +
			ArgumentBuilder.agent("traffic-light-2", TrafficLightAgent.class, "10000, 1, 5") +
			ArgumentBuilder.agent("car-traffic-1", CarTrafficLightAgent.class, "traffic-light-1, 1000, 0") + 
			ArgumentBuilder.agent("car-traffic-2", CarTrafficLightAgent.class, "traffic-light-2, 2000, 1");
		Boot.main(parameters);
	}
}
