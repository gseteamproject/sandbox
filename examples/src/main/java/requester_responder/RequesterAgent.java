package requester_responder;

import jade.content.lang.sl.SLCodec;
import jade.content.lang.xml.XMLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import requester_responder.ontology.ScenarioOntology;
import requester_responder.requester.PeriodicActivityRequest;

public class RequesterAgent extends Agent {

	private Ontology ontology = ScenarioOntology.getInstance();

	@Override
	protected void setup() {
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerLanguage(new XMLCodec());
		getContentManager().registerOntology(ontology);

		addBehaviour(new PeriodicActivityRequest(this, 5000));
	}

	private static final long serialVersionUID = 683846685759308562L;
}
