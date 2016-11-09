package palleteRobotCommunication.domain;

import jade.content.Concept;

public class Pallete implements Concept {

	private static final long serialVersionUID = 6518481924822268777L;

	private int maxCapacity;
	private int capacity;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}
}
