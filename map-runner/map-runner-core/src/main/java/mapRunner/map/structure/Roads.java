package mapRunner.map.structure;

import jade.util.leap.ArrayList;
import jade.util.leap.List;

import jade.content.Concept;

public class Roads implements Concept {
	private static final long serialVersionUID = 2402921957501403257L;

	public List roads = new ArrayList();

	public List getRoads() {
		return roads;
	}

	public void setRoads(List roads) {
		this.roads = roads;
	}

	public void addRoad(int firstPoint, int secondPoint) {
		Road road = new Road();
		road.setStartPoint(firstPoint);
		road.setFinishPoint(secondPoint);
		roads.add(road);
	}
}
