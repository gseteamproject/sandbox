package mapRunner.map;

import mapRunner.map.path.Command;
import mapRunner.map.path.Path;
import mapRunner.map.target.Target;

public class Map {

	public Path getPath(Target target) {
		Path path = new Path();

		if (target.id.equalsIgnoreCase("A")) {
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
		} else if (target.id.equalsIgnoreCase("B")) {
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_LEFT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_LEFT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_LEFT_90_DEGREE, 1);
			path.addPoint(Command.FORWARD, 1);
			path.addPoint(Command.ROTATE_180_DEGREE, 1);
		} else if (target.id.equalsIgnoreCase("l")) {
			path.addPoint(Command.ROTATE_LEFT_90_DEGREE, 1);
		} else if (target.id.equalsIgnoreCase("r")) {
			path.addPoint(Command.ROTATE_RIGHT_90_DEGREE, 1);
		} else if (target.id.equalsIgnoreCase("f")) {
			path.addPoint(Command.FORWARD, 1);
		}
		return path;
	}
}
