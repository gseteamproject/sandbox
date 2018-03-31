package palleteRobotCommunication;

import palleteRobotCommunication.domain.GoalPalleteStateVocabulary;
import palleteRobotCommunication.domain.State;

/**
 * This class can fill a message depending on its 3 states: 1. I am full 2. I am
 * almost full 3. I am empty again
 * 
 * @author Tobias
 */
public class GoalPalleteAgent extends PalleteAgent implements GoalPalleteStateVocabulary {
	private static final long serialVersionUID = 1L;

	@Override
	protected State getPalleteState() {
		State state = new State();
		if (this.pallete.getCapacity() == this.pallete.getMaxCapacity()) {
			state.setDescription(GoalPalleteStateVocabulary.FULL);
		} else if (this.pallete.getCapacity() == 0) {
			state.setDescription(GoalPalleteStateVocabulary.EMPTY_AGAIN);
		} else {
			state.setDescription(GoalPalleteStateVocabulary.AMLOST_FULL);
		}
		return state;
	}

	@Override
	protected void trace(String message) {
		System.out.println(getAID().getName() + " ( GoalPallete ): " + message);
	}
}
