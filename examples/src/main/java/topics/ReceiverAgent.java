package topics;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ReceiverAgent extends Agent {
	private static final long serialVersionUID = -2000635496947732364L;

	@Override
	protected void setup() {
		TopicManagementHelper topicHelper;
		AID jadeTopic = null;
		try {
			topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
			jadeTopic = topicHelper.createTopic("JADE");
			topicHelper.register(jadeTopic);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

		final MessageTemplate tpl = MessageTemplate.MatchTopic(jadeTopic);
		addBehaviour(new CyclicBehaviour() {
			private static final long serialVersionUID = 6000801404021958451L;

			@Override
			public void action() {
				ACLMessage msg = myAgent.receive(tpl);
				if (msg != null) {
					System.out.println(msg.getContent());
				} else {
					block();
				}
			}
		});
	}
}
