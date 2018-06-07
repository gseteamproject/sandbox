package servingTea.HomeWork;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class Robot_Agent extends Agent {

private static final long serialVersionUID = -7418692714860762106L;
protected String serviceName;

	public Robot_Agent() {
	// TODO Auto-generated constructor stub
}

	@Override
	protected void setup() {
		ServiceDescription serviceDescription = new ServiceDescription();
		serviceDescription.setName(serviceName);
		serviceDescription.setType("Coffee-machine");
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(getAID());
		agentDescription.addServices(serviceDescription);
		try {
			DFService.register(this, agentDescription);
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}

		MessageTemplate template = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		addBehaviour(new RobotResponder(this, template));
	}

	@Override
	protected void takeDown() {
		try {
			DFService.deregister(this);
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
	}


	class RobotResponder extends AchieveREResponder {
		public RobotResponder(Agent a, MessageTemplate mt) {
			super(a, mt);
		}

		@Override
		protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {

			return null;
		}

		@Override
		protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
				throws FailureException {
			ACLMessage inform = request.createReply();
			inform.setContent("[inform] Ended with " + serviceName);
			inform.setPerformative(ACLMessage.INFORM);
			return inform;
		}

		private static final long serialVersionUID = -8009542545033008746L;
	}

}