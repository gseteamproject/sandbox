package palleteRobotCommunication;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PrimitiveSchema;

public class PalleteOntology extends Ontology {

	private static final long serialVersionUID = 6803296772639334049L;

	public static final String ONTOLOGY_NAME = "Pallete-ontology";

	public static final String PALLETE = "Pallete";
	public static final String PALLETE_MAX_CAPACITY = "maxCapacity";
	public static final String PALLETE_CAPACITY = "capacity";

	private static Ontology theInstance = new PalleteOntology();

	public static Ontology getInstance() {
		return theInstance;
	}

	private PalleteOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new ConceptSchema(PALLETE), Pallete.class);
			ConceptSchema cs = (ConceptSchema) getSchema(PALLETE);
			cs.add(PALLETE_MAX_CAPACITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(PALLETE_CAPACITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

		} catch (OntologyException oe) {
			oe.printStackTrace();
		}
	}
}
