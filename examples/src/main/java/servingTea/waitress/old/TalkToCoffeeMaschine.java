package servingTea.waitress.old;
/*package waitress;

import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;


class TalkToCoffeeMaschine extends WakerBehaviour {
		public TalkToCoffeeMaschine(Agent a, long time, String string) {
			super(a, time);
		}

		protected void onWake() {
			String requestedAction = "ForwardOrder";
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("coffeemaschine"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			System.out.println("Waitress: Produce");
			myAgent.addBehaviour(new RequestToDoSomething(myAgent, msg));
		}

	}

class RequestToDoSomething extends AchieveREInitiator {
	public RequestToDoSomething(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Error: received refuse");
	}

	@Override
	protected void handleAgree(ACLMessage agree) {
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		//sreceivedMenu = inform.getContent();
		//if (sreceivedMenu.matches("none")) {
		//}else {
		//System.out.println("Customer: Thank you for the menu. This is what is offered: "+ sreceivedMenu);
		//DecideForItem(sreceivedMenu);}
	}

	@Override
	protected void handleFailure(ACLMessage failure) {
		System.out.println("Error: received failure");
	}

}*/