package transportsystem;

import jade.core.Agent;

public class ShuttleAgent extends Agent{

	private static final long serialVersionUID = -5137534167080817246L;
	
	protected void setup()
	{
		addBehaviour(new ShuttleAgentBehaviour());
	}


}
