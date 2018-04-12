package mapRunner.map.path;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;

public class Path implements Concept {
	private static final long serialVersionUID = 5010213474912833311L;

	public List points = new ArrayList();

	public List getPoints() {
		return points;
	}

	public void setPoints(List points) {
		this.points = points;
	}

	public void addPoint(int command, int amount, String name) {
		Point point = new Point();
		point.command = command;
		point.amount = amount;
		point.name = name;
		points.add(point);
	}
}
