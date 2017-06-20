package knowledge.consumer;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jade.content.ContentManager;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import knowledge.KnowledgeAgent;
import knowledge.ontology.KnowledgeOntology;

public class FindFactBehaviourTest {
	private final Mockery context = new Mockery() {
		{
			this.setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	ACLMessage requestMessage_mock;
	KnowledgeAgent knowledgeAgent_mock;

	FindFactBehaviour findFactBehaviour;

	@Before
	public void setUp() {
		requestMessage_mock = context.mock(ACLMessage.class, "request message");
		knowledgeAgent_mock = context.mock(KnowledgeAgent.class);

		findFactBehaviour = new FindFactBehaviour(knowledgeAgent_mock, requestMessage_mock);
	}
	
	@After
	public void tearDown() {
		context.assertIsSatisfied();
	}

	@Test
	public void handleInform_unexpected_response() throws CodecException, OntologyException {
		ACLMessage informMessage_mock = context.mock(ACLMessage.class, "inform message");
		ContentManager contentManager_mock = context.mock(ContentManager.class);
		AbsPredicate absPredicate_mock = context.mock(AbsPredicate.class);
		Ontology ontology_mock = context.mock(Ontology.class);

		context.checking(new Expectations() {
			{
				oneOf(knowledgeAgent_mock).trace("knowledge processor liked my question");

				oneOf(knowledgeAgent_mock).getContentManager();
				will(returnValue(contentManager_mock));

				oneOf(contentManager_mock).extractAbsContent(informMessage_mock);
				will(returnValue(absPredicate_mock));

				oneOf(knowledgeAgent_mock).getContentManager();
				will(returnValue(contentManager_mock));

				oneOf(contentManager_mock).lookupOntology(KnowledgeOntology.ONTOLOGY_NAME);
				will(returnValue(ontology_mock));

				exactly(2).of(absPredicate_mock).getTypeName();
				will(returnValue("unexpected"));

				oneOf(knowledgeAgent_mock).trace("unexpected response");
			}
		});

		findFactBehaviour.handleInform(informMessage_mock);
	}
}
