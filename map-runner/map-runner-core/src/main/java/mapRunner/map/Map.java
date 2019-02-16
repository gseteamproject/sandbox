package mapRunner.map;

import mapRunner.map.navigation.NavigationCommandType;
import mapRunner.map.navigation.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapRunner.map.navigation.Navigation;

public class Map {
	
	//Map settings
	int lengthOfMap = 3;
	int widthOfMap = 3;
	int sizeOfGraph = lengthOfMap*widthOfMap;
	// Direction robot is facing to
	// It would be better to not to use this variable in Map since it should be a Runner parameter
	int manualDirection = 0;
	
	// K - runner name, V - list of parameters
	java.util.Map<String, List<Integer>> runnersData = new HashMap<String, List<Integer>>();
	
	public Navigation getPath(Target target, String runnerName) {
		Navigation path = new Navigation();
		
		// targetName is what we want runner to do
		String targetName = target.getDestination().getName();
		// currentPoint is the point where runner is now
		int currentPoint = Integer.parseInt(target.getLocation().getName());
		// nextPoint is the point where runner is going to be after executing this command
		String nextPoint = Integer.toString(currentPoint);
		
		try {
			manualDirection = runnersData.get(runnerName).get(1);
		} catch (NullPointerException e) {
			manualDirection = 0;
		}

		if (targetName.equalsIgnoreCase("A")) {
			nextPoint = Integer.toString(currentPoint - widthOfMap);
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, nextPoint);
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
			nextPoint = Integer.toString(currentPoint - widthOfMap + 1);
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, nextPoint);
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
			nextPoint = Integer.toString(currentPoint + 1);
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, nextPoint);
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
			nextPoint = Integer.toString(currentPoint);
			path.addNavigationCommand(NavigationCommandType.FORWARD, 1, nextPoint);
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
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
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, nextPoint);
			if (manualDirection != 0) {
				manualDirection -= 1;
			} else {
				manualDirection = 3;
			}
		} else if (targetName.equalsIgnoreCase("r")) {
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
			if (manualDirection != 3) {
				manualDirection += 1;
			} else {
				manualDirection = 0;
			}
		} else if (targetName.equalsIgnoreCase("f")) {
				switch (manualDirection) {
				case 0:
					if (currentPoint - widthOfMap > 0) {
						nextPoint = Integer.toString(currentPoint - widthOfMap);					
					}
					break;
				case 1:
					if ((currentPoint) % widthOfMap != 0) {
						nextPoint = Integer.toString(currentPoint + 1);				
					}
					break;
				case 2:
					if (currentPoint + widthOfMap <= sizeOfGraph) {
						nextPoint = Integer.toString(currentPoint + widthOfMap);					
					}
					break;
				case 3:
					if ((currentPoint) % widthOfMap != 1) {
						nextPoint = Integer.toString(currentPoint - 1);
					}
					break;
				}
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, nextPoint);				
		} else if ((Integer.parseInt(targetName) > 0) & (Integer.parseInt(targetName) <= sizeOfGraph)) {
			findingPath(path, currentPoint, Integer.parseInt(targetName));
		}
		// 0 - location point, 1 - direction
		List<Integer> runnerParameters = new ArrayList<Integer>();
		runnerParameters.add(Integer.parseInt(nextPoint));
		runnerParameters.add(manualDirection);
		runnersData.put(runnerName, runnerParameters);
		
		return path;
	}

	public void updateLocation(RunnerLocation location) {
		System.out.println(
				String.format("Runner \"%s\" at point \"%s\"", location.getRunner(), location.getPoint().name));
	}	
	
	//Dijkstra's algorithm
	public void findingPath(Navigation path, int startPoint, int finishPoint) {
		// array of lengths of paths from current point to each other 
		int[] lengthOfWay = new int[sizeOfGraph + 1];
		// array of point names that are previous in paths for each point
		int[] previousPoint = new int[sizeOfGraph + 1];
		boolean[] usedPoint = new boolean[sizeOfGraph + 1];
		for (int i = 0; i <= sizeOfGraph; i++) {
			lengthOfWay[i] = sizeOfGraph+100;
			previousPoint[i] = 0; 
			usedPoint[i] = true;
		}
		lengthOfWay[startPoint] = 0;
		while (true) {
			int lessWay = sizeOfGraph+10;
			int lessWayPoint = 0;
			
			for (int i = 1; i <= sizeOfGraph; i++) {
				if ((lengthOfWay[i] < lessWay) & (usedPoint[i])) {
					lessWay = lengthOfWay[i];
					lessWayPoint = i;
				}	
			}
			
			if (lessWayPoint == 0) {
				break;
			}
			usedPoint[lessWayPoint] = false;
			// fill lengths of paths to neighboring point in previous row
			if (lessWayPoint - widthOfMap > 0) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint - widthOfMap]) {
					lengthOfWay[lessWayPoint - widthOfMap] = lengthOfWay[lessWayPoint] + 1;
					previousPoint[lessWayPoint - widthOfMap] = lessWayPoint;
				}
			}
			// fill lengths of paths to neighboring point in next row
			if (lessWayPoint + widthOfMap < sizeOfGraph) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint + widthOfMap]) {
					lengthOfWay[lessWayPoint + widthOfMap] = lengthOfWay[lessWayPoint] + 1;
					previousPoint[lessWayPoint + widthOfMap] = lessWayPoint;
				}
			}
			// fill lengths of paths to neighboring point in previous column
			if (lessWayPoint % widthOfMap != 1) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint - 1]) {
					lengthOfWay[lessWayPoint - 1] = lengthOfWay[lessWayPoint] + 1;
					previousPoint[lessWayPoint - 1] = lessWayPoint;
				}
			}
			// fill lengths of paths to neighboring point in next column
			if (lessWayPoint % widthOfMap != 0) {
				if (lengthOfWay[lessWayPoint] + 1 < lengthOfWay[lessWayPoint + 1]) {
					lengthOfWay[lessWayPoint + 1] = lengthOfWay[lessWayPoint] + 1;
					previousPoint[lessWayPoint + 1] = lessWayPoint;
				}
			}
		}
	//making path
		
	int sizeOfWay = 0;
	int[] way = new int[sizeOfGraph + 1];
	int consideredPoint = finishPoint;
	while (consideredPoint != 0) {
		sizeOfWay++;
		way[sizeOfWay] = consideredPoint;
		consideredPoint = previousPoint[consideredPoint];
	}
	
	// Direction robot is facing to
	/*
	 * 0 - forward
	 * 1 - right
	 * 2 - back
	 * 3 - left
	 */
	int direction = 0;
		for (int i = sizeOfWay; i > 1; i--) {
			String nextPoint = Integer.toString(way[i]);
			// if next point is at the left side of the current one
			if (way[i] - way[i - 1] == 1) {
				switch (direction) {
				case 0:
					path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, nextPoint);
					break;
				case 1:
					path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, nextPoint);
					break;
				case 2:
					path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
					break;
				}
				direction = 3;
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(way[i - 1]));
			}
			// if next point is at the right side of the current one
			if (way[i] - way[i - 1] == -1) {
				switch (direction) {
				case 2:
					path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, nextPoint);
					break;
				case 3:
					path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, nextPoint);
					break;
				case 0:
					path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
					break;
				}
				direction = 1;
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(way[i - 1]));
			}
			// if next point is in front of the current one
			if (way[i] - way[i - 1] == widthOfMap) {
				switch (direction) {
				case 1:
					path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, nextPoint);
					break;
				case 2:
					path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, nextPoint);
					break;
				case 3:
					path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
					break;
				}
				direction = 0;
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(way[i - 1]));
			}
			// if next point is in behind of the current one
			if (way[i] - way[i - 1] == -widthOfMap) {
				switch (direction) {
				case 3:
					path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, nextPoint);
					break;
				case 0:
					path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, nextPoint);
					break;
				case 1:
					path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, nextPoint);
					break;
				}
				direction = 2;
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(way[i - 1]));
			}
		}

		switch (direction) {
		case 1:
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, Integer.toString(way[1]));
			break;
		case 2:
			path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, Integer.toString(way[1]));
			break;
		case 3:
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, Integer.toString(way[1]));
			break;
		}
		// path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 2,
		// Integer.toString(startPoint));
	}
}
