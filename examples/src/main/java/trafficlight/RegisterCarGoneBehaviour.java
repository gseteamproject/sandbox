package trafficlight;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RegisterCarGoneBehaviour extends CyclicBehaviour {
    private static final long serialVersionUID = 6130496380982287815L;

    MessageTemplate template = MessageTemplate.MatchConversationId("car-gone");
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive(template);
        if (msg != null) {
            TrafficLightAgent trLightAgent = (TrafficLightAgent) myAgent;
            if (trLightAgent.getWaitingCarsCount() > 0) {
                trLightAgent.decreaseWaitingCarsCount();
            }
            System.out.println(myAgent.getLocalName() + ": Car gone. Total " + trLightAgent.getWaitingCarsCount());
        } else {
            block();
        }
    }
}
