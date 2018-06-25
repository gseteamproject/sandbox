package trafficlight;

import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder;

public class LightSwitchSubscriptionResponder extends SubscriptionResponder {
    public LightSwitchSubscriptionResponder(SubscriptionManager subscriptionManager) {
        super(null, SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE), subscriptionManager);
    }

    @Override
    protected ACLMessage handleSubscription(ACLMessage subscription)
            throws NotUnderstoodException, RefuseException {
        ACLMessage decisionResult = subscription.createReply();

        try {
            super.handleSubscription(subscription);
            decisionResult.setPerformative(ACLMessage.AGREE);
        } catch (RefuseException e) {
            decisionResult.setPerformative(ACLMessage.REFUSE);
        } catch (NotUnderstoodException e) {
            decisionResult.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        }
        return decisionResult;
    }


    @Override
    protected ACLMessage handleCancel(ACLMessage cancel) throws FailureException {
        super.handleCancel(cancel);
        ACLMessage cancelResponse = cancel.createReply();
        cancelResponse.setPerformative(ACLMessage.INFORM);
        return cancelResponse;
    }

    private static final long serialVersionUID = -3111194745853398686L;
}
