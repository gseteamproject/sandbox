package core.passive;

import core.Position;
import core.passive.behaviours.InformatorBehaviour;
import jade.core.Agent;

public abstract class AbstractPassiveAgent extends Agent {

	protected void setup() {
		addBehaviour(new InformatorBehaviour(this));
	}

	public abstract Position getPosition();

	private static final long serialVersionUID = 7164629106344932240L;
}
