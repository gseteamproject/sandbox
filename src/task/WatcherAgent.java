package task;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class WatcherAgent extends Agent {
	
	String news[] = new String[5];{
	news[0] = "В Питере сегодня солнечно. Ха-ха, нет.";
	news[1] = "На улице гололёд, будьте осторожны.";
	news[2] = "Биткоин пробил дно!!!";
	news[3] = "Новость, которую мне было лень придумывать :)";
	news[4] = "На самом деле вот та новость, которую мне было лень придумывать";
	}
	
	int message;

	private static final long serialVersionUID = 7905485865837911637L;
	
	protected void setup() {
		addBehaviour(new ListeningBehavior2());
	}
	
	class ListeningBehavior2 extends CyclicBehaviour{

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
