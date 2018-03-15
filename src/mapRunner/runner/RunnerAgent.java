package mapRunner.runner;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;

public class RunnerAgent extends Agent {

	private static final long serialVersionUID = -4794511877491259752L;

	private LegoRunner runner = new LegoRunner();

	private boolean isBusy;

	private ThreadedBehaviourFactory tbf = new ThreadedBehaviourFactory();

	@Override
	protected void setup() {
		addBehaviour(new WaitForTargetBehaviour());
	}

	class WaitForTargetBehaviour extends CyclicBehaviour {

		private static final long serialVersionUID = -7837531286759971777L;

		@Override
		public void action() {
			ACLMessage msg = myAgent.receive();
			if (msg != null) {
				if (isConversationAboutTarget(msg)) {
					if (isBusy) {
						respondBusy(msg);
					} else {
						respondAgree(msg);
						planMovement(Integer.parseInt(msg.getContent()));
					}
				} else {
					respondNoUnderstood(msg);
				}
			} else {
				block();
			}
		}

		private void planMovement(int target) {
			MoveToTargetBehaviour b = new MoveToTargetBehaviour();
			b.target = target;
			addBehaviour(tbf.wrap(b));
		}

		private boolean isConversationAboutTarget(ACLMessage msg) {
			return msg.getConversationId().compareTo("target") == 0;
		}

		private void respondNoUnderstood(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			reply.setContent("conversationId = target, content = x");
			myAgent.send(reply);
		}

		private void respondAgree(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.AGREE);
			reply.setContent("agree");
			myAgent.send(reply);
		}

		private void respondBusy(ACLMessage msg) {
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.CANCEL);
			reply.setContent("busy");
			myAgent.send(reply);
		}
	}

	class MoveToTargetBehaviour extends OneShotBehaviour {
		private static final long serialVersionUID = 5468774161513653773L;

		public int target;

		@Override
		public void action() {
			isBusy = true;
			runner.move(target);
			isBusy = false;
		}
	}
}
