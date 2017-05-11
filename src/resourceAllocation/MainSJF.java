package resourceAllocation;

import jade.Boot;

public class MainSJF {
    public static void main(String[] args) {
        String[] parameters = new String[2];
        parameters[0] = "-gui";
        parameters[1] = "processor:resourceAllocation.processors.ShortestJobFirstProcessorAgent;";
        parameters[1] += "dummy1:resourceAllocation.DummyAgent(5);";
        parameters[1] += "dummy2:resourceAllocation.DummyAgent(3);";
        parameters[1] += "dummy3:resourceAllocation.DummyAgent(7);";
        parameters[1] += "dummy4:resourceAllocation.DummyAgent(1);";
        Boot.main(parameters);
    }
}
