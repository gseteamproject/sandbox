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
import mapRunner.map.Point;
import mapRunner.map.RunnerLocation;
import mapRunner.map.navigation.Navigation;
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

	private Navigation navigation;

	private RunnerLocation location;

	private MapCreator mapCreator;

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
		String runnerStart = (String) getArguments()[1];
		if (runnerType.contentEquals("debug")) {
			runner = new DebugRunner();
		} else {
			runner = new LegoRunner();
		}
		location = new RunnerLocation();
		location.setRunner(this.getLocalName());
		// robot starts at "5"
		location.getPoint().setName(runnerStart);
		// "3" for A and B
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
					navigation = navigationToTarget.getNavigation();
					if (navigation.commands.size() == 1 && ((NavigationCommand) navigation.commands.get(0)).type == 3) {
						addBehaviour(new CreateMapBehaviour());
						mapCreator = new MapCreator();
					} else {
						addBehaviour(new MoveOnPathBehaviour());
					}
				}
			} else {
				block();
			}
		}
	}

	class CreateMapBehaviour extends SimpleBehaviour {
		private static final long serialVersionUID = -9019504440001330522L;

		private BehaviourState state = BehaviourState.initial;

		private Iterator<?> iterator = null;

		// Direction robot is facing to
		/*
		 * 0 - forward 1 - right 2 - back 3 - left
		 */
		int direction = 0;

		@Override
		public void action() {
			switch (state) {
			case initial:
				initialization();
				break;
			case active:
				if (iterator.hasNext()) {
					System.out.println("yos");
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
			iterator = navigation.commands.iterator();
			state = BehaviourState.active;
			runner.start();
		}

		private void activity() {

//			System.out.println("iterator1 " + iterator.hasNext());
			NavigationCommand command = (NavigationCommand) iterator.next();
//			System.out.println("iterator2 " + iterator.hasNext());
//			System.out.println("command.type1 " + command.type);

			switch (command.type) {
			case NavigationCommandType.FORWARD:
				runner.move(command.quantity);
				// TODO: enhance algorithm to create map without these corrective rotations
				switch (direction) {
				case 1:
					runner.rotate(1, null);
					break;
				case 2:
					runner.rotate(2, null);
					break;
				case 3:
					runner.rotate(-1, null);
					break;
				}
				direction = 0;
				if (!mapCreator.isMapCompleted) {
					navigation.addNavigationCommand(NavigationCommandType.ROTATE_180_DEGREE, 2, "0");
				}
				break;
			case NavigationCommandType.ROTATE_180_DEGREE:
				runner.rotate(2 * command.quantity, mapCreator);
				if (mapCreator.currentPointY > 0 && !mapCreator.checkedPoints
						.contains(mapCreator.pointGrid[mapCreator.currentPointY - 1][mapCreator.currentPointX])) {
					navigation.addNavigationCommand(NavigationCommandType.FORWARD, 1, "0");
					mapCreator.currentPointY -= 1;
				} else if (mapCreator.currentPointX < mapCreator.widthOfMap - 1 && !mapCreator.checkedPoints
						.contains(mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX + 1])) {
					runner.rotate(-1, null);
					direction = 1;
					navigation.addNavigationCommand(NavigationCommandType.FORWARD, 1, "0");
					mapCreator.currentPointX += 1;
				} else if (mapCreator.currentPointY < mapCreator.heightOfMap - 1 && !mapCreator.checkedPoints
						.contains(mapCreator.pointGrid[mapCreator.currentPointY + 1][mapCreator.currentPointX])) {
					runner.rotate(2, null);
					direction = 2;
					navigation.addNavigationCommand(NavigationCommandType.FORWARD, 1, "0");
					mapCreator.currentPointY += 1;
				} else if (mapCreator.currentPointX > 0 && !mapCreator.checkedPoints
						.contains(mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX - 1])) {
					runner.rotate(1, null);
					direction = 3;
					navigation.addNavigationCommand(NavigationCommandType.FORWARD, 1, "0");
					mapCreator.currentPointX -= 1;
				}

				break;
			}

			if (!mapCreator.isMapCompleted) {
				iterator = navigation.commands.iterator();
//				System.out.println("navigation.commands.size() " + navigation.commands.size());
				for (int i = 1; i < navigation.commands.size(); i++) {
//					System.out.println("meme " + i);
					iterator.next();
				}
			}
			for (int i = 0; i < navigation.commands.size(); i++) {
//				System.out.println("i " + i + " " + ((NavigationCommand) navigation.commands.get(i)).type);
			}
//			System.out.println("iterator3 "+iterator.hasNext());
//			System.out.println("command.type2 " + command.type);
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
			iterator = navigation.commands.iterator();
			state = BehaviourState.active;
			runner.start();
		}

		private void activity() {
			NavigationCommand command = (NavigationCommand) iterator.next();
			System.out.println("command.type " + command.type);

			switch (command.type) {
			case NavigationCommandType.FORWARD:
				runner.move(command.quantity);
				break;
			case NavigationCommandType.ROTATE_LEFT_90_DEGREE:
				runner.rotate(1 * command.quantity, null);
				break;
			case NavigationCommandType.ROTATE_RIGHT_90_DEGREE:
				runner.rotate(-1 * command.quantity, null);
				break;
			case NavigationCommandType.ROTATE_180_DEGREE:
				runner.rotate(2 * command.quantity, null);
				break;
			}
//			location.setPoint(command.point);
			Point newPoint = command.point;
			addBehaviour(new NotifyAboutLocationChangeBehaviour(newPoint));
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
		private Point currentPoint;

		public NotifyAboutLocationChangeBehaviour(Point newPoint) {
			currentPoint = newPoint;
		}

		@Override
		public void action() {
			location.setPoint(currentPoint);
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
