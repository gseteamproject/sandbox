package mapRunner.map.navigation;

import jade.content.Concept;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import mapRunner.map.Point;

public class Navigation implements Concept {
	private static final long serialVersionUID = 5010213474912833311L;

	public List commands = new ArrayList();

	public List getCommands() {
		return commands;
	}

	public void setCommands(List commands) {
		this.commands = commands;
	}

	public void addNavigationCommand(int commandType, int commandQuantity, String pointName) {
		Point point = new Point();
		point.name = pointName;

		NavigationCommand command = new NavigationCommand();
		command.type = commandType;
		command.quantity = commandQuantity;
		command.point = point;

		commands.add(command);
	}
}
