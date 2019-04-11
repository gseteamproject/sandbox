package mapRunner.runner;

public interface Runner {

	public void move(int amount);

	public void rotate(int degrees, MapCreator mapCreator);

	public void stop();

	public void start();

    public void load();
}
