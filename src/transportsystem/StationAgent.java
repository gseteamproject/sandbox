package transportsystem;

import jade.core.Agent;

public class StationAgent extends Agent {

	
	private static final long serialVersionUID = -6446547881456768836L;
	public String nextStationName;
	
	protected void setup()
	{
		addBehaviour(new StationAgentBehaviour());
		Object[] args = getArguments();
		if(args!=null && args.length>0)
		{
			nextStationName = args[0].toString();
		}
		else
		{
			nextStationName = null;
		}
	}
}
