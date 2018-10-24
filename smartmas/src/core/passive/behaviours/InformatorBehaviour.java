package core.passive.behaviours;

import java.io.IOException;
import jade.domain.DFService;
import jade.domain.FIPAException;
import core.passive.AbstractPassiveAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InformatorBehaviour extends CyclicBehaviour {
	
	private AbstractPassiveAgent _passiveAgent;
	
	public InformatorBehaviour(AbstractPassiveAgent agent){
		_passiveAgent = agent;
		
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(_passiveAgent.getAID());

		ServiceDescription serviceDescription = new ServiceDescription();		
		serviceDescription.setName("Informator");
		serviceDescription.setType("Position");
		
		agentDescription.addServices(serviceDescription);
		
		try {
			DFService.register(agent, agentDescription);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void action() {		
		MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
		ACLMessage receivedMsg = myAgent.receive(messageTemplate);
		
		if(receivedMsg != null)
		{
			//String title = receivedMsg.getContent();
			ACLMessage replyMsg = receivedMsg.createReply();

			replyMsg.setPerformative(ACLMessage.PROPOSE);
			try {
				replyMsg.setContentObject(_passiveAgent.getPosition());
			} catch (IOException e) {
				e.printStackTrace();
			}
			myAgent.send(replyMsg);
		}
		else
		{
			block();
		}
	}
	
	private static final long serialVersionUID = -1175486215251793563L;
}
