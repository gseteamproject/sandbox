package palleteRobotCommunication;

/**
 * This class can fill a message depending on its 3 states: 1. I am full 2. I am
 * almost full 3. I am empty again
 * 
 * @author Tobias
 */
public class GoalPalleteAgent extends PalleteAgent {
	private static final long serialVersionUID = 1L;

	@Override
	protected State getPalleteState() {
		State state = new State();
		if (this.pallete.getCapacity() == this.pallete.getMaxCapacity()) {
			state.setDescription(GoalPalleteReply.FULL);
		} else if (this.pallete.getCapacity() == 0) {
			state.setDescription(GoalPalleteReply.EMPTY_AGAIN);
		} else {
			state.setDescription(GoalPalleteReply.AMLOST_FULL);
		}
		return state;
	}

	@Override
	protected void trace(String message) {
		System.out.println(getAID().getName() + " ( GoalPallete ): " + message);
	}
}
