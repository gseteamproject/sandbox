package requester_responder.responder;

import java.util.Random;

public class Machine {

	private final static int DURATION_LIMIT = 5;

	private String reason = "";

	private final Random randomizer = new Random();

	private int durationEstimated = DURATION_LIMIT;

	private int duration = DURATION_LIMIT;

	public boolean willExecute() {
		if (randomizer.nextInt(100) > 50) {
			durationEstimated = randomizer.nextInt(DURATION_LIMIT) + 1;
			return true;
		}
		reason = "busy";
		return false;
	}

	public String getReason() {
		return reason;
	}

	public String getDurationEstimated() {
		return Integer.toString(durationEstimated);
	}

	public String getDuration() {
		return Integer.toString(duration);
	}

	public void execute() {
		duration = durationEstimated;
	}
}
