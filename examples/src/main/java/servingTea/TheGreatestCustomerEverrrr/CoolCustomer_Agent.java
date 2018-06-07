package servingTea.TheGreatestCustomerEverrrr;

import java.util.Date;

import servingTea.commonFunctionality.CommonAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.FIPANames;
import jade.domain.introspection.AddedBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import servingTea.commonFunctionality.CommonAgent;

public class CoolCustomer_Agent extends Agent {
	
	@Override
	protected void setup() {
		addBehaviour (new MenuRequest(this, 2000));
		
	}
	
	class MenuRequest extends WakerBehaviour {
		public MenuRequest(Agent a, long time) {
			super(a, time);
		}
		
		@Override
		protected void onWake() {
			String requestedAction = "RequestMenu";

			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("waitress"), AID.ISLOCALNAME));
			msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
			msg.setContent(requestedAction);
			
			myAgent.addBehaviour(new RequestToDoSomething(myAgent, msg));
		}
		
	}
	
	class RequestToDoSomething extends AchieveREInitiator {
		public RequestToDoSomething(Agent a, ACLMessage msg) {
			super(a, msg);
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			System.out.println("received refuse");
		}

		@Override
		protected void handleAgree(ACLMessage agree) {
			System.out.println("received agree");
		}

		@Override
		protected void handleInform(ACLMessage inform) {
			System.out.println("received inform");
		}

		@Override
		protected void handleFailure(ACLMessage failure) {
			System.out.println("received failure");
		}

	}
	

}

