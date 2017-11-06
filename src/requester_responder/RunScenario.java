package requester_responder;

import application.ApplicationController;
import application.ApplicationModel;

public class RunScenario {
	public static void main(String[] args) {
		new ApplicationController().executeScenario(new ApplicationModel().requestResponder());
	}
}
