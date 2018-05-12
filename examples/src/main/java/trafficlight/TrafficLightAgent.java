package trafficlight;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class TrafficLightAgent extends Agent {

	public int currentLight;
	
	public int switchTime=10000;
	
	public int amountOfCars = 0;
	
	public MySubscriptionManager myManager = new MySubscriptionManager();
	
	@Override
	protected void setup() {
		addBehaviour(new SwitchLightBehaviour(this,switchTime));
		addBehaviour(new AdaptSwitchTimeBehaviour());
		addBehaviour(new SyncronizeSwitchTimeBehaviour());
		
		addBehaviour(new RegisterCarArrivedBehaviour());
		addBehaviour(new RegisterCarGoneBehaviour());
		
		addBehaviour(new LightSwitchSubscriotionResponder());
	}
	
	class RegisterCarArrivedBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 6130496380982287815L;

		MessageTemplate template = MessageTemplate.MatchConversationId("car-arrived");
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				amountOfCars ++;				
				System.out.println(myAgent.getLocalName() + ": Car arrived. Total " + amountOfCars);
			} else {
				block();
			}
		}	
	}
	
	class RegisterCarGoneBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = 6130496380982287815L;

		MessageTemplate template = MessageTemplate.MatchConversationId("car-gone");
		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(template);
			if (msg != null) {
				amountOfCars = amountOfCars > 0 ? amountOfCars - 1 : 0;				 
				System.out.println(myAgent.getLocalName() + ": Car gone. Total " + amountOfCars);
			} else {
				block();
			}
		}	
	}
	
	private class LightSwitchSubscriotionResponder extends SubscriptionResponder {
		public LightSwitchSubscriotionResponder() {
			super(null, SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE), myManager);
		}

		@Override
		protected ACLMessage handleSubscription(ACLMessage subscription)
				throws NotUnderstoodException, RefuseException {
			ACLMessage decisionResult = subscription.createReply();
			
			try {
				super.handleSubscription(subscription);
				decisionResult.setPerformative(ACLMessage.AGREE);
			} catch (RefuseException e) {
				decisionResult.setPerformative(ACLMessage.REFUSE);
			} catch (NotUnderstoodException e) {
				decisionResult.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			}
			return decisionResult;
		}

		@Override
		protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
			super.handleCancel(cancel);
			ACLMessage cancellResponse = cancel.createReply();
			cancellResponse.setPerformative(ACLMessage.INFORM);
			return cancellResponse;
		}

		private static final long serialVersionUID = -3111194745853398686L;
	}

	class MySubscriptionManager implements SubscriptionManager {
		private Map<String, Subscription> subscriptions = new HashMap<String, Subscription>();

		@Override
		public boolean register(Subscription s) throws RefuseException, NotUnderstoodException {
			subscriptions.put(s.getMessage().getConversationId(), s);
			return true;
		}

		@Override
		public boolean deregister(Subscription s) throws FailureException {
			subscriptions.remove(s.getMessage().getConversationId());
			return true;
		}

		public void notifyAll(String event) {
			Iterator<Subscription> i = subscriptions.values().iterator();
			while (i.hasNext()) {
				Subscription s = (Subscription) i.next();
				ACLMessage notification = new ACLMessage(ACLMessage.INFORM);
				notification.setContent(event);
				s.notify(notification);
			}
		}
	}

        
        
}
