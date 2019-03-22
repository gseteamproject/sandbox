package mapRunner.map;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class RunnersData implements Concept {
	private static final long serialVersionUID = 4108096752428561332L;

	public List<RunnerLocation> runnersData = new ArrayList<RunnerLocation>();

	public List<RunnerLocation> getRunnersData() {
		return runnersData;
	}

	public void setRunnersData(List<RunnerLocation> runnersData) {
		this.runnersData = runnersData;
	}

	public RunnerLocation getRunnerData(String runner) {
		for (RunnerLocation runnerData : runnersData) {
			if (runnerData.getRunner().equals(runner)) {
				return runnerData;
			}
		}
		return null;
	}

	public void updateRunnersData(RunnerLocation location) {
		boolean isExist = false;
		for (RunnerLocation runnerData : runnersData) {
			if (runnerData.getRunner().equals(location.getRunner())) {
				runnerData.setPoint(location.getPoint());
				runnerData.setDirection(location.getDirection());
				isExist = true;
			}
		}
		if (!isExist) {
			runnersData.add(location);
		}
	}
}
