package knowledge.producer;

import java.util.ArrayList;
import java.util.List;

import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import knowledge.Knowledge;
import knowledge.KnowledgeAgent;
import knowledge.ontology.Fact;
import knowledge.ontology.KnowledgeOntology;
import knowledge.ontology.Register;

public class KnowledgeProducerAgent extends KnowledgeAgent {

	private static final long serialVersionUID = -8333173138628880437L;

	public List<String> factsToProduce = new ArrayList<String>();

	@Override
	protected void initializeBehaviours() {
		addBehaviour(new ProduceFactsBehaviour(this, 2000));
	}

	@Override
	protected void initializeData() {
		Object[] args = getArguments();
		for (Object arg : args) {
			factsToProduce.add(arg.toString());
			trace("got fact (" + arg.toString() + ")");
		}
	}

	@Override
	protected ServiceDescription[] getAgentServiceDescriptions() {
		ServiceDescription agentServices[] = new ServiceDescription[1];
		agentServices[0] = new ServiceDescription();
		agentServices[0].setName(Knowledge.KNOWLEDGE_SERVICES);
		agentServices[0].setType(Knowledge.KNOWLEDGE_SERVICE_PRODUCING);
		return agentServices;
	}

	synchronized public void produceFact(AID[] knowledgeProcessors) {
		String fact = factsToProduce.get(0);
		for (AID knowledgeProcessor : knowledgeProcessors) {
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			message.addReceiver(knowledgeProcessor);
			message.setConversationId(Knowledge.KNOWLEDGE_PRODUCE_FACT);

			String strings[] = fact.split("=");
			Fact f = new Fact();
			f.setKey(strings[0]);
			f.setValue(strings[1]);
			Register r = new Register();
			r.setFact(f);
			Action a = new Action();
			a.setActor(knowledgeProcessor);
			a.setAction(r);

			message.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			message.setOntology(KnowledgeOntology.ONTOLOGY_NAME);
			try {
				getContentManager().fillContent(message, a);
			} catch (Exception e) {
				e.printStackTrace();
			}

			addBehaviour(new ProduceFactBehaviour(this, message));
		}
		factsToProduce.remove(0);
	}

	public AID[] findKnowledgeProcessors() {
		ServiceDescription requiredService = new ServiceDescription();
		requiredService.setType(Knowledge.KNOWLEDGE_SERVICE_PROCESSING);
		ServiceDescription requiredServices[] = new ServiceDescription[] { requiredService };

		return findAgentsProvidingServices(requiredServices);
	}
}
