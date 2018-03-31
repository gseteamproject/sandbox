package knowledge.consumer;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

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
		AID[] knowledgeProcessors = myKnowledgeConsumerAgent.findKnowledgeProcessors();
		if (knowledgeProcessors != null) {
			myKnowledgeConsumerAgent.findFact(knowledgeProcessors);
		} else {
			myKnowledgeConsumerAgent.trace("no active knowledge-processors");
		}
	}
}
