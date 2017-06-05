package knowledge;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 3401188230499296662L;

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		if (message != null) {
			KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;

			switch (message.getPerformative()) {
			case ACLMessage.INFORM:
				myKnowledgeProcessorAgent.storeFact(message);
				break;

			case ACLMessage.REQUEST:
				myKnowledgeProcessorAgent.answerQuestion(message);
				break;

			default:
				myKnowledgeProcessorAgent.notUnderStood(message);
				break;
			}
		} else {
			block();
		}
	}
}
