package topics;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.TickerBehaviour;
import jade.core.messaging.TopicManagementHelper;
import jade.lang.acl.ACLMessage;

public class InitiatorAgent extends Agent {
	private static final long serialVersionUID = 8164567955691584565L;

	@Override
	protected void setup() {
		addBehaviour(new TickerBehaviour(null, 1000) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onTick() {
				TopicManagementHelper topicHelper;
				try {
					topicHelper = (TopicManagementHelper) getHelper(TopicManagementHelper.SERVICE_NAME);
					AID jadeTopic = topicHelper.createTopic("JADE");
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(jadeTopic);
					msg.setContent("JADE is a fantastic framework");
					send(msg);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
