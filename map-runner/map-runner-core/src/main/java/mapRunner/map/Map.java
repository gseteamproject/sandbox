package mapRunner.map;

import mapRunner.map.navigation.NavigationCommandType;
import mapRunner.map.navigation.Target;
import mapRunner.map.navigation.Navigation;

public class Map {

	public Navigation getPath(Target target) {
		Navigation path = new Navigation();

		String targetName = target.getDestination().getName();

		if (targetName.equalsIgnoreCase("A")) {
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "1");
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "1");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "2");
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "2");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "4");
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "4");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "3");
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "3");
		} else if (targetName.equalsIgnoreCase("B")) {
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "3");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "4");
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "4");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "2");
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "2");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "1");
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "1");
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "3");
			path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "3");
		} else if (targetName.equalsIgnoreCase("l")) {
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "3");
		} else if (targetName.equalsIgnoreCase("r")) {
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "3");
		} else if (targetName.equalsIgnoreCase("f")) {
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "3");
		}
		return path;
	}

	public void updateLocation(RunnerLocation location) {
		System.out.println(
				String.format("Runner \"%s\" at point \"%s\"", location.getRunner(), location.getPoint().name));
	}
}
