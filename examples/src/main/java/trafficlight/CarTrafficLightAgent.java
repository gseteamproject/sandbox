package trafficlight;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class CarTrafficLightAgent extends Agent {

	private String connectedAgentName;
			
	@Override
	protected void setup() {
		
		Object[] args = getArguments();
		int period = 0;
		
		if (args != null && args.length > 0) {
			connectedAgentName = args[0].toString();
			period = Integer.parseInt(args[1].toString());
		}
		
		addBehaviour(new CarArrivedBehaviour(this, period));
	}
	
	class CarArrivedBehaviour extends TickerBehaviour{

		public CarArrivedBehaviour(Agent a, long period) {
			super(a, period);			
		}

		@Override
		protected void onTick() {
			sendCarArrivedMessage();
		}
		
		private void sendCarArrivedMessage() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((connectedAgentName), AID.ISLOCALNAME));
			msg.setConversationId("car-arrived");
			msg.setContent("Car arrived");
			send(msg);
		}
		
	}

}
