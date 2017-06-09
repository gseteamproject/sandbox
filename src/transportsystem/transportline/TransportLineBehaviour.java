package transportsystem.transportline;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class TransportLineBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -4056102507089855423L;
	

	@Override
	public void action() {
		ACLMessage msg_received = myAgent.receive();
		if (msg_received != null) {
			System.out.println(myAgent.getLocalName()+ " Получено задание: " + msg_received.getContent());
			
			
			TransportLineAgent tla = (TransportLineAgent) myAgent;
//			String targetShuttleName = "Shuttle_" + msg_received.getContent();
			
			for(AID shuttle: tla.shuttleList){
				
//				if(shuttle.getLocalName().equalsIgnoreCase(targetShuttleName))
//				{
				ACLMessage mesg = new ACLMessage(ACLMessage.INFORM);
				mesg.addReceiver(shuttle);
				mesg.setContent(msg_received.getContent());
				tla.send(mesg);
//				}
			}
			
		} else {
			block();
		}

	}

}
