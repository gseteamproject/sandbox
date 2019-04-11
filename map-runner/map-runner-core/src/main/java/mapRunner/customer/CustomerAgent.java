package mapRunner.customer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.ontology.Vocabulary;

public class CustomerAgent extends Agent {

	private static final long serialVersionUID = 4486738805857696330L;

	CustomerView view = new CustomerView();

	@Override
	protected void setup() {
		view.agent = this;

		displayForm();
		addBehaviour(new HandleStatusReportsBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());
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
	
	public void sendTargetToRunner(String agent, String targetStart, String targetFinish) {
        SendTargetBehaviour b = new SendTargetBehaviour();
        b.agent = agent;
        b.targetStart = targetStart;
        b.targetFinish = targetFinish;
        addBehaviour(b);
    }

	class SendTargetBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -3289831867204027539L;

		String agent;

		String targetStart;		
        String targetFinish;

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((agent), AID.ISLOCALNAME));
			msg.setConversationId(Vocabulary.CONVERSATION_ID_TARGET);
			if (targetStart == null || targetStart.equals("")) {
	            msg.setContent(targetFinish);
			} else {
                msg.setContent(targetStart + "," + targetFinish);			    
			}
			send(msg);
		}
	}

	class HandleStatusReportsBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -8458168586548894275L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_TARGET);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
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
			} else {
				block();
			}
		}
	}
}
