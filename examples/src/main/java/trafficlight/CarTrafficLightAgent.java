package trafficlight;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class CarTrafficLightAgent extends Agent {

	private String connectedAgentName;
	public LightsColor light;
			
	@Override
	protected void setup() {
		
		Object[] args = getArguments();
		int period = 0;
		
		if (args != null && args.length > 0) {
			connectedAgentName = args[0].toString();
			period = Integer.parseInt(args[1].toString());
		}
		
		addBehaviour(new CarArrivedBehaviour(this, period));
		addBehaviour(new CarGoneBehaviour(this, period));
		
		addBehaviour(new LightsSwitchSubscriptionInitiator());
	}
	
	class CarArrivedBehaviour extends TickerBehaviour{

		public CarArrivedBehaviour(Agent a, long period) {
			super(a, period);			
		}

		@Override
		protected void onTick() {
			if (light == LightsColor.Red) {
				sendCarArrivedMessage();
			}
		}
		
		private void sendCarArrivedMessage() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((connectedAgentName), AID.ISLOCALNAME));
			msg.setConversationId("car-arrived");
			msg.setContent("Car arrived");
			send(msg);
		}
		
	}
	
	class CarGoneBehaviour extends TickerBehaviour{

		public CarGoneBehaviour(Agent a, long period) {
			super(a, period);			
		}

		@Override
		protected void onTick() {
			if (light == LightsColor.Green) {
				sendCarGoneMessage();
			}
		}
		
		private void sendCarGoneMessage() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((connectedAgentName), AID.ISLOCALNAME));
			msg.setConversationId("car-gone");
			msg.setContent("Car gone");
			send(msg);
		}
		
	}
	
	private class LightsSwitchSubscriptionInitiator extends SubscriptionInitiator {
		public LightsSwitchSubscriptionInitiator() {
			super(null, null);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected Vector prepareSubscriptions(ACLMessage subscription) {
			subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
			subscription.addReceiver(new AID((connectedAgentName), AID.ISLOCALNAME));
			subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
			Vector l = new Vector(1);
			l.addElement(subscription);
			System.out.println(myAgent.getLocalName() + ": subscription request");
			return l;
		}

		@Override
		protected void handleAgree(ACLMessage agree) {
			System.out.println(myAgent.getLocalName() + ": succesfully subscribed");
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			System.out.println(myAgent.getLocalName() + ": subscription failed");
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			light = LightsColor.values()[Integer.valueOf(inform.getContent())];
		}

		private static final long serialVersionUID = -8553947845257751922L;
	}

}
