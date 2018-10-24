package core;

import java.util.StringJoiner;

import jade.Boot;
import jade.core.Agent;

public class JadeExecutor {
	
	//TODO: not right obviously. core package should not have entry point
	public static void main(String[] args) {
		runJade(new String[] {"-gui"});
	}
	
	public static void runRemote(String agentName, Class<?> agentClass){
		runRemote(agentName, agentClass, new String[0]);
	}
	
	public static void runRemote(String agentName, Class<?> agentClass, String[] args){
		if (!Agent.class.isAssignableFrom(agentClass))
			return;
		
		String arguments = getArgumentsString(args);
		
		String[] parameters = getParamsForRemote(agentName, agentClass.getName(), arguments);
		
		runJade(parameters);
	}
	
	public static void runLocal(String agentName, Class<?> agentClass){
		runLocal(agentName, agentClass, new String[0]);
	}
	
	public static void runLocal(String agentName, Class<?> agentClass, String[] args){
		if (!Agent.class.isAssignableFrom(agentClass))
			return;
		
		String arguments = getArgumentsString(args);
		
		String[] parameters = getParamsForLocal(agentName, agentClass.getName(), arguments);
		
		runJade(parameters);
	}
	
	private static String[] getParamsForRemote(String agentName, String agentClassName, String arguments){
		return new String[] {
				"-container", 
				"-host", Configuration.HostIp,
				"-port", Configuration.HostPort,
				"-local-port", Configuration.LocalPort,
				agentName + ":" + agentClassName + arguments + ";"
				};
	}
	
	private static String[] getParamsForLocal(String agentName, String agentClassName, String arguments){
		return new String[] {
				"-gui",
				agentName + ":" + agentClassName + arguments + ";"
				};
	}
	
	private static String getArgumentsString(String[] args){
		if (args.length == 0)
			return new String();
		
		StringJoiner joiner = new StringJoiner(",");
		for (int i = 0; i < args.length; ++i){
			joiner.add(args[i]);
		}
		return "(" + joiner + ")";
	}
	
	private static void runJade(String[] parameters){
		
		try{
			Boot.main(parameters);
		}
		catch (Exception e){
			System.out.print(e.getMessage());
		}
	}
	
	private JadeExecutor() {}
}
