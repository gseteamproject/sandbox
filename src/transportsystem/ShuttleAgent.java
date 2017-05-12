package transportsystem;

import jade.core.Agent;

public class ShuttleAgent extends Agent{

	private static final long serialVersionUID = -5137534167080817246L;
	public String ownerStationName;
	protected void setup()
	{
		addBehaviour(new ShuttleAgentBehaviour());
		Object[] args = getArguments();
		if(args!=null && args.length>0)
		{
			ownerStationName = args[0].toString();
		}
		else
		{
			ownerStationName = null;
		}
	}


}
