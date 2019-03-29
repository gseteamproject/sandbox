package mapRunner.application;

import mapRunner.runner.LegoRunner;

public class LegoTestRunner {

	public static void main(String args[]) {
		LegoRunner runner = new LegoRunner();

		runner.start();
		
//		runner.getColor();		
		
//		runner.gyro();
		
//		runner.move(3);
		/*
		runner.rotate(90);
		runner.rotate(-90);
		runner.rotate(180);
		runner.rotate(-450);
		*/
		
		runner.stop();
	}
}
