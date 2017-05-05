package transportsystem;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class TransportLineAgent extends Agent {

	
	private static final long serialVersionUID = -3331126208966986144L;
	
	int nShuttle = 3;
	protected void setup()
	{
		addBehaviour(new TransportLineBehaviour());
	}
		
		@SuppressWarnings("rawtypes") 
		Vector shuttleList = new Vector();	
		
		PlatformController container = getContainerController(); {
		
try {
for (int i = 0; i < nShuttle; i++) {
// create a new agent
String localName = "guest_" + i;
AgentController shuttle = container.createNewAgent(localName, "line.shuttleagent", null);
shuttle.start();

shuttleList.add(new AID(localName, AID.ISLOCALNAME));
}
} 
catch (Exception e) 
{
System.err.println("Exception while adding guests: " + e);
e.printStackTrace();
}
	}
}


