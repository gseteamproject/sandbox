package requester_responder;

import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import requester_responder.models.Machine;
import requester_responder.models.Vocabulary;
import requester_responder.ontology.ScenarioOntology;
import requester_responder.responder.ActivityRespond;

public class ResponderAgent extends Agent {

	Machine machine = new Machine();

	private Ontology ontology = ScenarioOntology.getInstance();

	@Override
	protected void setup() {
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerLanguage(new XMLCodec());
		getContentManager().registerOntology(ontology);

		Behaviour main = new ActivityRespond(this);
		main.getDataStore().put(Vocabulary.MACHINE_OBJECT_KEY, machine);

		addBehaviour(main);
	}

	private static final long serialVersionUID = 579752306271997815L;
}
