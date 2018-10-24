package block;

import java.util.UUID;
import core.subject.AbstractSubjectAgent;

public class BlockAgent extends AbstractSubjectAgent {
	
	private final String Description = "block";
	private State _state;
	
	public BlockAgent(){
		super();
		
		_state = State.Dirty;
	}
	
	protected void setup(){
		
	}
	
	public State getBlockState(){
		return _state;
	}

	@Override
	public String getDescription() {
		return Description + " " + UUID.randomUUID().toString();
	}

	private static final long serialVersionUID = 6488727540705658107L;
}
