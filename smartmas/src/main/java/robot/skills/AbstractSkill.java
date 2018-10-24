package robot.skills;

public abstract class AbstractSkill {
		
	protected float _speed;
	
	public AbstractSkill(float speed){
		_speed = speed;
	}
	
	public abstract String getDescription();
	
	public float getSpeed(){
		return _speed;
	}

}
