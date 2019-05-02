package mapRunner.runner;

import mapRunner.map.RunnerLocation;

public interface Runner {

	public void move(int amount);

	public void rotate(int degrees);

	public void stop();

	public void start();

    public void load();

    public RunnerLocation getRunnerLocation();
    
    public void setRunnerLocation(RunnerLocation location);

    public void detectRoads(MapCreator mapCreator);
}
