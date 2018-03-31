package requester_responder.responder;

import java.util.Random;

public class Machine {

	public final static int DURATION_LIMIT = 5;

	private String reason = "";

	private final Random randomizer = new Random();

	private int durationEstimated = DURATION_LIMIT;

	private int duration = DURATION_LIMIT;

	public int counter = 0;

	public boolean executed = true;

	public boolean willExecute() {
		if (executed == true) {
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
		synchronized (this) {
			executed = false;
			counter = 0;

			int cycles = randomizer.nextInt(durationEstimated) + 2;
			/*
			 * duration = 0; while (duration < cycles) { System.out.println("cycle"); try {
			 * Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
			 * duration++; counter++; }
			 */
			try {
				Thread.sleep(cycles * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			duration = cycles;

			executed = true;
		}
	}

	public void terminate() {
		synchronized (this) {
			reason = "duration limit reached";

			executed = true;
		}
	}
}
