package application;

public class ArgumentBuilder {

	public static String agent(String agentName, Class<?> agentClass) {
		return agentName + ":" + agentClass.getName() + ";";
	}

	public static String agent(String agentName, Class<?> agentClass, String parameters) {
		return agentName + ":" + agentClass.getName() + "(" + parameters + ");";
	}

}
