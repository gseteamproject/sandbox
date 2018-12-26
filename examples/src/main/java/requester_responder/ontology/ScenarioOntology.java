package requester_responder.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

public class ScenarioOntology extends Ontology {

	public static final String ONTOLOGY_NAME = "scenario-ontology";

	public static final String OPERATION = "operation";
	public static final String OPERATION_NAME = "name";

	public static final String EXECUTE = "execute";
	public static final String EXECUTE_OPERATION = "operation";

	public static final String TERMINATE = "terminate";
	public static final String TERMINATE_OPERATION = "operation";

	public static final String OPERATION_PROPOSAL = "operation-proposal";
	public static final String OPERATION_PROPOSAL_OPERATION = "operation";
	public static final String OPERATION_PROPOSAL_DURATION_ESTIMATED = "durationEstimated";

	public static final String OPERATION_REFUSED = "operation-refused";
	public static final String OPERATION_REFUSED_OPERATION = "operation";
	public static final String OPERATION_REFUSED_REASON = "reason";

	public static final String OPERATION_COMPLETED = "operation-completed";
	public static final String OPERATION_COMPLETED_OPERATION = "operation";
	public static final String OPERATION_COMPLETED_DURATION = "duration";

	public static final String OPERATION_FAILED = "operation-failed";
	public static final String OPERATION_FAILED_OPERATION = "operation";
	public static final String OPERATION_FAILED_REASON = "reason";

	private static Ontology theInstance = new ScenarioOntology();

	public static Ontology getInstance() {
		return theInstance;
	}

	private ScenarioOntology() {
		super(ONTOLOGY_NAME, BasicOntology.getInstance());

		try {
			ConceptSchema cs = null;

			add(new ConceptSchema(OPERATION), Operation.class);
			cs = (ConceptSchema) getSchema(OPERATION);
			cs.add(OPERATION_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			AgentActionSchema as = null;

			add(new AgentActionSchema(EXECUTE), Execute.class);
			as = (AgentActionSchema) getSchema(EXECUTE);
			as.add(EXECUTE_OPERATION, (ConceptSchema) getSchema(OPERATION));

			add(new AgentActionSchema(TERMINATE), Terminate.class);
			as = (AgentActionSchema) getSchema(EXECUTE);
			as.add(TERMINATE_OPERATION, (ConceptSchema) getSchema(OPERATION));

			PredicateSchema ps = null;

			add(new PredicateSchema(OPERATION_PROPOSAL), OperationProposal.class);
			ps = (PredicateSchema) getSchema(OPERATION_PROPOSAL);
			ps.add(OPERATION_PROPOSAL_OPERATION, (ConceptSchema) getSchema(OPERATION));
			ps.add(OPERATION_PROPOSAL_DURATION_ESTIMATED, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

			add(new PredicateSchema(OPERATION_REFUSED), OperationRefused.class);
			ps = (PredicateSchema) getSchema(OPERATION_REFUSED);
			ps.add(OPERATION_REFUSED_OPERATION, (ConceptSchema) getSchema(OPERATION));
			ps.add(OPERATION_REFUSED_REASON, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			add(new PredicateSchema(OPERATION_COMPLETED), OperationCompleted.class);
			ps = (PredicateSchema) getSchema(OPERATION_COMPLETED);
			ps.add(OPERATION_COMPLETED_OPERATION, (ConceptSchema) getSchema(OPERATION));
			ps.add(OPERATION_COMPLETED_DURATION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));

			add(new PredicateSchema(OPERATION_FAILED), OperationFailed.class);
			ps = (PredicateSchema) getSchema(OPERATION_FAILED);
			ps.add(OPERATION_FAILED_OPERATION, (ConceptSchema) getSchema(OPERATION));
			ps.add(OPERATION_FAILED_REASON, (PrimitiveSchema) getSchema(BasicOntology.STRING));

		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 788796881383295456L;
}
