package mapRunner.runner;

import java.util.ArrayList;
import java.util.List;

public class DebugRunner implements Runner {

	int count = 0;

	@Override
	public void move(int target) {

	}

	@Override
	public void rotate(int degrees, MapCreator mapCreator) {

		if (mapCreator != null) {

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
			System.out.println(" n:[" + n + "]");
			mapCreator.checkedPoints.add(n);
			for (int isWay : list.get(n - 1)) {
				rotationCounter++;
				if (isWay == 1) {
					mapCreator.updatePosition(rotationCounter);

//					System.out.println("W:" + mapCreator.widthOfMap + " H:" + mapCreator.heightOfMap + " p:("
//							+ mapCreator.currentPointX + "; " + mapCreator.currentPointY + ")");
				}
			}

//			System.out.println("listOfRoads after point:");
//			Iterator<?> iterator = mapCreator.listOfRoads.roads.iterator();
//			for (int i = 0; i < mapCreator.listOfRoads.roads.size(); i++) {
//				Road r = (Road) iterator.next();
//				System.out.println(r.startPoint + "=" + r.finishPoint);
//			}

			if (count == mapCreator.widthOfMap * mapCreator.heightOfMap) {
				mapCreator.isMapCompleted = true;
				count = 0;
				System.out.println("STOP");
			}
		}

	}
    
    @Override
    public void load() {
        System.out.println("loading a cargo");        
    }

	@Override
	public void stop() {
	}

	@Override
	public void start() {
	}
}
