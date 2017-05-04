package resourceAllocation;

import jade.Boot;

public class MainFCFS {
    public static void main(String[] args) {
        String[] parameters = new String[2];
        parameters[0] = "-gui";
        parameters[1] = "processor:com.resourceAllocation.processors.FirstComeFirstServedProcessorAgent;";
        parameters[1] += "dummy1:com.resourceAllocation.DummyAgent(5);";
        parameters[1] += "dummy2:com.resourceAllocation.DummyAgent(3);";
        parameters[1] += "dummy3:com.resourceAllocation.DummyAgent(7);";
        parameters[1] += "dummy4:com.resourceAllocation.DummyAgent(1);";
        Boot.main(parameters);
    }
}
