package knowledge.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;

public class KnowledgeOntology extends Ontology {

	private static final long serialVersionUID = 2857686231339045347L;

	public static final String ONTOLOGY_NAME = "knowledge-ontology";

	public static final String FACT = "Fact";
	public static final String FACT_KEY = "key";
	public static final String FACT_VALUE = "value";

	public static final String REGISTER = "Register";
	public static final String REGISTER_FACT = "fact";

	private static Ontology theInstance = new KnowledgeOntology();

	public static Ontology getInstance() {
		return theInstance;
	}

	private KnowledgeOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			add(new ConceptSchema(FACT), Fact.class);
			ConceptSchema cs = (ConceptSchema) getSchema(FACT);
			cs.add(FACT_KEY, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(FACT_VALUE, (PrimitiveSchema) getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);

			add(new AgentActionSchema(REGISTER), Register.class);
			AgentActionSchema as = (AgentActionSchema) getSchema(REGISTER);
			as.add(REGISTER_FACT, (ConceptSchema) getSchema(FACT));
		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}
}
