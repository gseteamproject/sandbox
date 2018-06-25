package trafficlight;

import java.io.Serializable;

public class SwitchColorPropose implements Serializable {
    private LightsColor _color;
    private int _priority;

    public SwitchColorPropose(LightsColor color, int priority){
        _color = color;
        _priority = priority;
    }

    public LightsColor getColor() {
        return _color;
    }

    public int getPriority() {
        return _priority;
    }
}
