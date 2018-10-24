package robot.skills;

public class MoveSkill extends AbstractSkill {
	
	private static String Description = "Move";
	
	public MoveSkill(float speed){
		super(speed);
	}

	@Override
	public String getDescription() {
		return Description;
	}
}
