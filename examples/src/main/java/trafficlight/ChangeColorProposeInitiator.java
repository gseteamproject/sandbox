package trafficlight;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ProposeInitiator;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ChangeColorProposeInitiator extends ProposeInitiator {

    public ChangeColorProposeInitiator(Agent a, ACLMessage msg) {
        super(a, msg);
        try {
            ((TrafficLightAgent)a).setProposeInProgress((SwitchColorPropose) msg.getContentObject());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Vector prepareInitiations(ACLMessage propose) {
        System.out.println(myAgent.getLocalName()+ ": trying to switch the color" );
        propose.setPerformative(ACLMessage.PROPOSE);
        List<String> agentsNames = getAllAgentsNames();
        for (String name: agentsNames){
            propose.addReceiver(new AID(name, AID.ISLOCALNAME));
        }
        propose.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        Vector l = new Vector(1);
        l.addElement(propose);
        //System.out.println(myAgent.getLocalName() + ": subscription request");
        return l;
        //return super.prepareInitiations(propose);
    }

    private List<String> getAllAgentsNames(){
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults(new Long(-1));
            AMSAgentDescription[] agents = AMSService.search(myAgent, new AMSAgentDescription(), c);
            List<String> names = new ArrayList<>();
            for (AMSAgentDescription agentDescription : agents){
                if (!agentDescription.getName().getLocalName().equals(myAgent.getLocalName())){
                    names.add(agentDescription.getName().getLocalName());
                }
            }
            return names;
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void handleAllResponses(Vector responses) {
        System.out.println(myAgent.getLocalName()+ ": recieved strange response" );
    }

    @Override
    protected void handleAcceptProposal(ACLMessage acceptProposal) {
        try {
            SwitchColorPropose propose = (SwitchColorPropose) acceptProposal.getContentObject();
            TrafficLightAgent trLightAgent = ((TrafficLightAgent) myAgent);
            trLightAgent.setCurrentColor(propose.getColor());
            if (trLightAgent.getCurrentColor() == LightsColor.Green){
                trLightAgent.addBehaviour(new GreenLightTimeCountBehaviour(trLightAgent, trLightAgent.getMaxGreenTime()));
            }
            trLightAgent.setProposeInProgress(null);
            trLightAgent.getMySubscriptionManager().notifyAll(String.valueOf(trLightAgent.getCurrentColor().getValue()));
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        System.out.println(myAgent.getLocalName()+ ": succeded to change the color" );
        this.getAgent().removeBehaviour(this);
    }

    @Override
    protected void handleRejectProposal(ACLMessage rejectProposal) {
        TrafficLightAgent trLightAgent = ((TrafficLightAgent) myAgent);
        trLightAgent.setProposeInProgress(null);
        System.out.println(myAgent.getLocalName()+ ": failed to change the color" );
        this.getAgent().removeBehaviour(this);
    }
}
