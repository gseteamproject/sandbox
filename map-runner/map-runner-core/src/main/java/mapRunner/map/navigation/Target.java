package mapRunner.map.navigation;

import jade.content.Concept;
import mapRunner.map.Point;

public class Target implements Concept {
	private static final long serialVersionUID = -8957734323774814994L;

	public Point destination = new Point();

	public Point getDestination() {
		return destination;
	}

	public void setDestination(Point point) {
		this.destination = point;
	}

	public Point location = new Point();

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point point) {
		this.location = point;
	}
}
