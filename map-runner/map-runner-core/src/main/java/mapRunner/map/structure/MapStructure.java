package mapRunner.map.structure;

import jade.content.Predicate;

public class MapStructure implements Predicate {
	private static final long serialVersionUID = -3298892470609619756L;

	private Roads roads = new Roads();;

	public Roads getRoads() {
		return roads;
	}

	public void setRoads(Roads roads) {
		this.roads = roads;
	}

	private MapParameters mapParameters = new MapParameters();

	public MapParameters getMapParameters() {
		return mapParameters;
	}

	public void setMapParameters(MapParameters mapParameters) {
		this.mapParameters = mapParameters;
	}

	// TODO: Make another class for pointGrid.
	// It will be needed if some points will be unavailable.
	// If so switch them to "0"
//	public int[][] pointGrid;

//		this.pointGrid = new int[height][width];
//
//		int p = 1;
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				pointGrid[y][x] = p;
//				p++;
//			}
//		}

//	public int[][] getGrid() {
//		return pointGrid;
//	}
}
