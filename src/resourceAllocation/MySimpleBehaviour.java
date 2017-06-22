package resourceAllocation;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

public class MySimpleBehaviour extends Behaviour {

	private static final long serialVersionUID = 4319624727651629807L;
	private long _time;
    private int _step;
    private AID _processorAID;
    private MessageTemplate mt;


    public MySimpleBehaviour(long time){
        _time = time;
    }

    @Override
    public void action() {

        switch (_step){
            case 0:
                //getting processor
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("processor");
                dfd.addServices(sd);

                try {
                    DFAgentDescription[] result = DFService.search(myAgent, dfd);
                    if (result != null && result.length > 0){
                        _processorAID = result[0].getName();
                        _step = 1;
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
                break;

            case 1:
                //sending data to processor

                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                try {
                    cfp.setContentObject(_time);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cfp.addReceiver(_processorAID);
                cfp.setReplyWith("cfp"+System.currentTimeMillis());

                mt = MessageTemplate.MatchInReplyTo(cfp.getReplyWith());

                myAgent.send(cfp);
                _step = 2;
                break;

            case 2:
                ACLMessage reply = myAgent.receive(mt);
                if (reply != null){
                    System.out.println(myAgent.getName() + " was served.");
                    _step = 3;
                }
                break;
        }
    }

    @Override
    public boolean done() {
        return _step == 3;
    }
}

/*    @Override
    public void action() {
        //System.out.print("Agent " + getAgent().getName() + " work started. Time to finish: " + _time);
        try {
            Thread.sleep(_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.print("Agent " + getAgent().getName() + " work finished.");
    }*/