package mapRunner.runner;

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

	public void updateGrid() {
		
		System.out.println("W:" + widthOfMap + " H:" + heightOfMap + " p:("
				+ currentPointX + "; " + currentPointY + ")22222");
		
		pointGrid = new int[heightOfMap][widthOfMap];

		int p = 1;
		for (int y = 0; y < heightOfMap; y++) {
			for (int x = 0; x < widthOfMap; x++) {
				System.out.println("ячейка " + x + " " + y);
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

		for (Entry<Integer, Integer> entry : listOfRoads) {
			System.out.println(entry.toString());
			// TODO: Change roads points indexes
			// TODO: Make algorithm to check every road
		}
	}
}
