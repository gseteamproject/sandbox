package knowledge.consumer;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jade.content.ContentManager;
import jade.content.abs.AbsObject;
import jade.content.abs.AbsPredicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import knowledge.KnowledgeAgent;
import knowledge.ontology.Fact;
import knowledge.ontology.KnowledgeOntology;
import knowledge.ontology.Question;

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

	@Test
	public void handleInform_answer() throws CodecException, OntologyException {
		ACLMessage informMessage_mock = context.mock(ACLMessage.class, "inform message");
		ContentManager contentManager_mock = context.mock(ContentManager.class);
		AbsPredicate absPredicate_mock = context.mock(AbsPredicate.class);
		Ontology ontology_mock = context.mock(Ontology.class);

		Fact fact = new Fact("key", "value");
		Question question = new Question();
		question.setFact(fact);

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

				oneOf(absPredicate_mock).getTypeName();
				will(returnValue(KnowledgeOntology.QUESTION));

				oneOf(ontology_mock).toObject(absPredicate_mock);
				will(returnValue(question));

				oneOf(knowledgeAgent_mock).trace("answer question ( key [key] value [value] )");
			}
		});

		findFactBehaviour.handleInform(informMessage_mock);
	}

	@Test
	public void handleInform_no_answer() throws CodecException, OntologyException {
		ACLMessage informMessage_mock = context.mock(ACLMessage.class, "inform message");
		ContentManager contentManager_mock = context.mock(ContentManager.class);
		AbsPredicate absPredicate_mock = context.mock(AbsPredicate.class);
		Ontology ontology_mock = context.mock(Ontology.class);
		AbsObject absObject_mock = context.mock(AbsObject.class);

		Fact fact = new Fact("key", "value");
		Question question = new Question();
		question.setFact(fact);

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
				will(returnValue(SLVocabulary.NOT));

				oneOf(absPredicate_mock).getAbsObject(SLVocabulary.NOT_WHAT);
				will(returnValue(absObject_mock));

				oneOf(ontology_mock).toObject(absObject_mock);
				will(returnValue(question));

				oneOf(knowledgeAgent_mock).trace("no answer for question ( key [key] )");
			}
		});

		findFactBehaviour.handleInform(informMessage_mock);
	}

	@Test
	public void handleInform_CodecException() throws CodecException, OntologyException {
		ACLMessage informMessage_mock = context.mock(ACLMessage.class, "inform message");
		ContentManager contentManager_mock = context.mock(ContentManager.class);

		context.checking(new Expectations() {
			{
				oneOf(knowledgeAgent_mock).trace("knowledge processor liked my question");

				oneOf(knowledgeAgent_mock).getContentManager();
				will(returnValue(contentManager_mock));

				oneOf(contentManager_mock).extractAbsContent(informMessage_mock);
				will(new Action() {
					@Override
					public void describeTo(Description arg0) {
					}

					@Override
					public Object invoke(Invocation arg0) throws Throwable {
						throw new CodecException("Unknown language");
					}
				});

				oneOf(knowledgeAgent_mock).trace("Unknown language");
			}
		});

		findFactBehaviour.handleInform(informMessage_mock);
	}

	@Test
	public void handleInform_OntologyException() throws CodecException, OntologyException {
		ACLMessage informMessage_mock = context.mock(ACLMessage.class, "inform message");
		ContentManager contentManager_mock = context.mock(ContentManager.class);

		context.checking(new Expectations() {
			{
				oneOf(knowledgeAgent_mock).trace("knowledge processor liked my question");

				oneOf(knowledgeAgent_mock).getContentManager();
				will(returnValue(contentManager_mock));

				oneOf(contentManager_mock).extractAbsContent(informMessage_mock);
				will(new Action() {
					@Override
					public void describeTo(Description arg0) {
					}

					@Override
					public Object invoke(Invocation arg0) throws Throwable {
						throw new OntologyException("Unknown ontology");
					}
				});

				oneOf(knowledgeAgent_mock).trace("Unknown ontology");
			}
		});

		findFactBehaviour.handleInform(informMessage_mock);
	}

	@Test
	public void handleRefuse() {
		ACLMessage refuseMessage_mock = context.mock(ACLMessage.class, "refuse message");

		context.checking(new Expectations() {
			{
				oneOf(knowledgeAgent_mock).trace("knowledge processor does not liked my question");
			}
		});

		findFactBehaviour.handleRefuse(refuseMessage_mock);
	}
}
