package mapRunner.map.navigation;

import jade.content.Concept;
import mapRunner.map.Point;

public class NavigationCommand implements Concept {
	private static final long serialVersionUID = -4015599765990462510L;

	public int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int quantity;

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Point point = new Point();

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}
}
