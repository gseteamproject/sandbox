package selfmessage;

import jade.Boot;

public class RunScenario {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "agent:" + selfmessage.ResponderAgent.class.getName();
		Boot.main(parameters);
	}
}
