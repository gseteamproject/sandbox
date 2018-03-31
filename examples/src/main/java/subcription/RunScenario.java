package subcription;

import jade.Boot;

public class RunScenario {
	public static void main(String[] args) {
		String[] parameters = new String[2];
		parameters[0] = "-gui";
		parameters[1] = "requester1:" + subcription.InitiatorAgent.class.getName() + ";";
		parameters[1] += "requester2:" + subcription.InitiatorAgent.class.getName() + ";";
		parameters[1] += "responder:" + subcription.ResponderAgent.class.getName() + ";";
		parameters[1] += "sniffer:jade.tools.sniffer.Sniffer(re*);";
		Boot.main(parameters);
	}
}
