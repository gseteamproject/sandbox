package mapRunner.common;

import java.util.Date;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class HandlePendingMessageBehaviour extends TickerBehaviour {

	public HandlePendingMessageBehaviour() {
		super(null, 60 * 1000);
	}

	private static final long serialVersionUID = -5396637309764296247L;

	@Override
	protected void onTick() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			return;
		}
		Date date = msg.getReplyByDate();
		long currentTime = System.currentTimeMillis();
		if (date == null) {
			date = new Date(currentTime + 1000 * 60 * 2);
			msg.setReplyByDate(date);
			myAgent.putBack(msg);
		} else {
			if (date.getTime() > currentTime) {
				myAgent.putBack(msg);
			} else {
				System.out.println("removed outdated message");
			}
		}
	}
}
