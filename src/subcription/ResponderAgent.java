package subcription;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class ResponderAgent extends Agent {

	MySubscriptionManager myManager = new MySubscriptionManager();

	private void trace(String message) {
		System.out.println(getLocalName() + ": " + message);
	}

	@Override
	protected void setup() {
		addBehaviour(new MySubscriptionResponder());
		addBehaviour(new EventBehaviour());
	}

	private class MySubscriptionResponder extends SubscriptionResponder {
		public MySubscriptionResponder() {
			super(null, SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE), myManager);
		}

		@Override
		protected ACLMessage handleSubscription(ACLMessage subscription)
				throws NotUnderstoodException, RefuseException {
			ACLMessage decisionResult = subscription.createReply();
			try {
				super.handleSubscription(subscription);
				trace("subcriber added");
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
			trace("subscriber removed");
			ACLMessage cancellResponse = cancel.createReply();
			cancellResponse.setPerformative(ACLMessage.INFORM);
			return cancellResponse;
		}

		private static final long serialVersionUID = -3111194745853398686L;
	}

	private class MySubscriptionManager implements SubscriptionManager {
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

	private class EventBehaviour extends TickerBehaviour {
		public EventBehaviour() {
			super(null, 300);
		}

		public int counter = 0;

		@Override
		protected void onTick() {
			counter++;
			myManager.notifyAll(Integer.toString(counter));
		}

		private static final long serialVersionUID = -2370129159713678330L;
	}

	private static final long serialVersionUID = 8316644569175847583L;
}
