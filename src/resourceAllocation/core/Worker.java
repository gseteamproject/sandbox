package resourceAllocation.core;

import jade.core.AID;

public class Worker {
    private AID _agent;
    private long _time;
    private String _replyWith;

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
}
