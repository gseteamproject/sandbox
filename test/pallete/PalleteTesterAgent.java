package pallete;

import common.Test;
import common.TestGroup;
import common.TesterAgent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class PalleteTesterAgent extends TesterAgent {

	private static final long serialVersionUID = -4152023188919425834L;

	@Override
	protected TestGroup getTestGroup() {
		TestGroup tg = new TestGroup("pallete/palleteTestsList.xml") {
			private static final long serialVersionUID = 3695791829666231156L;
		};
		return tg;
	}

	public static void main(String[] args) {
		try {
			// Get a hold on JADE runtime
			Runtime rt = Runtime.instance();

			// Exit the JVM when there are no more containers around
			rt.setCloseVM(true);

			Profile pMain = new ProfileImpl(null, Test.DEFAULT_PORT, null);

			AgentContainer mc = rt.createMainContainer(pMain);

			AgentController rma = mc.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
			rma.start();

			AgentController tester = mc.createNewAgent("tester", "pallete.PalleteTesterAgent", args);
			tester.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
