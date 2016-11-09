package palleteRobotCommunication.domain;

import jade.content.AgentAction;

public class WhatIsYourState implements AgentAction {

	private static final long serialVersionUID = 418155681200341499L;

	private State state;

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
