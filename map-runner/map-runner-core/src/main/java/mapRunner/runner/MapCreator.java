package mapRunner.runner;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MapCreator {

	public int heightOfMap = 1;
	public int widthOfMap = 1;
	public int currentPointX = 0;
	public int currentPointY = 0;
	public boolean isMapCompleted = false;
	public int[][] pointGrid = new int[heightOfMap][widthOfMap];
	ArrayList<Integer> checkedPoints = new ArrayList<Integer>();

	public List<Map.Entry<Integer, Integer>> listOfRoads = new ArrayList<>();

	public void makeAMap() {
	}

	public void updateGrid(int whatChanged) {

//		System.out.println("W:" + widthOfMap + " H:" + heightOfMap + " p:("
//				+ currentPointX + "; " + currentPointY + ")22222");

		pointGrid = new int[heightOfMap][widthOfMap];

		int p = 1;
		for (int y = 0; y < heightOfMap; y++) {
			for (int x = 0; x < widthOfMap; x++) {
//				System.out.println("cell " + x + " " + y);
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

		if (whatChanged != 0) {
			updateRoads(whatChanged, listOfRoads);
		}
	}

	public void updateRoads(int whatChanged, List<Map.Entry<Integer, Integer>> list) {
		for (int i = 0; i < list.size(); i++) {
			Entry<Integer, Integer> entry = list.get(i);
			int newKey = entry.getKey();
			int newValue = entry.getValue();
			int dif = entry.getKey() - entry.getValue();
			switch (whatChanged) {
			case 1:
				newKey = entry.getKey() + currentPointY + 1;
				if (dif == -(widthOfMap - 1)) {
					newValue = entry.getValue() + currentPointY + 2;
				} else if (dif == widthOfMap) {
					newValue = entry.getValue() + currentPointY;
				} else if (dif == -1) {
					newValue = entry.getValue() + currentPointY + 1;
				}
				break;
			case 2:
				break;
			case 3:
				newKey = entry.getKey() + currentPointY;
				if (dif == -(widthOfMap - 1)) {
					newValue = entry.getValue() + currentPointY + 1;
				} else if (dif == widthOfMap - 1) {
					newValue = entry.getValue() + currentPointY - 1;
				} else if (dif == 1) {
					newValue = entry.getValue() + currentPointY;
				}
				break;
			case 4:
				newKey = entry.getKey() + widthOfMap;
				newValue = entry.getValue() + widthOfMap;
				break;
			}
			list.remove(i);
			list.add(i, new AbstractMap.SimpleEntry<>(newKey, newValue));
		}
	}

	// TODO: Make an algorithm to check every road

}
