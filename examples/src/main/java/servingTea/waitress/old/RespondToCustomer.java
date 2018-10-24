package servingTea.waitress.old;
/*package waitress;

import java.util.ArrayList;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class RespondToCustomer extends AchieveREResponder {
public String CustomerOrder="none";
	String Conversation = "none";
	String dummy = "tea";
	ArrayList<Object> menuForCustomer = new ArrayList<Object>();

	public RespondToCustomer(Waitress waitress, MessageTemplate mt) {
		super(waitress, mt);
		// TODO Auto-generated constructor stub
	}

	public RespondToCustomer(Agent a, MessageTemplate mt, ArrayList<Object> menuForCustomer) {
		super(a, mt);
		this.menuForCustomer = menuForCustomer;
	}

	@Override
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		// send AGREE
		Conversation = request.getContent();
		ACLMessage agree = request.createReply();
		agree.setPerformative(ACLMessage.AGREE);
		return agree;
		// send REFUSE
		// throw new RefuseException("check-failed");
	}

	@Override
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
		// if agent AGREEd to request
		// send INFORM
		ACLMessage inform = request.createReply();
		switch (Conversation.toString()) {
		case "RequestMenu":
			inform.setContent(dummy);
			inform.setPerformative(ACLMessage.INFORM);
			System.out.println("Waitress: This is the menu:  "+ Conversation);
			break;
		case "tea":
			inform.setContent("none");
			inform.setPerformative(ACLMessage.INFORM);
			CustomerOrder= Conversation.toString();
			System.out.println("Waitress: Dear Customer, you will get "+ Conversation);
			
			break;
		default:
			System.out.println("------>Waitress was fired!");
		}

		return inform;
		// send FAILURE
		// throw new FailureException("unexpected-error");
	}

	private static final long serialVersionUID = -8009542545033008746L;
}
*/