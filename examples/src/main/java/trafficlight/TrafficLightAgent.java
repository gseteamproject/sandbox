package trafficlight;

import jade.core.Agent;

public class TrafficLightAgent extends Agent {

	public int currentLight;
	
	public int switchTime=10000;
	
	public int amountOfCars;

	@Override
	protected void setup() {
		addBehaviour(new SwitchLightBehaviour(this,switchTime));
		addBehaviour(new AdaptSwitchTimeBehaviour());
		addBehaviour(new SyncronizeSwitchTimeBehaviour());
	}
        
        
}
