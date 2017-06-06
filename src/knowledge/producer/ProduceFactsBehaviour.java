package knowledge.producer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;

public class ProduceFactsBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2935412867956508374L;

	public ProduceFactsBehaviour(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		KnowledgeProducerAgent myKnowledgeProducerAgent = (KnowledgeProducerAgent) myAgent;
		if (myKnowledgeProducerAgent.factsToProduce.size() == 0) {
			myKnowledgeProducerAgent.trace("no facts to produce");
			myKnowledgeProducerAgent.doDelete();
		}
		AID[] knowledgeProcessors = findKnowledgeProcessors();
		if (knowledgeProcessors != null) {
			myKnowledgeProducerAgent.produceFact(knowledgeProcessors);
		} else {
			myKnowledgeProducerAgent.trace("no active knowledge-processors");
		}
	}

	private AID[] findKnowledgeProcessors() {
		DFAgentDescription agentDescriptionTemplate = new DFAgentDescription();
		ServiceDescription requiredService = new ServiceDescription();
		requiredService.setType(Knowledge.KNOWLEDGE_SERVICE_PROCESSING);
		agentDescriptionTemplate.addServices(requiredService);

		AID[] sellerAgents = null;
		try {
			DFAgentDescription[] foundAgents = DFService.search(myAgent, agentDescriptionTemplate);
			sellerAgents = new AID[foundAgents.length];
			for (int i = 0; i < foundAgents.length; i++) {
				sellerAgents[i] = foundAgents[i].getName();
			}
		} catch (FIPAException exception) {
			exception.printStackTrace();
		}
		return sellerAgents;
	}
}
