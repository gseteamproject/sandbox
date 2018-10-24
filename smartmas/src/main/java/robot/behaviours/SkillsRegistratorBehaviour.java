package robot.behaviours;

import java.util.ArrayList;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import robot.RobotAgent;
import robot.skills.AbstractSkill;
import jade.domain.FIPAAgentManagement.Property;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

public class SkillsRegistratorBehaviour extends OneShotBehaviour {
	
	private final String ServiceGroup = "Skill";
	
	//TODO: do I need to keep it?
	private ArrayList<AbstractSkill> _skillsToRegister;
	private Agent _agent;
	
	public SkillsRegistratorBehaviour(RobotAgent robotAgent, ArrayList<AbstractSkill> skills){
		super(robotAgent);
		_skillsToRegister = skills;
		_agent = robotAgent;
	}

	@Override
	public void action() {
		DFAgentDescription agentDescription = new DFAgentDescription();
		agentDescription.setName(_agent.getAID());

		
		for (AbstractSkill skill : _skillsToRegister) {
			ServiceDescription serviceDescription = new ServiceDescription();
			
			Property skillSpeed = new Property();
			//TODO: remove hardcoded property name
			skillSpeed.setName("Speed");
			skillSpeed.setValue(skill.getSpeed());

			serviceDescription.setName(ServiceGroup);
			serviceDescription.setType(skill.getDescription());
			serviceDescription.addProperties(skillSpeed);
			agentDescription.addServices(serviceDescription);
		}
		
		try
		{
			DFService.register(_agent, agentDescription);
		}
		catch(FIPAException exception)
		{
			exception.printStackTrace();
		}
	}
	
	private static final long serialVersionUID = 5419361273738981724L;
}
