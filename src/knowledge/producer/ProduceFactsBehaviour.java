package knowledge.producer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

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
		AID[] knowledgeProcessors = myKnowledgeProducerAgent.findKnowledgeProcessors();
		if (knowledgeProcessors != null) {
			myKnowledgeProducerAgent.produceFact(knowledgeProcessors);
		} else {
			myKnowledgeProducerAgent.trace("no active knowledge-processors");
		}
	}
}
