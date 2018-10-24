package testPackage;
import jade.core.Agent;

public class TrackRunnerAgent extends Agent {

	protected void setup(){
		
		try {
			addBehaviour(new TrackRunnerBehaviour(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 4423457642098614692L;
}
