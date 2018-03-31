package mapRunner.map.path;

import jade.content.Concept;

public class Point implements Concept {
	private static final long serialVersionUID = -8051944910031757970L;

	public int command;

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int amount;

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
}
