package transportsystem;

import jade.core.Agent;

public class TransportLineAgent extends Agent {

	
	private static final long serialVersionUID = -3331126208966986144L;
	
	protected void setup()
	{
		addBehaviour(new TransportLineBehaviour());
	}

}
