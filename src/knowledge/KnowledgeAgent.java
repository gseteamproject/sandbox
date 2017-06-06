package knowledge;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public abstract class KnowledgeAgent extends Agent {

	private static final long serialVersionUID = 1741839385173137863L;

	abstract protected void initializeBehaviours();

	@Override
	protected void setup() {
		initializeBehaviours();
		registerServices();
	}

	@Override
	protected void takeDown() {
		deregisterServices();
	}

	abstract protected ServiceDescription[] getAgentServiceDescriptions();

	private void registerServices() {
		ServiceDescription[] agentServices = getAgentServiceDescriptions();
		DFAgentDescription agentServicesDescription = new DFAgentDescription();
		agentServicesDescription.setName(getAID());
		for (ServiceDescription agentService : agentServices) {
			agentServicesDescription.addServices(agentService);
		}
		try {
			DFService.register(this, agentServicesDescription);
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

	public void trace(String p_message) {
		System.out.println(getAID().getName() + ": " + p_message);
	}
}
