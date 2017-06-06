package knowledge.producer;

import java.util.ArrayList;
import java.util.List;

import jade.core.Agent;

public class KnowledgeProducerAgent extends Agent {

	private static final long serialVersionUID = -8333173138628880437L;

	private List<String> factsToProduce = new ArrayList<String>();

	@Override
	protected void setup() {
		Object[] args = getArguments();
		for (Object arg : args) {
			factsToProduce.add(arg.toString());
		}

		addBehaviour(new ProduceFactsBehaviour(this, 2000));
	}
}
