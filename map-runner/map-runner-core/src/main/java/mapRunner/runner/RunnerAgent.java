package mapRunner.runner;

import java.util.Iterator;

import jade.content.ContentElement;
import jade.content.ContentManager;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.map.PathToTarget;
import mapRunner.map.path.Command;
import mapRunner.map.path.Point;
import mapRunner.ontology.MapRunnerOntology;
import mapRunner.ontology.Vocabulary;

public class RunnerAgent extends Agent {

	private static final long serialVersionUID = -4794511877491259752L;

	private Runner runner;

	private boolean isBusy;

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	private ACLMessage targetRequest;

	private PathToTarget predicate;

	@Override
	protected void setup() {
		setupRunner();

		addBehaviour(new WaitForTargetBehaviour());
		addBehaviour(new WaitForPathBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());

		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MapRunnerOntology.getInstance());
	}

	private void setupRunner() {
		String runnerType = (String) getArguments()[0];
		if (runnerType.contentEquals("debug")) {
			runner = new DebugRunner();
		} else {
			runner = new LegoRunner();
		}
	}

	private void respondCancel(ACLMessage msg) {
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.CANCEL);
		reply.setContent("busy");
		send(reply);
	}

	private void respondAgree(ACLMessage msg) {
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.AGREE);
		send(reply);
	}

	private void respondInform(ACLMessage msg) {
		ACLMessage reply = msg.createReply();
		reply.setPerformative(ACLMessage.INFORM);
		send(reply);
	}

	class WaitForTargetBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -7837531286759971777L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_TARGET);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
				if (isBusy) {
					respondCancel(msg);
				} else {
					targetRequest = msg;
					respondAgree(targetRequest);
					predicate = new PathToTarget();
					predicate.getTarget().setId(msg.getContent());
					requestPath();
				}
			} else {
				block();
			}
		}

		private void requestPath() {
			addBehaviour(new RequestPathBehaviour());
		}
	}

	class RequestPathBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = -6620687276496598269L;

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(new AID(("map"), AID.ISLOCALNAME));
			msg.setConversationId(Vocabulary.CONVERSATION_ID_PATH);
			msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			msg.setOntology(MapRunnerOntology.NAME);
			try {
				getContentManager().fillContent(msg, predicate);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				return;
			}
			send(msg);
		}
	}

	class WaitForPathBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = 6651524509534077272L;

		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId(Vocabulary.CONVERSATION_ID_PATH);

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive(messageTemplate);
			if (msg != null) {
				if (isBusy) {
					respondCancel(msg);
				} else {
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
					if (!(ce instanceof PathToTarget)) {
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						send(reply);
						return;
					}
					predicate = (PathToTarget) ce;
					performMovement();
				}
			} else {
				block();
			}
		}

		private void performMovement() {
			MoveOnPathBehaviour b = new MoveOnPathBehaviour();
			addBehaviour(tbf.wrap(b));
		}
	}

	class MoveOnPathBehaviour extends OneShotBehaviour {

		private static final long serialVersionUID = 5468774161513653773L;

		@Override
		public void action() {
			isBusy = true;

			Iterator<?> iterator = predicate.getPath().points.iterator();
			while (iterator.hasNext()) {
				Point point = (Point) iterator.next();
				switch (point.command) {
				case Command.FORWARD:
					runner.move(point.amount);
					break;
				case Command.ROTATE_LEFT_90_DEGREE:
					runner.rotate(90 * point.amount);
					break;
				case Command.ROTATE_RIGHT_90_DEGREE:
					runner.rotate(-90 * point.amount);
					break;
				case Command.ROTATE_180_DEGREE:
					runner.rotate(180 * point.amount);
					break;
				}
			}
			runner.stop();
			respondInform(targetRequest);
			isBusy = false;
		}
	}
}
