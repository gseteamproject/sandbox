package mapRunner.map;

import jade.content.Predicate;
import mapRunner.map.path.Path;
import mapRunner.map.target.Target;

public class PathToTarget implements Predicate {
	private static final long serialVersionUID = 2914983717229055059L;

	private Target target = new Target();

	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	private Path path = new Path();

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
}
