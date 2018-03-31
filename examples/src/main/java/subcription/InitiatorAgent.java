package subcription;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class InitiatorAgent extends Agent {

	private void trace(String message) {
		System.out.println(getLocalName() + ": " + message);
	}

	private int limit = 10;

	@Override
	protected void setup() {
		addBehaviour(new MyWakerBehaviour());
	}

	private class MyWakerBehaviour extends WakerBehaviour {
		public MyWakerBehaviour() {
			super(null, 300);
		}

		@Override
		protected void onWake() {
			addBehaviour(new MySubcriptionInitiator());
		}

		private static final long serialVersionUID = 1886305217565134596L;
	}

	private class MySubcriptionInitiator extends SubscriptionInitiator {
		public MySubcriptionInitiator() {
			super(null, null);
		}

		@SuppressWarnings({ "rawtypes", "unchecked" })
		@Override
		protected Vector prepareSubscriptions(ACLMessage subscription) {
			subscription = new ACLMessage(ACLMessage.SUBSCRIBE);
			subscription.addReceiver(new AID(("responder"), AID.ISLOCALNAME));
			subscription.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
			Vector l = new Vector(1);
			l.addElement(subscription);
			trace("subscription started");
			return l;
		}

		@Override
		protected void handleAgree(ACLMessage agree) {
			trace("agree");
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			trace("refuse");
		}

		boolean cancel_inform = false;

		@Override
		protected void handleInform(ACLMessage inform) {
			if (cancel_inform) {
				trace("subscription cancelled");
				cancellationCompleted(inform.getSender());
			} else {
				int counter = Integer.valueOf(inform.getContent());
				trace("current counter is - " + counter);
				if (counter >= limit) {
					cancel(inform.getSender(), false);
					cancel_inform = true;
				}
			}
		}

		private static final long serialVersionUID = -8553947845257751922L;
	}

	private static final long serialVersionUID = -3190992033692283964L;
}
