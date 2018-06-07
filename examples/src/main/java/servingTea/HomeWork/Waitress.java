package servingTea.HomeWork;

import java.util.ArrayList;
import java.util.Date;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;

public class Waitress extends Agent {
	String Order;
	String CustomerRequest = "none";
	ArrayList<String> menuForCustomer = new ArrayList<String>();

	protected void setup() {
		MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		addBehaviour(new RespondToCustomer(this, template, menuForCustomer));
	}

	// ############################################Give Menu and Get Order to/from
	// Customer#########################

	public class RespondToCustomer extends AchieveREResponder {
		public RespondToCustomer(Waitress waitress, MessageTemplate mt) {
			super(waitress, mt);
		}

		public RespondToCustomer(Agent a, MessageTemplate mt, ArrayList<String> menuForCustomer) {
			super(a, mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
			// send AGREE
			CustomerRequest = request.getContent();
			ACLMessage agree = request.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
				throws FailureException {
			ACLMessage inform = request.createReply();
			switch (CustomerRequest.toString()) {
			case "RequestMenu":
				inform.setContent("Menue: [0] Tea, [1] Coffee, [2] Hot Water");
				inform.setPerformative(ACLMessage.INFORM);
				System.out.println("Waitress      : This is the menu: [0] Tea, [1] Coffee, [2] Hot Water");
				break;
			case "Tea":
				Order = CustomerRequest.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Order);
				RequestItemFromCoffeeMaschine(Order);
				break;
			case "Coffee":
				Order = CustomerRequest.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Order);
				RequestItemFromCoffeeMaschine(Order);
				break;
			case "HotWater":
				Order = CustomerRequest.toString();
				System.out.println("Waitress      : Dear Customer, you will get " + Order);
				RequestItemFromCoffeeMaschine(Order);
				break;
			default:
				System.out.println("------>Waitress was fired!");
			}

			return inform;
		}

		private static final long serialVersionUID = 3926236008954757536L;
	}

	// ********************************************************************************************
	// ############################################Order to
	// CoffeeMaschine#######################
	// ********************************************************************************************
	public void RequestItemFromCoffeeMaschine(String OrderedItem) {
		addBehaviour(new RequestPreparation(this));
	}

	class RequestPreparation extends OneShotBehaviour {
		public RequestPreparation(Agent a) {
			super(a);
		}

		@Override
		public void action() {
			String requestedAction = "Prep" + Order;
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("coffeemaschine"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			System.out.println("#######################################################");
			System.out.println("Waitress      : Coffee Maschine please prepare " + Order);
			myAgent.addBehaviour(new RequestToDoSomethingNow(myAgent, msg));
		}

		private static final long serialVersionUID = 8950879219288894025L;
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
			System.out.println("Waitress      : Received Item from coffee maschine");
			System.out.println("#######################################################");
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("Error: received failure");
		}

		private static final long serialVersionUID = 2776615775411097278L;
	}

	private static final long serialVersionUID = -9079363306709279543L;
}
// ############################################ITEM to
// Customer#########################
