package selfmessage;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class RequesterAgent extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new WakerBehaviour(null, 3000) {
			@Override
			protected void onWake() {
				addBehaviour(new ActivityInitiator());
			}

			private static final long serialVersionUID = -3081399341585325226L;
		});
	}

	private class ActivityInitiator extends AchieveREInitiator {
		public ActivityInitiator() {
			super(null, null);
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected Vector prepareRequests(ACLMessage request) {
			request = new ACLMessage(ACLMessage.REQUEST);
			request.addReceiver(new AID(("responder"), AID.ISLOCALNAME));
			request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			Vector<ACLMessage> l = new Vector<ACLMessage>(1);
			l.addElement(request);
			return l;
		}

		@Override
		protected void handleAgree(ACLMessage agree) {
			System.out.println("recieved: agree");
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			System.out.println("recieved: refuse");
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			System.out.println("recieved: inform");
		}

		@Override
		public void handleFailure(ACLMessage failure) {
			System.out.println("recieved: failure");
		}

		private static final long serialVersionUID = -6526955054837302593L;
	}

	private static final long serialVersionUID = 8940961727570173966L;
}
