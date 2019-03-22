package mapRunner.map;

import jade.content.Predicate;

public class RunnerLocation implements Predicate {
	private static final long serialVersionUID = -6906958044706475317L;

	private String runner;

	public void setRunner(String runner) {
		this.runner = runner;
	}

	public String getRunner() {
		return runner;
	}

	public Point point = new Point();

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	// Direction robot is facing to
	/*
	 * 0 - forward 
	 * 1 - right 
	 * 2 - back 
	 * 3 - left
	 */
	public int direction;

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
