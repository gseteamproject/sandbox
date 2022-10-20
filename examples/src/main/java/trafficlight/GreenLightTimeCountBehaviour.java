package trafficlight;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GreenLightTimeCountBehaviour extends TickerBehaviour {

	public GreenLightTimeCountBehaviour(Agent a, long period) {
        super(a, period);
    }

    @Override
    protected void onTick() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        List<String> agentsNames = getAllAgentsNames();
        for (String name: agentsNames){
            msg.addReceiver(new AID(name, AID.ISLOCALNAME));
        }
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        try {
            msg.setContentObject(new SwitchColorPropose(LightsColor.Red, ((TrafficLightAgent)myAgent).getPriority()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(myAgent.getLocalName() + ": green-light time is over");
        myAgent.addBehaviour(new ChangeColorProposeInitiator(myAgent, msg));
    }

    private List<String> getAllAgentsNames(){
        try {
            SearchConstraints c = new SearchConstraints();
            c.setMaxResults(Long.valueOf(-1));
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

    private static final long serialVersionUID = -1587957059358048498L;
}
