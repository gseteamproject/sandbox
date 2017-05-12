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
			String localName;
			AgentController station;
			
			localName= "Station_1";
			station=container.createNewAgent(localName, "transportsystem.StationAgent", new Object[] { "Station_2" });
			station.start();
			stationList.add(new AID(localName, AID.ISLOCALNAME));
			
			localName= "Station_2";
			station=container.createNewAgent(localName, "transportsystem.StationAgent", new Object[] { "Station_3" });
			station.start();
			stationList.add(new AID(localName, AID.ISLOCALNAME));
			
			localName= "Station_3";
			station=container.createNewAgent(localName, "transportsystem.StationAgent", new Object[] { "Station_4" });
			station.start();
			stationList.add(new AID(localName, AID.ISLOCALNAME));
			
			localName= "Station_4";
			station=container.createNewAgent(localName, "transportsystem.StationAgent", new Object[] { "Station_1" });
			station.start();
			stationList.add(new AID(localName, AID.ISLOCALNAME));

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

				AgentController shuttle = container.createNewAgent(localName, "transportsystem.ShuttleAgent",
						new Object[] { "Station_1" });
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
