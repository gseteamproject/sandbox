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
import mapRunner.map.path.Path;
import mapRunner.map.target.Target;
import mapRunner.ontology.MapRunnerOntology;
import mapRunner.ontology.Vocabulary;

public class MapAgent extends Agent {
	private static final long serialVersionUID = -2245739743003700621L;

	private Map map = new Map();

	@Override
	protected void setup() {
		addBehaviour(new PlanPathBehaviour());
		addBehaviour(new UpdateMapBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());

		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MapRunnerOntology.getInstance());
	}

	class PlanPathBehaviour extends CyclicBehaviour {
		private static final long serialVersionUID = -5851628986020982760L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_PATH);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
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
				if (!(ce instanceof PathToTarget)) {
					reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
					send(reply);
					return;
				}
				PathToTarget predicate = (PathToTarget) ce;
				Target target = predicate.getTarget();
				Path path = map.getPath(target);
				predicate.setPath(path);

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
				RunnerLocation location = (RunnerLocation) ce;
				map.updateLocation(location);
			} else {
				block();
			}
		}
	}
}
