package mapRunner.runner;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import mapRunner.common.HandlePendingMessageBehaviour;
import mapRunner.ontology.Vocabulary;

public class RunnerAgent extends Agent {

	private static final long serialVersionUID = -4794511877491259752L;

	private Runner runner;

	private boolean isBusy;

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	private ACLMessage targetRequest;

	private ACLMessage pathRespond;

	@Override
	protected void setup() {
		setupRunner();

		addBehaviour(new WaitForTargetBehaviour());
		addBehaviour(new WaitForPathBehaviour());
		addBehaviour(new HandlePendingMessageBehaviour());
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
			msg.setConversationId("path");
			msg.setContent(targetRequest.getContent());
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
					pathRespond = msg;
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
			runner.move(Integer.parseInt(pathRespond.getContent()));
			respondInform(targetRequest);
			isBusy = false;
		}
	}
}
