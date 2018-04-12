package mapRunner.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import mapRunner.map.PathToTarget;
import mapRunner.map.RunnerLocation;
import mapRunner.map.path.Path;
import mapRunner.map.path.Point;
import mapRunner.map.target.Target;

public class MapRunnerOntology extends Ontology {
	private static final long serialVersionUID = 5644667211496682059L;

	public static final String NAME = "map-runner-ontology";

	public static final String TARGET = "target";
	public static final String TARGET_ID = "id";
	public static final String TARGET_POINT_NAME = "point-name";

	public static final String PATH = "path";
	public static final String PATH_POINTS = "points";

	public static final String POINT = "point";
	public static final String POINT_COMMAND = "command";
	public static final String POINT_AMOUNT = "amount";
	public static final String POINT_NAME = "name";

	public static final String PATH_TO_TARGET = "path-to-target";
	public static final String PATH_TO_TARGET_TARGET = "target";
	public static final String PATH_TO_TARGET_PATH = "path";

	public static final String RUNNER_LOCATION = "runner-location";
	public static final String RUNNER_LOCATION_POINT = "point";
	public static final String RUNNER_LOCATION_RUNNER = "runner";

	private MapRunnerOntology() {
		super(NAME, BasicOntology.getInstance());

		try {
			add(new ConceptSchema(TARGET), Target.class);
			add(new ConceptSchema(PATH), Path.class);
			add(new ConceptSchema(POINT), Point.class);
			add(new PredicateSchema(PATH_TO_TARGET), PathToTarget.class);
			add(new PredicateSchema(RUNNER_LOCATION), RunnerLocation.class);

			ConceptSchema cs;
			cs = (ConceptSchema) getSchema(TARGET);
			cs.add(TARGET_ID, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			cs.add(TARGET_POINT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			cs = (ConceptSchema) getSchema(PATH);
			cs.add(PATH_POINTS, (ConceptSchema) getSchema(POINT), 0, ObjectSchema.UNLIMITED);

			cs = (ConceptSchema) getSchema(POINT);
			cs.add(POINT_COMMAND, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(POINT_AMOUNT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(POINT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			PredicateSchema ps;
			ps = (PredicateSchema) getSchema(PATH_TO_TARGET);
			ps.add(PATH_TO_TARGET_TARGET, (ConceptSchema) getSchema(TARGET));
			ps.add(PATH_TO_TARGET_PATH, (ConceptSchema) getSchema(PATH));

			ps = (PredicateSchema) getSchema(RUNNER_LOCATION);
			ps.add(RUNNER_LOCATION_POINT, (ConceptSchema) getSchema(POINT));
			ps.add(RUNNER_LOCATION_RUNNER, (PrimitiveSchema) getSchema(BasicOntology.STRING));

		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

	private static Ontology theInstance = new MapRunnerOntology();

	public static Ontology getInstance() {
		return theInstance;
	}
}
