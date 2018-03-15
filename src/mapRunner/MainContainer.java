package mapRunner;

import application.ArgumentBuilder;
import jade.Boot;

public class MainContainer {

	public static void main(String args[]) {
		String[] parameters = new String[] {
			"-gui",
			"-host", "localhost",
			ArgumentBuilder.agent("map", mapRunner.map.MapAgent.class) + ArgumentBuilder.agent("customer", mapRunner.customer.CustomerAgent.class)
		};
		Boot.main(parameters);
	}
}
