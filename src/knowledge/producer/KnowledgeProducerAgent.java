package knowledge.producer;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;

public class KnowledgeProducerAgent extends KnowledgeAgent {

	private static final long serialVersionUID = -8333173138628880437L;

	public List<String> factsToProduce = new ArrayList<String>();

	@Override
	protected void initializeBehaviours() {
		addBehaviour(new ProduceFactsBehaviour(this, 2000));
	}

	@Override
	protected void initializeData() {
		Object[] args = getArguments();
		for (Object arg : args) {
			factsToProduce.add(arg.toString());
			trace("got fact (" + arg.toString() + ")");
		}
	}

	@Override
	protected ServiceDescription[] getAgentServiceDescriptions() {
		ServiceDescription agentServices[] = new ServiceDescription[1];
		agentServices[0] = new ServiceDescription();
		agentServices[0].setName(Knowledge.KNOWLEDGE_SERVICES);
		agentServices[0].setType(Knowledge.KNOWLEDGE_SERVICE_PRODUCING);
		return agentServices;
	}

	synchronized public void produceFact(AID[] knowledgeProcessors) {
		String fact = factsToProduce.get(0);
		for(AID knowledgeProcessor: knowledgeProcessors) {
			addBehaviour(new ProduceFactBehaviour(knowledgeProcessor, fact));
		}
		factsToProduce.remove(0);
	}
}
