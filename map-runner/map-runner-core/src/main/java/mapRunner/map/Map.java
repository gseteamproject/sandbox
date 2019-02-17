package mapRunner.map;

import mapRunner.map.navigation.NavigationCommandType;
import mapRunner.map.navigation.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapRunner.map.navigation.Navigation;

public class Map {
	
	//Map settings
	int heightOfMap = 3;
	int widthOfMap = 3;
	int sizeOfGraph = heightOfMap*widthOfMap;
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
	
	// Lee algorithm
	public void findingPath(Navigation path, int startPoint, int finishPoint) {
		// extend the grid to add impassable borders
		int H = heightOfMap + 2;
		int W = widthOfMap + 2;
		int G = H * W;

		int ax = (int) ((startPoint - 1) % widthOfMap) + 1;
		int ay = (int) ((startPoint - 1) / heightOfMap) + 1;
		int bx = (int) ((finishPoint - 1) % widthOfMap) + 1;
		int by = (int) ((finishPoint - 1) / heightOfMap) + 1;

		int WALL = -1; // impassable
		int BLANK = -2; // empty unmarked point

		// final path coordinates
		int[] px = new int[G];
		int[] py = new int[G];
		int len; // length of path
		int[][] grid = new int[H][W];
		// grid of actual point names
		int[][] pointGrid = new int[heightOfMap][widthOfMap];
		boolean stop;
		int dx[] = { 1, 0, -1, 0 };
		int dy[] = { 0, 1, 0, -1 };
		int d, x, y, k, p;

		// fill grids with values
		p = 1;
		for (y = 0; y < heightOfMap; y++) {
			for (x = 0; x < widthOfMap; x++) {
				pointGrid[y][x] = p;
				p++;
			}
		}

		for (y = 0; y < H; y++) {
			for (x = 0; x < W; x++) {
				grid[y][x] = BLANK;
				grid[0][x] = WALL;
				grid[H - 1][x] = WALL;
			}
			grid[y][0] = WALL;
			grid[y][W - 1] = WALL;
		}
		grid[ay][ax] = 0;

		d = 0;
		do {
			stop = true;
			for (y = 0; y < H; ++y) {
				for (x = 0; x < W; ++x) {
					if (grid[y][x] == d) {
						for (k = 0; k < 4; ++k) {
							int iy = y + dy[k], ix = x + dx[k];
							if (iy >= 0 && iy < H && ix >= 0 && ix < W && grid[iy][ix] == BLANK) {
								stop = false;
								grid[iy][ix] = d + 1;
							}
						}
					}
				}
			}
			d++;
		} while (!stop && grid[by][bx] == BLANK);

		len = grid[by][bx];
		x = bx;
		y = by;
		d = len;
		while (d > 0) {
			px[d] = x;
			py[d] = y;
			d--;
			for (k = 0; k < 4; ++k) {
				int iy = y + dy[k], ix = x + dx[k];
				if (iy >= 0 && iy < H && ix >= 0 && ix < W && grid[iy][ix] == d) {
					x = x + dx[k];
					y = y + dy[k];
					break;
				}
			}
		}
		px[0] = ax;
		py[0] = ay;

		int[] pointWay = new int[G];
		for (int i = 0; i < G; i++) {
			if (px[i] > 0 && py[i] > 0) {
				pointWay[i] = pointGrid[py[i] - 1][px[i] - 1];
			} else {
				break;
			}
		}
	//making path
	
	// Direction robot is facing to
	/*
	 * 0 - forward
	 * 1 - right
	 * 2 - back
	 * 3 - left
	 */
	int direction = 0;
		for (int i = 1; i < len + 1; i++) {
			String nextPoint = Integer.toString(pointWay[i - 1]);
			// if next point is at the left side of the current one
			if (pointWay[i] - pointWay[i - 1] == -1) {
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
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(pointWay[i]));
			}
			// if next point is at the right side of the current one
			if (pointWay[i] - pointWay[i - 1] == 1) {
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
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(pointWay[i]));
			}
			// if next point is in front of the current one
			if (pointWay[i] - pointWay[i - 1] == -widthOfMap) {
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
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(pointWay[i]));
			}
			// if next point is in behind of the current one
			if (pointWay[i] - pointWay[i - 1] == widthOfMap) {
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
				path.addNavigationCommand(NavigationCommandType.FORWARD, 1, Integer.toString(pointWay[i]));
			}
		}

		switch (direction) {
		case 1:
			path.addNavigationCommand(NavigationCommandType.ROTATE_LEFT_90_DEGREE, 1, Integer.toString(pointWay[len]));
			break;
		case 2:
			path.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 1, Integer.toString(pointWay[len]));
			break;
		case 3:
			path.addNavigationCommand(NavigationCommandType.ROTATE_RIGHT_90_DEGREE, 1, Integer.toString(pointWay[len]));
			break;
		}
	}
}
