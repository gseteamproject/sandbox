package knowledge.consumer;

import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;

public class KnowledgeConsumerAgent extends KnowledgeAgent {
	
	private static final long serialVersionUID = 1069538812627168203L;

	@Override
	protected void initializeBehaviours() {
	}

	@Override
	protected void initializeData() {
	}

	@Override
	protected ServiceDescription[] getAgentServiceDescriptions() {
		ServiceDescription agentServices[] = new ServiceDescription[1];
		agentServices[0] = new ServiceDescription();
		agentServices[0].setName(Knowledge.KNOWLEDGE_SERVICES);
		agentServices[0].setType(Knowledge.KNOWLEDGE_SERVICE_CONSUMING);
		return agentServices;
	}
}
