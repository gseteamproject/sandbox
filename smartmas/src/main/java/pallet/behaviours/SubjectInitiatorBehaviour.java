package pallet.behaviours;

import core.subject.AbstractSubjectAgent;
import jade.core.behaviours.TickerBehaviour;
import pallet.SourcePalletAgent;

public class SubjectInitiatorBehaviour extends TickerBehaviour {
	
	private Class<? extends AbstractSubjectAgent> _subject;
	
	public SubjectInitiatorBehaviour (SourcePalletAgent palletAgent, Class<? extends AbstractSubjectAgent> subject, long period){
		super(palletAgent, period);
		
		_subject = subject;
	}

	@Override
	protected void onTick() {
		try {
			AbstractSubjectAgent agentInstance = _subject.newInstance();
			agentInstance.Initiate();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = 3602144538151140397L;
}
