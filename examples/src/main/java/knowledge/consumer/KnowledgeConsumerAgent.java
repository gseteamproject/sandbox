package knowledge.consumer;

import java.util.ArrayList;
import java.util.List;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;
import knowledge.ontology.Fact;
import knowledge.ontology.KnowledgeOntology;
import knowledge.ontology.Question;

public class KnowledgeConsumerAgent extends KnowledgeAgent {

	private static final long serialVersionUID = 1069538812627168203L;

	public List<Fact> questions = new ArrayList<Fact>();

	@Override
	protected void initializeBehaviours() {
		addBehaviour(new FindFactsBehaviour(this, 5000));
	}

	@Override
	protected void initializeData() {
		Object[] args = getArguments();
		for (Object arg : args) {
			Fact fact = new Fact(arg.toString(), null);
			questions.add(fact);
			trace("got question (" + arg.toString() + ")");
		}
	}

	@Override
	protected ServiceDescription[] getAgentServiceDescriptions() {
		ServiceDescription agentServices[] = new ServiceDescription[1];
		agentServices[0] = new ServiceDescription();
		agentServices[0].setName(Knowledge.KNOWLEDGE_SERVICES);
		agentServices[0].setType(Knowledge.KNOWLEDGE_SERVICE_CONSUMING);
		return agentServices;
	}

	synchronized public void findFact(AID[] knowledgeProcessors) {
		Fact fact = questions.get(0);
		for (AID knowledgeProcessor : knowledgeProcessors) {
			ACLMessage message = new ACLMessage(ACLMessage.QUERY_IF);
			message.setProtocol(FIPANames.InteractionProtocol.FIPA_QUERY);
			message.addReceiver(knowledgeProcessor);
			message.setConversationId(Knowledge.KNOWLEDGE_CONSUME_FACT);

			Question q = new Question();
			q.setFact(fact);

			message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			message.setOntology(KnowledgeOntology.ONTOLOGY_NAME);
			try {
				getContentManager().fillContent(message, q);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
			}

			addBehaviour(new FindFactBehaviour(this, message));
		}
		questions.remove(0);
	}

	public AID[] findKnowledgeProcessors() {
		ServiceDescription requiredService = new ServiceDescription();
		requiredService.setType(Knowledge.KNOWLEDGE_SERVICE_PROCESSING);
		ServiceDescription requiredServices[] = new ServiceDescription[] { requiredService };

		return findAgentsProvidingServices(requiredServices);
	}
}
