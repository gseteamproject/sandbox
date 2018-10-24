package robot;
import java.util.ArrayList;

import jade.core.Agent;
import robot.behaviours.*;
import robot.skills.*;

public class RobotAgent extends Agent {
	
	private ArrayList<AbstractSkill> _skills; 
	
	public RobotAgent(){
		_skills = RobotHelper.skillsGenerator();
	}

	protected void setup(){
		//SkillRegistration 
		SkillsRegistratorBehaviour skillRegistrator = new SkillsRegistratorBehaviour(this, _skills);
		
		addBehaviour(skillRegistrator);
		//addBehaviour(new TrackRunnerBehaviour(this));
	}
	
	private static final long serialVersionUID = 8180700933370666129L;
}
