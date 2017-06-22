package resourceAllocation.core;

import java.util.Comparator;

import jade.core.AID;

public class Worker {
    private AID _agent;
    private long _time;
    private String _replyWith;
    
    public float waitingTime = 0;

    public Worker(AID agent, long time, String replyWith){
        _agent = agent;
        _time = time;
        _replyWith = replyWith;
	}

    public AID getAgent(){
        return _agent;
    }

    public long getTime(){
        return _time;
    }

    public String getReplyWIth(){
        return _replyWith;
    }
    
    public final static Comparator<Worker> compareByTime = new Comparator<Worker>() {
		@Override
		public int compare(Worker o1, Worker o2) {
			return (int) (o1.getTime() - o2.getTime());
		}
	};
}
