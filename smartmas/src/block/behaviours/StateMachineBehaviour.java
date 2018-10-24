package block.behaviours;

import block.StateToSkillMapper;
import block.BlockAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;

public class StateMachineBehaviour extends CyclicBehaviour {
	
	private BlockAgent _blockAgent;
	private boolean _isOnThePoint = false;
	private AID _agentWorkerAID = null;
	
	public StateMachineBehaviour(BlockAgent blockAgent){
		_blockAgent = blockAgent;
	}

	@Override
	public void action() {
		if (_agentWorkerAID == null){
			_agentWorkerAID = getAgentForWork(StateToSkillMapper.MapToSkillFrom(_blockAgent.getBlockState(), _isOnThePoint));
		}
	}
	
	private AID getAgentForWork(String skill){
		return null;
	}
	
	private static final long serialVersionUID = -4001789017247709926L;
}
