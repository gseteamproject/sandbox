package mapRunner.map;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.ontology.Vocabulary;

public class MapAgent extends Agent {

	private static final long serialVersionUID = -2245739743003700621L;

	@Override
	protected void setup() {
		addBehaviour(new PlanPathBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());
	}

	class PlanPathBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -5851628986020982760L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_PATH);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
				planPath(msg);
			} else {
				block();
			}
		}

		private void planPath(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			String target = msg.getContent();
			int path = getPath(target);
			reply.setContent(String.valueOf(path));
			myAgent.send(reply);
		}

		private int getPath(String target) {
			if (target.equalsIgnoreCase("A")) {
				return 1;
			} else if (target.equalsIgnoreCase("B")) {
				return 2;
			} else if (target.equalsIgnoreCase("C")) {
				return 3;
			}
			return 0;
		}
	}
}
