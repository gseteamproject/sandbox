package palleteRobotCommunication.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import palleteRobotCommunication.domain.Pallete;
import palleteRobotCommunication.domain.State;
import palleteRobotCommunication.domain.WhatIsYourState;

public class PalleteRobotOntology extends Ontology implements PalleteVocabulary, RobotVocabulary {

	private static final long serialVersionUID = 6803296772639334049L;

	public static final String NAME = "PalleteRobot-ontology";

	private static Ontology theInstance = new PalleteRobotOntology();

	public static Ontology getInstance() {
		return theInstance;
	}

	private PalleteRobotOntology() {
		super(NAME, BasicOntology.getInstance());

		ConceptSchema cs;
		try {
			add(new ConceptSchema(PALLETE), Pallete.class);
			cs = (ConceptSchema) getSchema(PALLETE);
			cs.add(PALLETE_MAX_CAPACITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(PALLETE_CAPACITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

			add(new ConceptSchema(STATE), State.class);
			cs = (ConceptSchema) getSchema(STATE);
			cs.add(STATE_DESCRIPTION, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			add(new AgentActionSchema(WHATISYOURSTATE), WhatIsYourState.class);
			cs = (AgentActionSchema) getSchema(WHATISYOURSTATE);
			cs.add(WHATISYOURSTATE_STATE, (ConceptSchema) getSchema(STATE), ObjectSchema.OPTIONAL);

		} catch (OntologyException oe) {
			oe.printStackTrace();
		}
	}
}
