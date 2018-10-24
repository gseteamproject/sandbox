package robot.skills;

public class CleanSkill extends AbstractSkill {
	
	private static String Description = "Clean";
	
	public CleanSkill(float speed){
		super(speed);
	}

	@Override
	public String getDescription() {
		return Description;
	}
}
