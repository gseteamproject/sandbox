package mapRunner.ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;
import mapRunner.map.Point;
import mapRunner.map.RunnerLocation;
import mapRunner.map.navigation.Navigation;
import mapRunner.map.navigation.NavigationCommand;
import mapRunner.map.navigation.NavigationToTarget;
import mapRunner.map.navigation.Target;
import mapRunner.map.structure.MapParameters;
import mapRunner.map.structure.MapStructure;
import mapRunner.map.structure.Road;
import mapRunner.map.structure.Roads;

public class MapRunnerOntology extends Ontology {
	private static final long serialVersionUID = 5644667211496682059L;

	public static final String NAME = "map-runner-ontology";

	public static final String TARGET = "target";
	public static final String TARGET_DESTINATION = "destination";
	public static final String TARGET_LOCATION = "location";

	public static final String NAVIGATION = "path";
	public static final String NAVIGATION_COMMANDS = "commands";

	public static final String POINT = "point";
	public static final String POINT_NAME = "name";

	public static final String NAVIGATION_COMMAND = "navigation-commmand";
	public static final String NAVIGATION_COMMAND_TYPE = "type";
	public static final String NAVIGATION_COMMAND_QUANTITY = "quantity";
	public static final String NAVIGATION_COMMAND_POINT = "point";

	public static final String PATH_TO_TARGET = "path-to-target";
	public static final String PATH_TO_TARGET_TARGET = "target";
	public static final String PATH_TO_TARGET_NAVIGATION = "navigation";

	public static final String RUNNER_LOCATION = "runner-location";
	public static final String RUNNER_LOCATION_RUNNER = "runner";
	public static final String RUNNER_LOCATION_POINT = "point";
	public static final String RUNNER_LOCATION_DIRECTION = "direction";

	public static final String MAP_STRUCTURE = "map-structure";
	public static final String MAP_PARAMETERS = "map-parameters";
	
	// TODO: Refactor names
	public static final String MAP_HEIGHT = "height";
	public static final String MAP_WIDTH = "width";
	public static final String MAP_SIZE = "size";
	public static final String MAP_GRID = "grid";
	public static final String MAP_ROADS = "roads";
	
	public static final String MAP_ROAD = "road";
	public static final String ROAD_START = "startPoint";
	public static final String ROAD_FINISH = "finishPoint";

	private MapRunnerOntology() {
		super(NAME, BasicOntology.getInstance());

		try {
			add(new ConceptSchema(TARGET), Target.class);
			add(new ConceptSchema(NAVIGATION), Navigation.class);
			add(new ConceptSchema(POINT), Point.class);
			add(new ConceptSchema(NAVIGATION_COMMAND), NavigationCommand.class);
			add(new PredicateSchema(PATH_TO_TARGET), NavigationToTarget.class);
			add(new PredicateSchema(RUNNER_LOCATION), RunnerLocation.class);

			add(new ConceptSchema(MAP_ROADS), Roads.class);
			add(new ConceptSchema(MAP_ROAD), Road.class);
			add(new ConceptSchema(MAP_PARAMETERS), MapParameters.class);
			add(new PredicateSchema(MAP_STRUCTURE), MapStructure.class);

			ConceptSchema cs;
			cs = (ConceptSchema) getSchema(TARGET);
			cs.add(TARGET_DESTINATION, (ConceptSchema) getSchema(POINT));
			cs.add(TARGET_LOCATION, (ConceptSchema) getSchema(POINT));

			cs = (ConceptSchema) getSchema(NAVIGATION);
			cs.add(NAVIGATION_COMMANDS, (ConceptSchema) getSchema(NAVIGATION_COMMAND), 0, ObjectSchema.UNLIMITED);

			cs = (ConceptSchema) getSchema(POINT);
			cs.add(POINT_NAME, (PrimitiveSchema) getSchema(BasicOntology.STRING));

			cs = (ConceptSchema) getSchema(NAVIGATION_COMMAND);
			cs.add(NAVIGATION_COMMAND_TYPE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(NAVIGATION_COMMAND_QUANTITY, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(NAVIGATION_COMMAND_POINT, (ConceptSchema) getSchema(POINT));
			
			cs = (ConceptSchema) getSchema(MAP_PARAMETERS);
			cs.add(MAP_HEIGHT, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(MAP_WIDTH, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(MAP_SIZE, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			
			cs = (ConceptSchema) getSchema(MAP_ROAD);
			cs.add(ROAD_START, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			cs.add(ROAD_FINISH, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			
			cs = (ConceptSchema) getSchema(MAP_ROADS);
			cs.add(MAP_ROADS, (ConceptSchema) getSchema(MAP_ROAD), 0, ObjectSchema.UNLIMITED);

			PredicateSchema ps;
			ps = (PredicateSchema) getSchema(PATH_TO_TARGET);
			ps.add(PATH_TO_TARGET_TARGET, (ConceptSchema) getSchema(TARGET));
			ps.add(PATH_TO_TARGET_NAVIGATION, (ConceptSchema) getSchema(NAVIGATION));
			// TODO: Refactor schemas
			ps = (PredicateSchema) getSchema(RUNNER_LOCATION);
			ps.add(RUNNER_LOCATION_RUNNER, (PrimitiveSchema) getSchema(BasicOntology.STRING));
			ps.add(RUNNER_LOCATION_POINT, (ConceptSchema) getSchema(POINT));
			ps.add(RUNNER_LOCATION_DIRECTION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
			
			ps = (PredicateSchema) getSchema(MAP_STRUCTURE);
			ps.add(MAP_PARAMETERS, (ConceptSchema) getSchema(MAP_PARAMETERS));
			ps.add(MAP_ROADS, (ConceptSchema) getSchema(MAP_ROADS));

		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}

	private static Ontology theInstance = new MapRunnerOntology();

	public static Ontology getInstance() {
		return theInstance;
	}
}
