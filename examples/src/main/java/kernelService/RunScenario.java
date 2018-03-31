package kernelService;

import jade.Boot;

public class RunScenario {
	public static void main(String[] args) {
		String[] parameters = new String[] {
			"-gui",
			"-services", kernelService.LoggingService.class.getName() + ";jade.core.event.NotificationService",
			"-loggingService_verbose", "true",
			"agent:jade.core.Agent"
		};
		Boot.main(parameters);
	}
}
