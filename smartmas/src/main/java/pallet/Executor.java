package pallet;

import core.JadeExecutor;

public class Executor {
	
	public static final String AgentName = "pallet";
	
	public static void main(String[] args) {
		JadeExecutor.runRemote(AgentName, SourcePalletAgent.class);
	}
}
