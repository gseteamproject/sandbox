package mapRunner.runner;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class LegoRunner implements Runner {

	final static int ForwardSpeed = 300;
	final static int RotationSpeed = 100;

	public void printData(float[] data) {
		GraphicsLCD lcd = ev3.getGraphicsLCD();
		lcd.setFont(Font.getSmallFont());

		lcd.clear();
		for (int i = 0; i < data.length; ++i) {
			lcd.drawString(String.format("%.2f", data[i]), lcd.getWidth() / 2, i * 10, GraphicsLCD.HCENTER);
		}
		lcd.refresh();
	}

	public void moveForward() {
		right.setSpeed(ForwardSpeed);
		left.setSpeed(ForwardSpeed);
		left.forward();
		right.forward();
	}

	public void stopMoving() {
		left.stop(true);
		right.stop(true);
	}

	public void turnDirection(Direction direction) {
		right.setSpeed(RotationSpeed);
		left.setSpeed(RotationSpeed);
		if (direction == Direction.LEFT) {
			right.forward();
			left.backward();
		} else {
			left.forward();
			right.backward();
		}
	}

	Brick ev3 = BrickFinder.getLocal();

	EV3ColorSensor cs = new EV3ColorSensor(ev3.getPort("S1"));
	SensorMode sensor = cs.getColorIDMode();

	EV3GyroSensor qs = new EV3GyroSensor(ev3.getPort("S2"));
	SampleProvider sensor2 = qs.getAngleMode();
	
	RegulatedMotor left = new EV3LargeRegulatedMotor(ev3.getPort("D"));
	RegulatedMotor right = new EV3LargeRegulatedMotor(ev3.getPort("A"));

	public boolean notWorkSpace(float[] data) {
		int colorId = (int) data[0];
		switch (colorId) {
		case lejos.robotics.Color.BLACK:
			return true;
		case lejos.robotics.Color.GREEN:
			return true;
		}
		return false;
	}

	public boolean alarmColour(float[] data) {
		int colorId = (int) data[0];
		switch (colorId) {
		case lejos.robotics.Color.GREEN:
			return true;
		}
		return false;
	}
	
	@Override
	public void rotate(int degrees) {
		int controlTime = 100;	
		float[] firstData = new float[sensor2.sampleSize()];
		sensor2.fetchSample(firstData, 0);
		Direction moveDirection = Direction.RIGHT;		
		if (degrees > 0) {
			moveDirection = Direction.LEFT;
		}
		else {
			moveDirection = Direction.RIGHT;
		}		
		while (!ev3.getKey("Escape").isDown()){
			float[] data = new float[sensor2.sampleSize()];
			sensor2.fetchSample(data, 0);
			printData(data);
			if (Math.abs(data[0]-firstData[0]) >= Math.abs(degrees)) {
				break;
			}
			turnDirection(moveDirection);
			Delay.msDelay(controlTime);
		}
	}

	@Override
	public void move(int circleAmount) {
		/*
		 * здесь надо разместить код из trackRunner учитывающий подсчёт зеленых меток
		 * 
		 * количество меток которые надо проверить определяется параметром circleAmount
		 */

		left.setSpeed(ForwardSpeed);
		right.setSpeed(ForwardSpeed);

		Direction lastDirection = Direction.RIGHT;
		int turningTime = 1000;

		boolean turningMode = false;
		boolean alarmFlag = false;
		long startTime = 0;
		long greenCounter = 0;

		while ((!ev3.getKey("Escape").isDown()) & (greenCounter < circleAmount)) {
			long currentTime = System.currentTimeMillis();

			float[] data = new float[sensor.sampleSize()];
			sensor.fetchSample(data, 0);

			printData(data);

			if (notWorkSpace(data)) {
				if (turningMode) {
					stopMoving();
				}

				/* Начало магии */
				if (alarmColour(data)) {
					alarmFlag = true;
				} else {
					if (alarmFlag) {
						alarmFlag = false;
						greenCounter++;
					}
				}
				/* Конец магии */

				moveForward();
				turningTime = 200;
				turningMode = false;
				startTime = currentTime;
			} else {
				if (currentTime - startTime > turningTime) {
					stopMoving();

					if (turningMode) {
						if (lastDirection == Direction.RIGHT) {
							lastDirection = Direction.LEFT;
						} else {
							lastDirection = Direction.RIGHT;
						}
					} else {
						turningMode = true;
					}

					startTime = currentTime;
					turningTime += 300;
					turnDirection(lastDirection);
					turningMode = true;
				}
			}
		}
		cs.close();
	}

	private enum Direction {
		LEFT, RIGHT
	}
}
