package knowledge.consumer;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;

public class KnowledgeConsumerAgent extends KnowledgeAgent {

	private static final long serialVersionUID = 1069538812627168203L;

	public List<String> questions = new ArrayList<String>();

	@Override
	protected void initializeBehaviours() {
		addBehaviour(new FindFactsBehaviour(this, 5000));
	}

	@Override
	protected void initializeData() {
		Object[] args = getArguments();
		for (Object arg : args) {
			questions.add(arg.toString());
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

	// TODO: change INTERACTION_PROTOCOL to QUERY_IF
	// TODO: ontology to implement = Predicate - HAS_FACT ( name, FACT) => YES (name, FACT) = NO (name, FACT) 
	synchronized public void findFact(AID[] knowledgeProcessors) {
		String fact = questions.get(0);
		for (AID knowledgeProcessor : knowledgeProcessors) {
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			message.addReceiver(knowledgeProcessor);
			message.setContent(fact);
			message.setConversationId(Knowledge.KNOWLEDGE_CONSUME_FACT);
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
