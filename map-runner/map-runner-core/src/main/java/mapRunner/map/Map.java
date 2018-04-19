package mapRunner.map;

import mapRunner.map.navigation.NavigationCommandType;
import mapRunner.map.navigation.Target;
import mapRunner.map.navigation.Navigation;

public class Map {
	
	//Map settings
	int lengthOfMap = 3;
	int widthOfMap = 3;
	int sizeOfGraph = lengthOfMap*widthOfMap;
	
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
	
	public void findingPath(int startPoint, int finishPoint) {
		int[] lengthOfWay = new int[sizeOfGraph];
		int[] previosPoint = new int[sizeOfGraph];
		boolean[] usedPoint = new boolean[sizeOfGraph];
		for (int i = 1; i < sizeOfGraph; i++) {
			lengthOfWay[i] = sizeOfGraph+100;
			previosPoint[i] = 0; 
			usedPoint[i] = true;
		}
		lengthOfWay[startPoint] = 0;
		while (true) {
			int lessWay = sizeOfGraph+100;
			int lessWayPoint = 0;
			
			for (int i = 1; i < sizeOfGraph; i++) {
				if ((lengthOfWay[i] < lessWay) & (usedPoint[i])) {
					lessWay = lengthOfWay[i];
					lessWayPoint = 0;
				}	
			}
			
			if (lessWayPoint == 0) {
				break;
			}
			
			usedPoint[lessWayPoint] = false;
			
			if (lessWayPoint - widthOfMap > 0) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint - widthOfMap]) {
					lengthOfWay[lessWayPoint - widthOfMap] = lengthOfWay[lessWayPoint] + 1;
					previosPoint[lessWayPoint - widthOfMap] = lessWayPoint;
				}
			}
			if (lessWayPoint + widthOfMap < sizeOfGraph) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint + widthOfMap]) {
					lengthOfWay[lessWayPoint + widthOfMap] = lengthOfWay[lessWayPoint] + 1;
					previosPoint[lessWayPoint + widthOfMap] = lessWayPoint;
				}
			}
			if (lessWayPoint % widthOfMap != 1) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint - 1]) {
					lengthOfWay[lessWayPoint - 1] = lengthOfWay[lessWayPoint] + 1;
					previosPoint[lessWayPoint - 1] = lessWayPoint;
				}
			}
			if (lessWayPoint % widthOfMap != 0) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint + 1]) {
					lengthOfWay[lessWayPoint + 1] = lengthOfWay[lessWayPoint] + 1;
					previosPoint[lessWayPoint + 1] = lessWayPoint;
				}
			}
		}
	//making path
	/*	
	int sizeOfWay = 0;
	int[] way = new int[sizeOfGraph];
	int consideredPoint = finishPoint;
	while (consideredPoint != 0) {
		sizeOfWay++;
		way[sizeOfWay] = consideredPoint;
		consideredPoint = previosPoint[consideredPoint];
	}
	int direction = 1;
	for (int i = 1; i < sizeOfWay; i++) {
		if (way[i] - way[i + 1] == 1) {
			switch (direction) {
			case 1: path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "SomeLocation");
			case 2: path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "SomeLocation");
			case 3: path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "SomeLocation");
			}
			direction = 0;
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "SomeLocation");
		}
		if (way[i] - way[i + 1] == -1) {
			switch (direction) {
			case 3: path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "SomeLocation");
			case 0: path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "SomeLocation");
			case 1: path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "SomeLocation");
			}
			direction = 2;
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "SomeLocation");
		}
		if (way[i] - way[i + 1] == -widthOfMap) {
			switch (direction) {
			case 2: path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "SomeLocation");
			case 3: path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "SomeLocation");
			case 0: path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "SomeLocation");
			}
			direction = 1;
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "SomeLocation");
		}
		if (way[i] - way[i + 1] == widthOfMap) {
			switch (direction) {
			case 2: path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "SomeLocation");
			case 1: path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "SomeLocation");
			case 0: path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "SomeLocation");
			}
			direction = 3;
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, "SomeLocation");
		}
	}
	switch (direction) {
	case 2: path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, "SomeLocation");
	case 3: path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, "SomeLocation");
	case 0: path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, "SomeLocation");
	}
	*/
	//TODO: make return path	
	}
}
