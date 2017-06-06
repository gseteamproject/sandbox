package knowledge.consumer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;

public class FindFactsBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -1895404937198381633L;

	public FindFactsBehaviour(Agent a, long period) {
		super(a, period);
	}

	@Override
	protected void onTick() {
		KnowledgeConsumerAgent myKnowledgeConsumerAgent = (KnowledgeConsumerAgent) myAgent;
		if (myKnowledgeConsumerAgent.questions.size() == 0) {
			myKnowledgeConsumerAgent.trace("no questions to find");
			myKnowledgeConsumerAgent.doDelete();
		}
		AID[] knowledgeProcessors = findKnowledgeProcessors();
		if (knowledgeProcessors != null) {
			myKnowledgeConsumerAgent.findFact(knowledgeProcessors);
		} else {
			myKnowledgeConsumerAgent.trace("no active knowledge-processors");
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
