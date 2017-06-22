package resourceAllocation.core;

import java.util.Comparator;

import jade.core.AID;

public class Worker {
	final public AID _agent;
	final public long _time;
	final public String _replyWith;

	public float waitingTime = 0;

	public Worker(AID agent, long time, String replyWith) {
		_agent = agent;
		_time = time;
		_replyWith = replyWith;
	}

	public final static Comparator<Worker> compareByTime = new Comparator<Worker>() {
		@Override
		public int compare(Worker o1, Worker o2) {
			return (int) (o1._time - o2._time);
		}
	};
}
