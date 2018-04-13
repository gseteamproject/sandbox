package mapRunner.map;

import jade.content.Concept;

public class Point implements Concept {
	private static final long serialVersionUID = -8051944910031757970L;

	public String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
