package trafficlight;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class SwitchLightBehaviour extends TickerBehaviour {

		private LightsColor currLight; 
    
        public SwitchLightBehaviour(Agent a, long period) {
            super(a, period);
        }
	
        @Override
        protected void onTick() {
             TrafficLightAgent trflight = getMyTrafficLightAgent();
             currLight = trflight.currentLight;
             if(currLight == LightsColor.Green){
                 currLight = LightsColor.Red;
                  System.out.println(trflight.getLocalName()+ ": light is now red" );
             } else {
                 currLight = LightsColor.Green;
                 System.out.println(trflight.getLocalName()+ ": light is now green" );
             }
             trflight.currentLight = currLight; //update
             trflight.myManager.notifyAll(String.valueOf(currLight.getValue()));
        }
        
        private TrafficLightAgent getMyTrafficLightAgent(){
            return (TrafficLightAgent) myAgent;
        }

        private static final long serialVersionUID = -5926454346965177769L;
}
