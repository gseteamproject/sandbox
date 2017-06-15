package knowledge.processor;

import jade.content.ContentManager;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import knowledge.ontology.Fact;
import knowledge.ontology.Register;

public class StoreFactBehaviour extends AchieveREResponder {

	private static final long serialVersionUID = -2485692838213609172L;

	public StoreFactBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) {
		KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
		ACLMessage reply = request.createReply();

		ContentManager cm = myKnowledgeProcessorAgent.getContentManager();
		Action a = null;
		try {
			a = (Action) cm.extractContent(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Register r = (Register) a.getAction();
		Fact f = r.getFact();

//		if (request.getContent() == null) {
//			myKnowledgeProcessorAgent.trace("incorrect fact (" + request.getContent() + ")");
//			reply.setPerformative(ACLMessage.REFUSE);
//			reply.setContent("empty fact");
//			return reply;
//		}
//		String strings[] = request.getContent().split("=");
//		if (strings.length < 2) {
//			myKnowledgeProcessorAgent.trace("incorrect fact (" + request.getContent() + ")");
//			reply.setPerformative(ACLMessage.REFUSE);
//			reply.setContent("incorrect fact");
//			return reply;
//		}

//		String key = strings[0].trim();
//		String value = strings[1].trim();
		String key = f.getKey();
		String value = f.getValue();
		myKnowledgeProcessorAgent.knowledge.put(key, value);
		myKnowledgeProcessorAgent.trace("stored fact ( key [" + key + "] value [" + value + "] )");
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
}
