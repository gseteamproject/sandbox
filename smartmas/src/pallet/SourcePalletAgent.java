package pallet;

import block.BlockAgent;
import core.Position;
import core.passive.AbstractPassiveAgent;
import pallet.behaviours.SubjectInitiatorBehaviour;

public class SourcePalletAgent extends AbstractPassiveAgent {
	
	private Position _position;
	
	public SourcePalletAgent(){
		_position = new Position("RGB");
	}
	
	protected void setup(){
		addBehaviour(new SubjectInitiatorBehaviour(this, BlockAgent.class, 5000));
	}

	@Override
	public Position getPosition() {
		return _position;
	}

	private static final long serialVersionUID = -8595956382919674316L;
}
