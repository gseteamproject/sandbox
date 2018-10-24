package core.subject;

import core.JadeExecutor;
import jade.core.Agent;

public abstract class AbstractSubjectAgent extends Agent {
	
	public AbstractSubjectAgent(){
	}
	
	public void Initiate(){
		JadeExecutor.runRemote(getDescription(), this.getClass());
	}
	
	public abstract String getDescription();
	
	private static final long serialVersionUID = -5743235354994663853L;
}
