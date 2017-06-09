package transportsystem.transportline;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import knowledge.Knowledge;

public class ListenBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = -4056102507089855423L;
	

	@Override
	public void action() {
		ACLMessage message = myAgent.receive();
		
		if (message != null){
			System.out.println(myAgent.getLocalName()+ " Получен заказ: " + message.getContent());
			
			String conversationId = message.getConversationId();
			//if(conversationId.equals(TransportSystem.TRANSPORT_SHUTTLE)){
				
			//}
			
		}
		else{
			block();
		}
	}

}
