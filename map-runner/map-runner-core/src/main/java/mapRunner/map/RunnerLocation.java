package mapRunner.map;

import jade.content.Predicate;
import mapRunner.map.path.Point;

public class RunnerLocation implements Predicate {
	private static final long serialVersionUID = -6906958044706475317L;

	private Point point;

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	private String runner;

	public void setRunner(String runner) {
		this.runner = runner;
	}

	public String getRunner() {
		return runner;
	}
}
