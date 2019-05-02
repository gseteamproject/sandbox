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
	public static final int FORWARD = 0;
    public static final int LEFT = 1;
    public static final int BACK = 2;
    public static final int RIGHT = 3;
    
	public int direction;

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
    
    public float angle;

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
