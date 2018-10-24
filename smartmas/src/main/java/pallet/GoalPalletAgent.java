package pallet;

import core.Position;
import core.passive.AbstractPassiveAgent;

public class GoalPalletAgent extends AbstractPassiveAgent {

	private Position _position;

	public GoalPalletAgent() {
		_position = new Position("RGB");
	}

	@Override
	public Position getPosition() {
		return _position;
	}

	private static final long serialVersionUID = 1770882580763337705L;
}
