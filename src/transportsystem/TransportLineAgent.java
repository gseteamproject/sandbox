package transportsystem;

import java.util.Vector;

import jade.core.AID;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class TransportLineAgent extends Agent {

	private static final long serialVersionUID = -3331126208966986144L;

	int nShuttle = 1;
	int nStation = 4;
	public Vector<AID> shuttleList = new Vector<AID>();
	public Vector<AID> stationList = new Vector<AID>();

	private void createStationAgents() {
		PlatformController container = getContainerController();
		try {
			for (int i = 1; i <= nStation; i++) {
				// create a new agent
				String localName = "Station_" + i;
				AgentController station = container.createNewAgent(localName, "transportsystem.StationAgent", null);
				station.start();

				stationList.add(new AID(localName, AID.ISLOCALNAME));
			}
		} catch (Exception e) {
			System.err.println("Exception while adding station: " + e);
			e.printStackTrace();
		}
	}

	private void createShuttleAgents() {
		PlatformController container = getContainerController();
		try {
			for (int i = 1; i <= nShuttle; i++) {
				// create a new agent
				String localName = "Shuttle_" + i;
				Object [] n = {"Station_1"};
				AgentController shuttle = container.createNewAgent(localName, "transportsystem.ShuttleAgent", n);
				shuttle.start();

				shuttleList.add(new AID(localName, AID.ISLOCALNAME));
			}
		} catch (Exception e) {
			System.err.println("Exception while adding shuttle: " + e);
			e.printStackTrace();
		}
	}

	protected void setup() {
		addBehaviour(new TransportLineBehaviour());

		createStationAgents();
		createShuttleAgents();

	}
}
