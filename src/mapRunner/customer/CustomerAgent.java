package mapRunner.customer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class CustomerAgent extends Agent {

	private static final long serialVersionUID = 4486738805857696330L;

	CustomerView view = new CustomerView();

	@Override
	protected void setup() {
		view.agent = this;

		displayForm();
		addBehaviour(new HandleStatusReportsBehaviour());
	}

	@Override
	protected void takeDown() {
		hideForm();
	}

	private void displayForm() {
		view.setVisible(true);
	}

	private void hideForm() {
		view.dispose();
	}

	public void sendTargetToRunner(String agent, String target) {
		SendTargetBehaviour b = new SendTargetBehaviour();
		b.agent = agent;
		b.target = target;
		addBehaviour(b);
	}

	class SendTargetBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -3289831867204027539L;

		String agent;

		String target;

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((agent), AID.ISLOCALNAME));
			msg.setConversationId("target");
			msg.setContent(target);
			send(msg);
		}
	}

	class HandleStatusReportsBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -8458168586548894275L;

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				if (isConversationAboutTarget(msg)) {
					switch (msg.getPerformative()) {
					case ACLMessage.AGREE:
						view.showStatus("moving");
						break;
					case ACLMessage.INFORM:
						view.showStatus("done");
						break;
					case ACLMessage.CANCEL:
						view.showStatus("busy");
						break;
					default:
						view.showStatus("error");
						break;
					}
				}
			} else {
				block();
			}
		}

		private boolean isConversationAboutTarget(ACLMessage msg) {
			String theme = msg.getConversationId();
			return theme != null && msg.getConversationId().compareTo("target") == 0;
		}
	}
}
