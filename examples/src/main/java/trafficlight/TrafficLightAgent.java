package trafficlight;

import jade.core.Agent;

public class TrafficLightAgent extends Agent {

	public int currentLight;
	
	public int switchTime;
	
	public int amountOfCars;

	@Override
	protected void setup() {
		addBehaviour(new SwitchLightBehaviour());
		addBehaviour(new AdaptSwitchTimeBehaviour());
		addBehaviour(new SyncronizeSwitchTimeBehaviour());
	}
}
