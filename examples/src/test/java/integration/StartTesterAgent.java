package integration;

import jade.Boot;

public class StartTesterAgent {
	public static void main(String[] p_args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "sta:test.common.testSuite.TestSuiteAgent(testerList.xml)";
		Boot.main(parameters);
	}
}
