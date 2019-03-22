package mapRunner.runner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mapRunner.map.structure.MapStructure;
import mapRunner.map.structure.Road;
import mapRunner.map.structure.Roads;

public class MapCreator {

	public int heightOfMap = 1;
	public int widthOfMap = 1;
	public int currentPointX = 0;
	public int currentPointY = 0;
	public boolean isMapCompleted = false;
	public int[][] pointGrid = new int[heightOfMap][widthOfMap];
	public List<Integer> checkedPoints = new ArrayList<Integer>();

	public Roads listOfRoads = new Roads();
	
	public int startDirection = 0;

	public MapStructure makeAMap() {
		MapStructure map = new MapStructure();
		map.getMapParameters().setHeight(heightOfMap);
		map.getMapParameters().setWidth(widthOfMap);
		map.getMapParameters().setSize(heightOfMap * widthOfMap);
		map.setRoads(listOfRoads);
		return map;
	}

	public void updateGrid(int rotationCounter) {
		pointGrid = new int[heightOfMap][widthOfMap];

		int p = 1;
		for (int y = 0; y < heightOfMap; y++) {
			for (int x = 0; x < widthOfMap; x++) {
				pointGrid[y][x] = p;
				p++;
			}
		}

		for (int i = 0; i < heightOfMap; i++) {
			for (int j = 0; j < widthOfMap; j++) {
				System.out.printf("%3d ", pointGrid[i][j]);
			}
			System.out.println();
		}

		if (rotationCounter != 0) {
			updateRoads(rotationCounter, listOfRoads);
		}
	}

	public void updateRoads(int rotationCounter, Roads roads) {
//		Iterator<?> iterator = roads.roads.iterator();
//		for (int i = 0; i < roads.roads.size(); i++) {
		for (Object road1 : Arrays.asList(roads.roads.toArray())) {
			Road road = (Road) road1;
//			Road road = (Road) iterator.next();
			int dif = road.startPoint - road.finishPoint;
			switch (rotationCounter) {
			case 1:
				road.startPoint += (currentPointY + 1);
				if (dif == -(widthOfMap - 1)) {
					road.finishPoint += (currentPointY + 2);
				} else if (dif == widthOfMap) {
					road.finishPoint += currentPointY;
				} else if (dif == -1) {
					road.finishPoint += (currentPointY + 1);
				}
				break;
			case 2:
				break;
			case 3:
				road.startPoint += currentPointY;
				if (dif == -(widthOfMap - 1)) {
					road.finishPoint += (currentPointY + 1);
				} else if (dif == widthOfMap - 1) {
					road.finishPoint += (currentPointY - 1);
				} else if (dif == 1) {
					road.finishPoint += currentPointY;
				}
				break;
			case 4:
				road.startPoint += widthOfMap;
				road.finishPoint += widthOfMap;
				break;
			}
		}
	}
	
	public void updatePosition(int rotationCounter) {
		if (rotationCounter != 0) {
			switch (startDirection) {
			case 1:
				switch (rotationCounter) {
				case 1:
					rotationCounter = 4;
					break;
				case 2:
				case 3:
				case 4:
					rotationCounter -= 1;
					break;
				}
				break;
			case 2:
				switch (rotationCounter) {
				case 1:
				case 2:
					rotationCounter += 2;
					break;
				case 3:
				case 4:
					rotationCounter -= 2;
					break;
				}
			case 3:
				switch (rotationCounter) {
				case 1:
				case 2:
				case 3:
					rotationCounter += 1;
					break;
				case 4:
					rotationCounter = 1;
					break;
				}
			}
		}
		
		switch (rotationCounter) {
		case 1: // if road to the left is found
			if (currentPointX == 0) {
				currentPointX += 1;
				widthOfMap += 1;
				updateGrid(rotationCounter);
			}
			listOfRoads.addRoad(
					pointGrid[currentPointY][currentPointX],
					pointGrid[currentPointY][currentPointX - 1]);
			break;
		case 2: // if road to the bottom is found
			if (currentPointY == heightOfMap - 1) {
				heightOfMap += 1;
				updateGrid(rotationCounter);
			}
			listOfRoads.addRoad(
					pointGrid[currentPointY][currentPointX],
					pointGrid[currentPointY + 1][currentPointX]);
			break;
		case 3: // if road to the right is found
			if (currentPointX == widthOfMap - 1) {
				widthOfMap += 1;
				updateGrid(rotationCounter);
			}
			listOfRoads.addRoad(
					pointGrid[currentPointY][currentPointX],
					pointGrid[currentPointY][currentPointX + 1]);
			break;
		case 4: // if road to the top is found
			if (currentPointY == 0) {
				currentPointY += 1;
				heightOfMap += 1;
				updateGrid(rotationCounter);
			}
			listOfRoads.addRoad(
					pointGrid[currentPointY][currentPointX],
					pointGrid[currentPointY - 1][currentPointX]);
			break;
		}
	}
			

	// TODO: Make an algorithm to check every road

}
