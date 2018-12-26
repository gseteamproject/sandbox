package task;

import java.util.Random;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class TellerAgent extends Agent {

	String conversationAgentName;

	long tickerPeriod;

	Random randomNumber = new Random(5);

	String gottedMessage = "5";

	String messageToSend = "5";
	
	MessageTemplate messageTemplate = MessageTemplate.MatchConversationId("1234");

	private static final long serialVersionUID = 1832882011224052171L;

	@Override
	protected void setup() {

		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			conversationAgentName = args[0].toString();
			tickerPeriod = Long.parseLong(args[1].toString());
		} else {
			conversationAgentName = null;
		}

		addBehaviour(new TellingBehavior(this, tickerPeriod));
		addBehaviour(new ListeningBehavior());
	}

	class TellingBehavior extends TickerBehaviour {
		
		private static final long serialVersionUID = 2440456042860733818L;

		public TellingBehavior(Agent a, long period) {
			super(a, period);
		}

		@Override
		protected void onTick() {
			messageToSend = String.valueOf(randomNumber.nextInt(5));
			if (Long.parseLong(messageToSend) == Long.parseLong(gottedMessage)) {
								
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID(("Boss"), AID.ISLOCALNAME));
				msg.setConversationId("1234");
				msg.setContent(messageToSend);
				send(msg);
			}
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID((conversationAgentName), AID.ISLOCALNAME));
			msg.setConversationId("1234");
			msg.setContent(messageToSend);
			send(msg);
			tickerPeriod = 1000;
		}

	}

	class ListeningBehavior extends CyclicBehaviour {

		private static final long serialVersionUID = 5624499704245954673L;
		

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null)  {
				gottedMessage = msg.getContent();
			} else {
				block();
			}

		}

	}
}
