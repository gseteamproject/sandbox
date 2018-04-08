package topics;

import jade.Boot;

public class RunScenario {

	public static void main(String[] args) {
		String[] parameters = new String[] {
			"-gui",
			"-services", "jade.core.messaging.TopicManagementService",
			"initiator:" + InitiatorAgent.class.getName() + ";"
					+ "responder1:" + ReceiverAgent.class.getName() + ";"
					+ "responder2:" + ReceiverAgent.class.getName() + ";"
		};
		Boot.main(parameters);
	}
}
