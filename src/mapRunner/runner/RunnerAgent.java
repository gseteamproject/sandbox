package mapRunner.runner;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class RunnerAgent extends Agent {

	private static final long serialVersionUID = -4794511877491259752L;

	private Runner runner;

	private boolean isBusy;

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	@Override
	protected void setup() {
		setupRunner();

		addBehaviour(new WaitForTargetBehaviour());
	}

	private void setupRunner() {
		String runnerType = (String) getArguments()[0];
		if (runnerType.contentEquals("debug")) {
			runner = new DebugRunner();
		} else {
			runner = new LegoRunner();
		}
	}

	class WaitForTargetBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -7837531286759971777L;

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				if (isConversationAboutTarget(msg)) {
					if (isBusy) {
						respondCancel(msg);
					} else {
						respondAgree(msg);
						planMovement(msg);
					}
				} else {
					respondNotUnderstood(msg);
				}
			} else {
				block();
			}
		}

		private void planMovement(ACLMessage msg) {
			MoveToTargetBehaviour b = new MoveToTargetBehaviour();
			b.request = msg;
			addBehaviour(tbf.wrap(b));
		}

		private boolean isConversationAboutTarget(ACLMessage msg) {
			String theme = msg.getConversationId();
			return theme != null && msg.getConversationId().compareTo("target") == 0;
		}

		private void respondNotUnderstood(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			reply.setContent("conversationId = target, content = x");
			myAgent.send(reply);
		}

		private void respondAgree(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.AGREE);
			myAgent.send(reply);
		}

		private void respondCancel(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.CANCEL);
			reply.setContent("busy");
			myAgent.send(reply);
		}
	}

	class MoveToTargetBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 5468774161513653773L;

		public ACLMessage request;

		@Override
		public void action() {
			isBusy = true;
			runner.move(Integer.parseInt(request.getContent()));
			respondInform(request);
			isBusy = false;
		}

		private void respondInform(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			myAgent.send(reply);
		}
	}
}
