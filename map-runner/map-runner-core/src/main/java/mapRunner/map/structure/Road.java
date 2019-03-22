package mapRunner.map.structure;

import jade.content.Concept;

public class Road implements Concept {
	private static final long serialVersionUID = -9157400910193270471L;

	public int startPoint;

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	public int finishPoint;

	public int getFinishPoint() {
		return finishPoint;
	}

	public void setFinishPoint(int finishPoint) {
		this.finishPoint = finishPoint;
	}

}
