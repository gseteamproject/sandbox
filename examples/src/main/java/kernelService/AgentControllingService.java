package kernelService;

import jade.core.Agent;
import jade.core.ServiceException;
import jade.core.behaviours.OneShotBehaviour;

public class AgentControllingService extends Agent {

	private static final long serialVersionUID = 1101232116948406458L;

	@Override
	protected void setup() {
		addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 9087766835773899981L;

			@Override
			public void action() {
				try {
					LoggingHelper helper = (LoggingHelper) getHelper(LoggingService.NAME);
					helper.setVerbose(true);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
