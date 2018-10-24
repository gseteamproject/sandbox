package robot.skills;

public class PaintSkill extends AbstractSkill {
	
	private static String Description = "Paint";
	
	public PaintSkill(float speed){
		super(speed);
		_speed = speed;
	}

	@Override
	public String getDescription() {
		return Description;
	}
}
