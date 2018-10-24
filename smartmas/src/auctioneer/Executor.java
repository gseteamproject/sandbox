package auctioneer;

import core.JadeExecutor;

public class Executor {
	
	public static final String AgentName = "robot";
	
	public static void main(String[] args) {
		JadeExecutor.runRemote(AgentName, AuctioneerAgent.class);
	}
}
