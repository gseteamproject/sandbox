package trafficlight;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TrafficLightAgent extends Agent {

	public int currentLight;
        
       
	
	public int switchTime;
	
	public int amountOfCars;
	
	private int waitingCarsCount = 0;

	@Override
	protected void setup() {
		
                Object[] args = getArguments();
		
		
		if (args != null && args.length > 0) {
			//connectedAgentName = args[0].toString();
			switchTime = Integer.parseInt(args[0].toString());
                        currentLight = Integer.parseInt(args[1].toString());
		}
            
                addBehaviour(new SwitchLightBehaviour(this,switchTime));
		addBehaviour(new AdaptSwitchTimeBehaviour());
		addBehaviour(new SyncronizeSwitchTimeBehaviour());
		
		addBehaviour(new RegisterCarBehaviour());
	}
	
	class RegisterCarBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 6130496380982287815L;

		MessageTemplate template = MessageTemplate.MatchConversationId("car-arrived");
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				waitingCarsCount ++;
				System.out.println(myAgent.getLocalName() + ": Car registered. Total " + waitingCarsCount);
			} else {
				block();
			}
		}
	
	}
        
        
}
