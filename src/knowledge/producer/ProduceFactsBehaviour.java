package knowledge.producer;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class ProduceFactsBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = -2935412867956508374L;

	public ProduceFactsBehaviour(Agent a, long period) {
		super(a, period);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		myAgent.doDelete();
	}
}
