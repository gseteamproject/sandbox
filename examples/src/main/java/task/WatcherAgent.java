package task;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class WatcherAgent extends Agent {

	String news[] = new String[] { "news 1", "news 2", "news 3", "news 4", "news 5" };

	int message;

	private static final long serialVersionUID = 7905485865837911637L;

	protected void setup() {
		addBehaviour(new ListeningBehavior2());
	}

	class ListeningBehavior2 extends CyclicBehaviour {

		private static final long serialVersionUID = -7960774253378262134L;

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				message = Integer.parseInt(msg.getContent());
				System.out.println(news[message]);
			} else {
				block();
			}

		}

	}

}
