package resourceAllocation.core;

import java.util.Comparator;

import jade.core.AID;

public class Worker {
	final public AID _agent;
	final public long processingTime;
	final public String _replyWith;

	public long startedAt = 0;
	public long finishedAt = 0;

	public long remainingTime = 0;

	public long processSwitching = 0;

	public Worker(AID agent, long time, String replyWith) {
		_agent = agent;
		processingTime = time;
		_replyWith = replyWith;
	}

	public final static Comparator<Worker> compareByTime = new Comparator<Worker>() {
		@Override
		public int compare(Worker o1, Worker o2) {
			return (int) (o1.processingTime - o2.processingTime);
		}
	};

	public long getWaitingTime() {
		return finishedAt - processingTime;
	}

	public long getFullTime() {
		return finishedAt - startedAt;
	}
}
