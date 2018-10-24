package robot;

import java.util.ArrayList;
import java.util.Random;

import robot.skills.AbstractSkill;
import robot.skills.CleanSkill;
import robot.skills.MoveSkill;
import robot.skills.PaintSkill;

public final class RobotHelper {

	public static ArrayList<AbstractSkill> skillsGenerator(){
		ArrayList<AbstractSkill> skills = new ArrayList<>();
		ArrayList<AbstractSkill> skillsWithNulls = new ArrayList<>();
		
		skillsWithNulls.add((PaintSkill) skillGenerator(PaintSkill.class));
		skillsWithNulls.add((CleanSkill) skillGenerator(CleanSkill.class));
		skillsWithNulls.add((MoveSkill) skillGenerator(MoveSkill.class));
		
		for(AbstractSkill skill : skillsWithNulls){
			if (skill != null)
				skills.add(skill);
		}
		return skills;
	}
	
	 static private AbstractSkill skillGenerator(Class<? extends AbstractSkill> skill){
		Random random = new Random();
		
		float speed = random.nextInt(5) + 1;
		try{
			return skill.getDeclaredConstructor(float.class).newInstance(speed); 
		}
		catch(Exception e){
			return null;
		}
	}
	
	private RobotHelper (){}
}
