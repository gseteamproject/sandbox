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
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ResponderAgent extends Agent {
	@Override
	protected void setup() {
		addBehaviour(new ActivityResponder());
	}

	private class ActivityResponder extends AchieveREResponder {
		public ACLMessage getRequest() {
			return (ACLMessage) getDataStore().get(REQUEST_KEY);
		}

		public void setResponse(ACLMessage response) {
			getDataStore().put(RESPONSE_KEY, response);
		}

		public void setResult(ACLMessage result) {
			getDataStore().put(RESULT_NOTIFICATION_KEY, result);
		}

		public ActivityResponder() {
			super(null, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));
			registerHandleRequest(new Decision());
			registerPrepareResultNotification(new Execution());
		}

		private class Decision extends OneShotBehaviour {
			@Override
			public void action() {
				ACLMessage response = getRequest().createReply();
				response.setPerformative(ACLMessage.AGREE);
				setResponse(response);
			}

			private static final long serialVersionUID = 4494839197316629204L;
		}

		private class Execution extends ParallelBehaviour {
			private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

			private TickerBehaviour status = new Status();
			private WakerBehaviour deadline = new Deadline();

			public Execution() {
				super(null, WHEN_ALL);
				addSubBehaviour(tbf.wrap(new Work()));
				addSubBehaviour(new WorkResult());
				addSubBehaviour(status);
				addSubBehaviour(deadline);
			}

			private class Work extends OneShotBehaviour {
				@Override
				public void action() {
					System.out.println("operation execution ...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("operation execution success");
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(getAgent().getAID());
					msg.setConversationId("work-result");
					msg.setContent("success");
					send(msg);
				}

				private static final long serialVersionUID = 2388791988973959654L;
			}

			private class Status extends TickerBehaviour {
				public Status() {
					super(null, 500);
				}

				@Override
				protected void onTick() {
					System.out.println("execution in process");
				}

				private static final long serialVersionUID = 8997602545557647730L;
			}

			private class WorkResult extends SimpleBehaviour {
				private boolean isMessageSent = false;

				@Override
				public void action() {
					ACLMessage msg = myAgent.receive(MessageTemplate.MatchConversationId("work-result"));
					if (msg != null) {
						ACLMessage result = getRequest().createReply();
						if (msg.getContent().compareTo("success") == 0) {
							result.setPerformative(ACLMessage.INFORM);
							System.out.println("send inform");
						} else {
							result.setPerformative(ACLMessage.FAILURE);
							System.out.println("send failure");
						}
						setResult(result);
						isMessageSent = true;
						status.stop();
						deadline.stop();
					} else {
						block();
					}
				}

				@Override
				public boolean done() {
					return isMessageSent;
				}

				private static final long serialVersionUID = 4747926179706803322L;
			}

			private class Deadline extends WakerBehaviour {
				public Deadline() {
					super(null, 5000);
				}

				@Override
				protected void onWake() {
					System.out.println("operation execution success");
					ACLMessage msg = new ACLMessage(ACLMessage.FAILURE);
					msg.addReceiver(getAgent().getAID());
					msg.setConversationId("work-result");
					msg.setContent("failure");
					send(msg);
				}

				private static final long serialVersionUID = -7549844752378583516L;
			}

			private static final long serialVersionUID = -2858297295800555581L;
		}

		private static final long serialVersionUID = 632163594464739296L;
	}

	private static final long serialVersionUID = -3430755832196648259L;
}
