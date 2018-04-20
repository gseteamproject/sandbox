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
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.map.RunnerLocation;
import mapRunner.map.navigation.NavigationCommand;
import mapRunner.map.navigation.NavigationCommandType;
import mapRunner.map.navigation.NavigationToTarget;
import mapRunner.ontology.MapRunnerOntology;
import mapRunner.ontology.Vocabulary;

public class RunnerAgent extends Agent {
	private static final long serialVersionUID = -4794511877491259752L;

	private Runner runner;

	private boolean isBusy;

	private ACLMessage targetRequest;

	private NavigationToTarget navigationToTarget;

	private RunnerLocation location;

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
		location = new RunnerLocation();
		location.setRunner("runner");
		location.getPoint().setName("5");
		//"3" fo A and B
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
					navigationToTarget = new NavigationToTarget();
					navigationToTarget.getTarget().getDestination().setName(msg.getContent());
					navigationToTarget.getTarget().setLocation(location.getPoint());
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
				getContentManager().fillContent(msg, navigationToTarget);
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
					if (!(ce instanceof NavigationToTarget)) {
						reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
						send(reply);
						return;
					}
					navigationToTarget = (NavigationToTarget) ce;
					addBehaviour(new MoveOnPathBehaviour());
				}
			} else {
				block();
			}
		}
	}

	class MoveOnPathBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = 5468774161513653773L;

		private BehaviourState state = BehaviourState.initial;

		private Iterator<?> iterator = null;

		@Override
		public void action() {
			switch (state) {
			case initial:
				initialization();
				break;
			case active:
				if (iterator.hasNext()) {
					activity();
				} else {
					state = BehaviourState.beforeEnd;
				}
				break;
			case beforeEnd:
				shutdown();
				state = BehaviourState.finished;
				break;
			default:
				state = BehaviourState.finished;
				break;
			}
		}

		private void initialization() {
			isBusy = true;
			iterator = navigationToTarget.getNavigation().commands.iterator();
			state = BehaviourState.active;
			runner.start();
		}

		private void activity() {
			NavigationCommand command = (NavigationCommand) iterator.next();
			switch (command.type) {
			case NavigationCommandType.FORWARD:
				runner.move(command.quantity);
				break;
			case NavigationCommandType.ROTATE_LEFT_90_DEGREE:
				runner.rotate(1 * command.quantity);
				break;
			case NavigationCommandType.ROTATE_RIGHT_90_DEGREE:
				runner.rotate(-1 * command.quantity);
				break;
			case NavigationCommandType.ROTATE_180_DEGREE:
				runner.rotate(2 * command.quantity);
				break;
			}
			location.setPoint(command.point);
			addBehaviour(new NotifyAboutLocationChangeBehaviour());
		}

		private void shutdown() {
			runner.stop();
			respondInform(targetRequest);
			isBusy = false;
		}

		@Override
		public boolean done() {
			return state == BehaviourState.finished;
		}
	}

	private enum BehaviourState {
		initial, active, finished, beforeEnd
	};

	class NotifyAboutLocationChangeBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 7084813117098820135L;

		@Override
		public void action() {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID(("map"), AID.ISLOCALNAME));
			msg.setConversationId(Vocabulary.CONVERSATION_ID_LOCATION);
			msg.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
			msg.setOntology(MapRunnerOntology.NAME);
			try {
				getContentManager().fillContent(msg, location);
			} catch (CodecException | OntologyException e) {
				e.printStackTrace();
				return;
			}
			send(msg);
		}
	}
}
