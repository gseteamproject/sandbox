package palleteRobotCommunication;

import palleteRobotCommunication.domain.SourcePalleteStateVocabulary;
import palleteRobotCommunication.domain.State;

/**
 * This class can fill a message depending on its 3 states: 1. I am empty 2. I
 * am almost empty 3. I am not empty anymore
 * 
 * @author Tobias
 */
public class SourcePalleteAgent extends PalleteAgent implements SourcePalleteStateVocabulary {
	private static final long serialVersionUID = 1L;

	@Override
	protected State getPalleteState() {
		State state = new State();
		if (this.pallete.getCapacity() == this.pallete.getMaxCapacity()) {
			state.setDescription(SourcePalleteStateVocabulary.NOT_EMPTY_ANYMORE);
		} else if (this.pallete.getCapacity() == 0) {
			state.setDescription(SourcePalleteStateVocabulary.EMPTY);
		} else {
			state.setDescription(SourcePalleteStateVocabulary.ALMOST_EMPTY);
		}
		return state;
	}

	@Override
	protected void trace(String message) {
		System.out.println(getAID().getName() + " ( SourcePallete ): " + message);
	}

}
