package trafficlight;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.ProposeResponder;

import java.io.IOException;
import java.util.Date;

public class ChangeColorProposeResponder extends ProposeResponder {

	public ChangeColorProposeResponder(Agent a) {
        super(a, ProposeResponder.createMessageTemplate(FIPA_PROPOSE));
//        MessageTemplate t = ProposeResponder.createMessageTemplate("trLight");
    }

    @Override
    protected ACLMessage prepareResponse(ACLMessage propose) throws NotUnderstoodException, RefuseException {
        try {
            System.out.println(myAgent.getLocalName()+ ": preparing response" );

            ACLMessage response;
            TrafficLightAgent trafficLightAgent = (TrafficLightAgent) myAgent;

            SwitchColorPropose currentPropose = (SwitchColorPropose)propose.getContentObject();
            SwitchColorPropose proposeInProgress = trafficLightAgent.getProposeInProgress();

            if (proposeInProgress != null && proposeInProgress.getColor().equals(currentPropose.getColor())
                    && trafficLightAgent.getPriority() > currentPropose.getPriority()){
                response = prepareResponse(false, currentPropose, propose.getSender().getLocalName());
                System.out.println(myAgent.getLocalName()+ ": response is negative" );
            } else{
                response = prepareResponse(true, currentPropose, propose.getSender().getLocalName());
                if (currentPropose.getColor() == LightsColor.Green){
                    trafficLightAgent.setCurrentColor(LightsColor.Red);
                } else{
                    trafficLightAgent.setCurrentColor(LightsColor.Green);
                    if (trafficLightAgent.getGreenLightTimeCounter() == null){
                        trafficLightAgent.setGreenLightTimeCounter(new GreenLightTimeCountBehaviour(trafficLightAgent, trafficLightAgent.getMaxGreenTime()));
                        trafficLightAgent.addBehaviour(trafficLightAgent.getGreenLightTimeCounter());
                    } else{
                        trafficLightAgent.getGreenLightTimeCounter().restart();
                    }
                }
                System.out.println(myAgent.getLocalName()+ ": response is positive" );

                trafficLightAgent.getMySubscriptionManager().notifyAll(String.valueOf(trafficLightAgent.getCurrentColor().getValue()));
            }
            return response;
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ACLMessage prepareResponse(boolean isPositive, SwitchColorPropose propose, String destAgentName){
        int status = isPositive ? ACLMessage.ACCEPT_PROPOSAL : ACLMessage.REJECT_PROPOSAL;
        ACLMessage msg = new ACLMessage(status);

        msg.addReceiver(new AID(destAgentName, AID.ISLOCALNAME));
        msg.setProtocol(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
        try {
            msg.setContentObject(propose);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return msg;
    }

	private static final long serialVersionUID = -1786192933061510216L;
}
