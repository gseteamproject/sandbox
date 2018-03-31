package integration.pallete.tests;

import jade.content.Concept;
import jade.content.ContentManager;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.content.onto.basic.Done;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import palleteRobotCommunication.ontology.PalleteRobotOntology;
import test.common.Test;
import test.common.TestException;
import test.common.TestUtility;
import palleteRobotCommunication.domain.SourcePalleteStateVocabulary;
import palleteRobotCommunication.domain.WhatIsYourState;

public class PalleteAlmostEmptyTest extends Test {

	private static final long serialVersionUID = -7605911029923355560L;

	private AID sourcePallete;

	Codec codec = new SLCodec();
	Ontology ontology = PalleteRobotOntology.getInstance();

	@Override
	public Behaviour load(Agent a) throws TestException {
		setTimeout(2000);
		sourcePallete = TestUtility.createAgent(a, "test", "palleteRobotCommunication.SourcePalleteAgent",
				new Object[] { 1 });
		CyclicBehaviour b = new CyclicBehaviour(a) {
			private static final long serialVersionUID = -3423642459063630856L;

			@Override
			public void onStart() {
				ContentManager cm = myAgent.getContentManager();
				cm.registerLanguage(codec);
				cm.registerOntology(ontology);

				WhatIsYourState question = new WhatIsYourState();
				Action a = new Action();
				a.setAction(question);
				a.setActor(sourcePallete);

				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(sourcePallete);
				msg.setOntology(PalleteRobotOntology.NAME);
				msg.setLanguage(codec.getName());
				try {
					cm.fillContent(msg, a);
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}
				System.out.println("Sending message " + msg);
				myAgent.send(msg);
			}

			@Override
			public void action() {
				ACLMessage msg = myAgent.receive();
				if (msg != null) {
					try {
						Done a = (Done) myAgent.getContentManager().extractContent(msg);
						Concept c = (Concept) a.getAction();
						WhatIsYourState question = (WhatIsYourState) ((Action) c).getAction();
						if (question != null) {
							if (question.getState().getDescription()
									.equals(SourcePalleteStateVocabulary.ALMOST_EMPTY)) {
								passed("Reply correct");
							} else {
								failed("Reply incorrect");
							}
						}

					} catch (OntologyException oe) {
						oe.printStackTrace();
					} catch (jade.content.lang.Codec.CodecException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return b;
	}

	@Override
	public void clean(Agent a) {
		try {
			TestUtility.killAgent(a, sourcePallete);
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
