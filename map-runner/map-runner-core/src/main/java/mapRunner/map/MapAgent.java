package mapRunner.map;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.map.navigation.Navigation;
import mapRunner.map.navigation.NavigationToTarget;
import mapRunner.map.navigation.Target;
import mapRunner.map.structure.MapStructure;
import mapRunner.ontology.MapRunnerOntology;
import mapRunner.ontology.Vocabulary;

public class MapAgent extends Agent {
	private static final long serialVersionUID = -2245739743003700621L;

	private Pathfinder pathfinder = new Pathfinder();

	@Override
	protected void setup() {
		addBehaviour(new CreatePathBehaviour());
		addBehaviour(new UpdateMapBehaviour());
		addBehaviour(new CreateMapBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());

		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MapRunnerOntology.getInstance());

		pathfinder.setMap(null);
	}

	class CreatePathBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = -5851628986020982760L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_PATH);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
//			    System.out.println(msg);
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.INFORM);

				ContentManager cm = getContentManager();
				ContentElement ce = null;
				try {
					ce = cm.extractContent(msg);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent(e.getMessage());
					send(reply);
					return;
				}
				if (!(ce instanceof NavigationToTarget)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(reply);
					return;
				}

				NavigationToTarget predicate = (NavigationToTarget) ce;
				Target target = predicate.getTarget();
				Navigation path = pathfinder.getPath(target, msg.getSender().getLocalName());
				predicate.setNavigation(path);

				try {
					getContentManager().fillContent(reply, predicate);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent(e.getMessage());
					send(reply);
					return;
				}
				send(reply);
			} else {
				block();
			}
		}
	}

	class UpdateMapBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = -5851628986020982760L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_LOCATION);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
				ACLMessage reply = msg.createReply();

				ContentManager cm = getContentManager();
				ContentElement ce = null;
				try {
					ce = cm.extractContent(msg);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent(e.getMessage());
					send(reply);
					return;
				}
				if (!(ce instanceof RunnerLocation)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(reply);
					return;
				}
				RunnerLocation predicate = (RunnerLocation) ce;
				pathfinder.updateLocation(predicate);
			} else {
				block();
			}
		}
	}

	class CreateMapBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = -5851628986020982760L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_MAP);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
				ACLMessage reply = msg.createReply();

				ContentManager cm = getContentManager();
				ContentElement ce = null;
				try {
					ce = cm.extractContent(msg);
				} catch (CodecException | OntologyException e) {
					e.printStackTrace();
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent(e.getMessage());
					send(reply);
					return;
				}
				if (!(ce instanceof MapStructure)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(reply);
					return;
				}
				// TODO:
				MapStructure predicate = (MapStructure) ce;
				pathfinder.setMap(predicate);

			} else {
				block();
			}
		}
	}
}
