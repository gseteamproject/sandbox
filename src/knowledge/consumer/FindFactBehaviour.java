package knowledge.consumer;

import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import knowledge.KnowledgeAgent;
import knowledge.ontology.KnowledgeOntology;
import knowledge.ontology.Question;

public class FindFactBehaviour extends AchieveREInitiator {

	private static final long serialVersionUID = -8559347099939136940L;

	public FindFactBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor liked my question");
		try {
			AbsPredicate cs = (AbsPredicate) myAgent.getContentManager().extractAbsContent(inform);
			Ontology o = myAgent.getContentManager().lookupOntology(KnowledgeOntology.ONTOLOGY_NAME);
			if (cs.getTypeName().equals(KnowledgeOntology.QUESTION)) {
				Question q = (Question) o.toObject((AbsObject) cs);
				myKnowledgeAgent.trace("answer question ( key [" + q.getFact().getKey() + "] value ["
						+ q.getFact().getValue() + "] )");
			} else if (cs.getTypeName().equals(SLVocabulary.NOT)) {
				Question q = (Question) o.toObject(cs.getAbsObject(SLVocabulary.NOT_WHAT));
				myKnowledgeAgent.trace("no answer for question ( key [" + q.getFact().getKey() + "] )");
			} else {
				myKnowledgeAgent.trace("unexpected response");
			}
		} catch (CodecException | OntologyException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		KnowledgeAgent myKnowledgeAgent = (KnowledgeAgent) myAgent;
		myKnowledgeAgent.trace("knowledge processor does not liked my question");
	}
}
