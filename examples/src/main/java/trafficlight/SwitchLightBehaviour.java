package trafficlight;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;

public class SwitchLightBehaviour extends TickerBehaviour {

        private int currLight; //0=green; 1=red;
    
        public SwitchLightBehaviour(Agent a, long period) {
            super(a, period);
        }
	
        @Override
        protected void onTick() {
             TrafficLightAgent trflight = getMyTrafficLightAgent();
             currLight = trflight.currentLight;
             if(currLight==0){
                 currLight=1;
                  System.out.println("Light is now red" );
             } else {
                 currLight=0;
                 System.out.println("Light is now green" );
             }
             trflight.currentLight=currLight; //update
            
        }
        
        private TrafficLightAgent getMyTrafficLightAgent(){
            return (TrafficLightAgent) myAgent;
        }
}
