package selfmessage;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREResponder;

public class ResponderAgent extends Agent {

	@Override
	protected void setup() {
		addBehaviour(new ActivityResponder());
	}

	private class ActivityResponder extends AchieveREResponder {
		public ActivityResponder() {
			super(null, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));
			registerHandleRequest(new Decision());
			registerPrepareResultNotification(new Execution());
		}

		private class Decision extends OneShotBehaviour {
			@Override
			public void action() {
				ACLMessage request = (ACLMessage) getDataStore().get(REQUEST_KEY);
				ACLMessage response = request.createReply();
				response.setPerformative(ACLMessage.AGREE);
				getDataStore().put(RESPONSE_KEY, response);
			}

			private static final long serialVersionUID = 4494839197316629204L;
		}

		private class Execution extends ParallelBehaviour {
			private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

			public Execution() {
				super(null, WHEN_ANY);
				addSubBehaviour(tbf.wrap(new Work()));
				addSubBehaviour(new Success());
				addSubBehaviour(new Failure());
				addSubBehaviour(new Status());
			}

			private class Work extends SimpleBehaviour {
				@Override
				public void action() {
					// add thread-sleep
					// send completition message to self
				}

				@Override
				public boolean done() {
					return false;
				}

				private static final long serialVersionUID = 2388791988973959654L;
			}

			private class Status extends TickerBehaviour {
				public Status() {
					super(null, 500);
				}

				@Override
				protected void onTick() {
					// just show that agent is active
				}

				private static final long serialVersionUID = 8997602545557647730L;
			}

			private class Success extends OneShotBehaviour {
				@Override
				public void action() {
					// add waiting for completition message
					// send inform message
				}

				private static final long serialVersionUID = 4747926179706803322L;
			}

			private class Failure extends WakerBehaviour {
				public Failure() {
					super(null, 5000);
					// send failure message
				}

				private static final long serialVersionUID = -7549844752378583516L;
			}

			private static final long serialVersionUID = -2858297295800555581L;
		}

		private static final long serialVersionUID = 632163594464739296L;
	}

	private static final long serialVersionUID = -3430755832196648259L;
}
