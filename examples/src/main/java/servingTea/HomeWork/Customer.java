package servingTea.HomeWork;

import java.util.ArrayList;
import java.util.Date;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

import java.util.Random;

public class Customer extends Agent {
	ArrayList<String> receivedMenu = new ArrayList<String>();
	String sreceivedMenu;
	String choice= "HotWater";

	@Override
	protected void setup() {
		addBehaviour(new MenuRequest(this));

	}

	class MenuRequest extends OneShotBehaviour {
		public MenuRequest(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			String requestedAction = "RequestMenu";
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("waitress"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			System.out.println("Customer      : Asking for Menu");
			myAgent.addBehaviour(new RequestToDoSomethingNow(myAgent, msg));
		}

	}

	class ItemRequest extends OneShotBehaviour {
		public ItemRequest(String choice) {
			super();
		}

		public void action() {
			String requestedItem = choice;
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("waitress"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedItem);
			myAgent.addBehaviour(new RequestToDoSomethingNow(myAgent, msg));
			if (requestedItem.toString() != "none") {
				System.out.println("Customer      : My choice is: " + requestedItem);
			}
		}
	}
	class RequestToDoSomethingNow extends AchieveREInitiator {
		public RequestToDoSomethingNow(Agent a, ACLMessage msg) {
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
			sreceivedMenu = inform.getContent();
			System.out.println("Customer      : Thank you for the menu. This is what is offered: " + sreceivedMenu);
			DecideForItem(sreceivedMenu);
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("Error: received failure");
		}

	}

	public void DecideForItem(String receivedMenu2) {
		addBehaviour(new ItemRequest(choice));
	}
	
	//#######################################################
}
