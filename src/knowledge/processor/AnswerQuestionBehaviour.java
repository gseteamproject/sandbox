package knowledge.processor;

import jade.content.ContentManager;
import jade.content.Predicate;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import knowledge.ontology.KnowledgeOntology;
import knowledge.ontology.Question;

public class AnswerQuestionBehaviour extends AchieveREResponder {

	private static final long serialVersionUID = -6512614753440637766L;

	public AnswerQuestionBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
	}

	@Override
	protected ACLMessage handleRequest(ACLMessage request) {
		KnowledgeProcessorAgent myKnowledgeProcessorAgent = (KnowledgeProcessorAgent) myAgent;
		ACLMessage reply = request.createReply();

		ContentManager cm = myKnowledgeProcessorAgent.getContentManager();
		Predicate p = null;
		try {
			p = (Predicate) cm.extractContent(request);
		} catch (CodecException | OntologyException e) {
			myKnowledgeProcessorAgent.trace("incorrect question (" + request.getContent() + ")");
			reply.setPerformative(ACLMessage.REFUSE);
			reply.setContent(e.getMessage());
			return reply;
		}

		if (!(p instanceof Question)) {
			myKnowledgeProcessorAgent.trace("incorrect question (" + request.getContent() + ")");
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			return reply;
		}

		Question q = (Question) p;

		String key = q.getFact().getKey();
		String value = myKnowledgeProcessorAgent.knowledge.get(key);
		q.getFact().setValue(value);
		myKnowledgeProcessorAgent.trace("got question ( key [" + key + "] )");

		if (value != null) {
			// YES
			try {
				cm.fillContent(reply, q);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
		} else {
			// NO
			Ontology o = cm.lookupOntology(KnowledgeOntology.ONTOLOGY_NAME);
			AbsPredicate not = new AbsPredicate(SLVocabulary.NOT);
			try {
				not.set(SLVocabulary.NOT_WHAT, o.fromObject(q));
				cm.fillContent(reply, not);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}
		}

		reply.setPerformative(ACLMessage.INFORM);
		return reply;
	}
}
