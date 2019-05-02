package mapRunner.runner;

import mapRunner.map.RunnerLocation;

public class DebugRunner implements Runner {

	int count = 0;

    public RunnerLocation location;
    
    @Override
    public RunnerLocation getRunnerLocation() {
        return location;
    }
    
    @Override
    public void setRunnerLocation(RunnerLocation location) {
        this.location = location;
    }
    
	@Override
	public void move(int target) {

	}

	@Override
	public void rotate(int degrees) {

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

    @Override
    public void detectRoads(MapCreator mapCreator) {
    }
}
