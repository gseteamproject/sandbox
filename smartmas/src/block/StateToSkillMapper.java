package block;

public class StateToSkillMapper {
	
	private final static String _clean = "Clean";
	private final static String _paint = "Paint";
	private final static String _intermediate = "Move";
	
	private StateToSkillMapper (){}
	
	public static String MapToSkillFrom(State state, boolean isOnThePoint){
		switch (state){
			case Dirty:
				if (!isOnThePoint)
					return _intermediate;
				else
					return _clean;
			
			case Clear:
				if (!isOnThePoint)
					return _intermediate;
				else
					return _paint;
			
			case Painted:
				return _intermediate;
			
		}
		
		return null;
	}
}
