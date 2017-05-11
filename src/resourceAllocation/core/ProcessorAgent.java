package resourceAllocation.core;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;
import java.util.List;

public abstract class ProcessorAgent extends Agent{

    private String _serviceType = "processor";

    @Override
    public void setup(){
        ServiceDescription sd = new ServiceDescription();
        sd.setType(_serviceType);
        sd.setName("ProcessingService");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        addBehaviour(new AgentFinderBehaviour(this, 1000));
    }

    public abstract void Serve(Worker[] agents);


    private class AgentFinderBehaviour extends WakerBehaviour{

        private ProcessorAgent _processorAgent;

        public AgentFinderBehaviour(ProcessorAgent agent, long tick){
            super(agent, tick);
            _processorAgent = agent;
        }

        @Override
        public void onWake() {
            List<Worker> foundWorkers = new ArrayList<Worker>();

            boolean replyStackIsNotEmpty = true;

            while (replyStackIsNotEmpty){
                ACLMessage aclMessage = myAgent.receive();
                if (aclMessage != null){
                    try {
                        long time = (long) aclMessage.getContentObject();

                        Worker worker = new Worker(aclMessage.getSender(), time, aclMessage.getReplyWith());
                        foundWorkers.add(worker);

                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    replyStackIsNotEmpty = false;
                }
            }

            if (foundWorkers.isEmpty()){
                this.reset();
            }

            _processorAgent.Serve(foundWorkers.toArray(new Worker[0]));
        }
    }
}
