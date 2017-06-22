package resourceAllocation.workers;

import jade.core.Agent;

public class WorkerAgent extends Agent {

	private static final long serialVersionUID = -2959026186780910618L;

	@Override
    public void setup(){
        Object[] args = getArguments();

        if (args != null && args.length > 0) {
            long time = (long) Long.parseLong((String)args[0]);

            System.out.println("Agent " + getAID().getName() + " is ready to perform the process that lasts " + time + " seconds.");

            addBehaviour(new LookForProcessor(time));
        }
    }
}
