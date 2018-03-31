package threaded_vs_blocking;

import jade.core.behaviours.OneShotBehaviour;

public class BlockingBehaviour extends OneShotBehaviour {

	@Override
	public void action() {
		try {
			System.out.println("started");
			Thread.sleep(3000);
			System.out.println("stopped");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = -5284606566050055557L;
}
