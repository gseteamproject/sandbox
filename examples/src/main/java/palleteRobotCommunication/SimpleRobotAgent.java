package palleteRobotCommunication;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import palleteRobotCommunication.domain.WhatIsYourState;
import palleteRobotCommunication.ontology.PalleteRobotOntology;

/**
 * 
 * @author Tobias
 *
 */
public class SimpleRobotAgent extends Agent {

	private static final long serialVersionUID = 9146116664910560304L;

	private Codec codec = new SLCodec();
	private Ontology ontology = PalleteRobotOntology.getInstance();

	private void trace(String message) {
		System.out.println(getAID().getName() + " ( SimpleRobot ): " + message);
	}

	protected void setup() {
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		initializeBehaviour();
	}

	private void initializeBehaviour() {
		AID sourcePallete = new AID("sourcePallete", AID.ISLOCALNAME);

		WhatIsYourState question = new WhatIsYourState();
		Action a = new Action();
		a.setAction(question);
		a.setActor(sourcePallete);

		ACLMessage request = new ACLMessage(ACLMessage.INFORM);
		request.addReceiver(sourcePallete);
		request.setOntology(PalleteRobotOntology.NAME);
		request.setLanguage(codec.getName());
		try {
			getContentManager().fillContent(request, a);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		trace("Requesting Source Pallete about his state: " + request.getContent());
		send(request);
		addBehaviour(new SimpleBehaviour(this) {
			private static final long serialVersionUID = 7774831398907094833L;

			public void action() {
				ACLMessage msg = receive();
				if (msg != null) {
					try {
						Done a = (Done) myAgent.getContentManager().extractContent(msg);
						Concept c = (Concept) a.getAction();
						WhatIsYourState question = (WhatIsYourState) ((Action) c).getAction();
						if (question != null) {
							trace("Got Reply from Pallete: " + question.getState().getDescription());
						}
					} catch (OntologyException oe) {
						oe.printStackTrace();
					} catch (jade.content.lang.Codec.CodecException e) {
						e.printStackTrace();
					}
				}

				block();
			}

			@Override
			public boolean done() {
				return false;
			}
		});

	}
}
