package knowledge;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public abstract class KnowledgeAgent extends Agent {

	private static final long serialVersionUID = 1741839385173137863L;

	abstract protected void initializeBehaviours();

	abstract protected void initializeData();

	@Override
	protected void setup() {
		initializeData();
		initializeBehaviours();
		registerServices();
	}

	@Override
	protected void takeDown() {
		deregisterServices();
	}

	abstract protected ServiceDescription[] getAgentServiceDescriptions();

	private DFAgentDescription getDFAgentDescription() {
		ServiceDescription[] agentServices = getAgentServiceDescriptions();
		DFAgentDescription agentServicesDescription = new DFAgentDescription();
		agentServicesDescription.setName(getAID());
		for (ServiceDescription agentService : agentServices) {
			agentServicesDescription.addServices(agentService);
		}
		return agentServicesDescription;
	}

	private void registerServices() {
		try {
			DFService.register(this, getDFAgentDescription());
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
	}

	private void deregisterServices() {
		try {
			DFService.deregister(this);
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
	}

	public AID[] findAgentsProvidingServices(ServiceDescription[] requiredServices) {
		DFAgentDescription agentDescriptionTemplate = new DFAgentDescription();
		for (ServiceDescription requiredService : requiredServices) {
			agentDescriptionTemplate.addServices(requiredService);
		}
		AID[] foundAgents = null;
		try {
			DFAgentDescription[] agentsProvidingServices = DFService.search(this, agentDescriptionTemplate);
			foundAgents = new AID[agentsProvidingServices.length];
			for (int i = 0; i < agentsProvidingServices.length; i++) {
				foundAgents[i] = agentsProvidingServices[i].getName();
			}
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
		return foundAgents;
	}

	synchronized public void trace(String p_message) {
		System.out.println(getAID().getName() + ": " + p_message);
	}

	synchronized public void notUnderStood(ACLMessage message) {
		addBehaviour(new NotUnderstoodBehaviour(message));
	}
}
