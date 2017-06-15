package knowledge.processor;

import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
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
		} catch (CodecException | OntologyException e) {
			myKnowledgeProcessorAgent.trace("incorrect fact (" + request.getContent() + ")");
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(e.getMessage());
			return reply;
		}

		Register r = (Register) a.getAction();
		Fact fact = r.getFact();

		String key = fact.getKey();
		String value = fact.getValue();
		myKnowledgeProcessorAgent.knowledge.put(key, value);
		myKnowledgeProcessorAgent.trace("stored fact ( key [" + key + "] value [" + value + "] )");
		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
}
