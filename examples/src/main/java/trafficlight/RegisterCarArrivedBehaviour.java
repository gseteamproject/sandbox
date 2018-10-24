package trafficlight;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.Date;

public class RegisterCarArrivedBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 6130496380982287815L;

    MessageTemplate template = MessageTemplate.MatchConversationId("car-arrived");
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);
        if (msg != null) {
            TrafficLightAgent trafficLightAgent = ((TrafficLightAgent) myAgent);
            trafficLightAgent.increaseWaitingCarsCount();
            System.out.println(myAgent.getLocalName() + ": Car arrived. Total " + trafficLightAgent.getWaitingCarsCount());
            if (trafficLightAgent.getWaitingCarsCount() >= trafficLightAgent.getMaxCarsWaitingCount()
                    && trafficLightAgent.getCurrentColor() == LightsColor.Red ){

                trafficLightAgent.addBehaviour(new ChangeColorProposeInitiator(trafficLightAgent, prepareMessage()));
            }
        } else {
            block();
        }
    }

    private ACLMessage prepareMessage(){
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(new AID(("trLight"), AID.ISLOCALNAME));
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        try {
            msg.setContentObject(new SwitchColorPropose(LightsColor.Green, ((TrafficLightAgent) myAgent).getPriority()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }
}
