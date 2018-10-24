package trafficlight;

public enum LightsColor {
	Red(0),
	Green(1);
	
	private final int id;
	LightsColor(int id) { this.id = id; }
    public int getValue() { return id; }
}
