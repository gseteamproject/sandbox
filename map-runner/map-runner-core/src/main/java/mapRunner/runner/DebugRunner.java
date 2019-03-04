package mapRunner.runner;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class DebugRunner implements Runner {

	int count = 0;

	@Override
	public void move(int target) {

	}

	@Override
	public void rotate(int degrees, MapCreator mapCreator) {

		if (mapCreator != null) {

			Entry<Integer, Integer> road;
			int rotationCounter = 0;

			List<int[]> list = new ArrayList<>();
			int[] subList1 = { 0, 1, 1, 0 };
			list.add(subList1);
			int[] subList2 = { 1, 1, 1, 0 };
			list.add(subList2);
			int[] subList3 = { 1, 1, 0, 0 };
			list.add(subList3);
			int[] subList4 = { 0, 1, 1, 1 };
			list.add(subList4);
			int[] subList5 = { 1, 1, 1, 1 };
			list.add(subList5);
			int[] subList6 = { 1, 1, 0, 1 };
			list.add(subList6);
//			int[] subList7 = { 0, 0, 1, 1 };
//			list.add(subList7);
//			int[] subList8 = { 1, 0, 1, 1 };
//			list.add(subList8);
//			int[] subList9 = { 1, 0, 0, 1 };
//			list.add(subList9);
			int[] subList7 = { 0, 1, 1, 1 };
			list.add(subList7);
			int[] subList8 = { 1, 1, 1, 1 };
			list.add(subList8);
			int[] subList9 = { 1, 1, 0, 1 };
			list.add(subList9);
			int[] subList10 = { 0, 0, 1, 1 };
			list.add(subList10);
			int[] subList11 = { 1, 0, 1, 1 };
			list.add(subList11);
			int[] subList12 = { 1, 0, 0, 1 };
			list.add(subList12);

			mapCreator.updateGrid(rotationCounter);

			count++;

//			System.out.println(" p:(" + mapCreator.currentPointX + "; " + mapCreator.currentPointY + ")");
			int n = mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX];
			System.out.println(mapCreator.checkedPoints.toString());
			System.out.println(" n:[" + n + "]");
			mapCreator.checkedPoints.add(n);
			for (int isWay : list.get(n - 1)) {
				rotationCounter++;
				if (isWay == 1) {
					System.out.println("rotationCounter=" + rotationCounter);

//					System.out.println("W:" + mapCreator.widthOfMap + " H:" + mapCreator.heightOfMap + " p:("
//							+ mapCreator.currentPointX + "; " + mapCreator.currentPointY + ")");
					switch (rotationCounter) {
					case 1: // if road to the left is found
						if (mapCreator.currentPointX == 0) {
							mapCreator.currentPointX += 1;
							mapCreator.widthOfMap += 1;
							mapCreator.updateGrid(rotationCounter);
						}
						road = new AbstractMap.SimpleEntry<>(
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX],
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX - 1]);
						mapCreator.listOfRoads.add(road);
						break;
					case 2: // if road to the bottom is found
						if (mapCreator.currentPointY == mapCreator.heightOfMap - 1) {
							mapCreator.heightOfMap += 1;
							mapCreator.updateGrid(rotationCounter);
						}
						road = new AbstractMap.SimpleEntry<>(
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX],
								mapCreator.pointGrid[mapCreator.currentPointY + 1][mapCreator.currentPointX]);
						mapCreator.listOfRoads.add(road);
						break;
					case 3: // if road to the right is found
						if (mapCreator.currentPointX == mapCreator.widthOfMap - 1) {
							mapCreator.widthOfMap += 1;
							mapCreator.updateGrid(rotationCounter);
						}
						road = new AbstractMap.SimpleEntry<>(
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX],
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX + 1]);
						mapCreator.listOfRoads.add(road);
						break;
					case 4: // if road to the top is found
						if (mapCreator.currentPointY == 0) {
							mapCreator.currentPointY += 1;
							mapCreator.heightOfMap += 1;
							mapCreator.updateGrid(rotationCounter);
						}
						road = new AbstractMap.SimpleEntry<>(
								mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX],
								mapCreator.pointGrid[mapCreator.currentPointY - 1][mapCreator.currentPointX]);
						mapCreator.listOfRoads.add(road);
						break;
					}

					System.out.println("W:" + mapCreator.widthOfMap + " H:" + mapCreator.heightOfMap + " p:("
							+ mapCreator.currentPointX + "; " + mapCreator.currentPointY + ")");
				}
			}

			System.out.println("listOfRoads after point:");
			for (Entry<Integer, Integer> entry : mapCreator.listOfRoads) {
				System.out.println(entry.toString());
			}

			if (count == mapCreator.widthOfMap * mapCreator.heightOfMap) {
				mapCreator.isMapCompleted = true;
				System.out.println("STOP");
			}
		}

	}

	@Override
	public void stop() {
	}

	@Override
	public void start() {
	}
}
