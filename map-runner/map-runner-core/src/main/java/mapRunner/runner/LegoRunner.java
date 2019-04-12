package mapRunner.runner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import mapRunner.map.structure.Road;

public class LegoRunner implements Runner {

	final static int MovementSpeed = 200;
	final static int RotationSpeed = 100;

	final static int StackSize = 5;
	final static int ControlTime = 150;

	float[] angleData;
	float[] colorData;
	int newColor = Color.WHITE;
	int currentColor = Color.BLACK;
	List<Integer> recentColors = new ArrayList<Integer>();
	float colorRed;
	float colorGreen;
	float colorBlue;

	public void printData(float[] data) {
		GraphicsLCD lcd = ev3.getGraphicsLCD();
		lcd.setFont(Font.getSmallFont());

		lcd.clear();
		int x = lcd.getWidth() / 2;
		for (int i = 0; i < data.length; ++i) {
			lcd.drawString(String.format("%.2f", data[i]), x, i * 10, GraphicsLCD.HCENTER);
		}
		lcd.refresh();
	}

	public void getChargeLevel() {
		System.out.println("Battery: " + ev3.getPower().getVoltageMilliVolt() + " mV");
	}

	public void startMovingForward() {
		rightMotor.setSpeed(MovementSpeed);
		leftMotor.setSpeed(MovementSpeed);
		rightMotor.forward();
		leftMotor.forward();
	}

	public void startMovingBackward() {
		rightMotor.setSpeed(MovementSpeed);
		leftMotor.setSpeed(MovementSpeed);
		rightMotor.backward();
		leftMotor.backward();
	}

	public void stopMoving() {
		rightMotor.stop(true);
		leftMotor.stop(true);
	}

	public void startRotation(Direction direction) {
		rightMotor.setSpeed(RotationSpeed);
		leftMotor.setSpeed(RotationSpeed);
		if (direction == Direction.LEFT) {
			rightMotor.forward();
			leftMotor.backward();
		} else {
			leftMotor.forward();
			rightMotor.backward();
		}
	}

	public LegoRunner() {
		ev3 = BrickFinder.getLocal();
	}

	LegoRunner(Brick ev3, EV3ColorSensor colorSensor, EV3GyroSensor gyroSensor, EV3UltrasonicSensor ultrasonicSensor,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		this.ev3 = ev3;
		this.colorSensor = colorSensor;
		this.gyroSensor = gyroSensor;
		this.ultrasonicSensor = ultrasonicSensor;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
	}

	Brick ev3;

	EV3ColorSensor colorSensor;
	SampleProvider rgbSensor;

	EV3GyroSensor gyroSensor;
	SampleProvider angleSensor;

	EV3UltrasonicSensor ultrasonicSensor;
	SampleProvider distanceSensor;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;

	public boolean isMarker(int color) {
		switch (color) {
		case Color.BLACK:
		case Color.GREEN:
		case Color.RED:
			return true;
		default:
			return false;
		}
	}

	public boolean isActiveMarker(int color) {
		switch (color) {
		case Color.BLACK:
		case Color.GREEN:
			return true;
		default:
			return false;
		}
	}

	public boolean isInactiveMarker(int color) {
		switch (color) {
		case Color.RED:
			return true;
		default:
			return false;
		}
	}

	public boolean isSignalMarker(int color) {
		switch (color) {
		case Color.GREEN:
			return true;
		default:
			return false;
		}
	}

	@Override
	public void rotate(int rotationNumber, MapCreator mapCreator) {
		{
			int rotationCounter = 0;
			boolean rotationFlag = false;

			Direction moveDirection = Direction.RIGHT;
			if (rotationNumber > 0) {
				moveDirection = Direction.LEFT;
			} else {
				moveDirection = Direction.RIGHT;
			}

			if (mapCreator != null) {
				mapCreator.updateGrid(rotationCounter);
			}

			gyroSensor.reset();
			recentColors.clear();
			currentColor = Color.WHITE;

			startRotation(moveDirection);

			while (!ev3.getKey("Escape").isDown()) {

				if (rotationCounter == Math.abs(rotationNumber)) {
					stopMoving();
					Delay.msDelay(ControlTime);
					break;
				}

				colorMode(1);

				// if color detected
				// if white then keep rotating
				if (!(isMarker(currentColor))) {
					rotationFlag = true;
				}
//				System.out.println("currentColor " + currentColor);

				angleSensor.fetchSample(angleData, 0);
//				System.out.println("angleData[0] " + angleData[0]);
				// if not white then robot turned for 90 degrees
				if ((isMarker(currentColor) && rotationFlag
						&& (Math.abs(angleData[0]) > (float) (90 * (rotationCounter + 1) - 25)))) {
					rotationCounter++;
					rotationFlag = false;

					System.out.println("rotationCounter " + rotationCounter);

					// if black or green then it's a road
					if (mapCreator != null && isActiveMarker(currentColor)) {
						mapCreator.updatePosition(rotationCounter);
					}
				}
			}

			if (mapCreator != null) {

				int n = mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX];
				mapCreator.checkedPoints.add(n);

				System.out.println("listOfRoads after point:");
				Iterator<?> iterator = mapCreator.listOfRoads.roads.iterator();
				for (int i = 0; i < mapCreator.listOfRoads.roads.size(); i++) {
					Road r = (Road) iterator.next();
					System.out.println(r.startPoint + "=" + r.finishPoint);
				}
				System.out.println("W: " + mapCreator.widthOfMap + " H: " + mapCreator.heightOfMap + " p: ("
						+ mapCreator.currentPointX + ";" + mapCreator.currentPointY + ")");

				if (mapCreator.checkedPoints.size() == mapCreator.widthOfMap * mapCreator.heightOfMap) {
					mapCreator.isMapCompleted = true;
					System.out.println("STOP");
				}
			}
		}
		stopMoving();
	}

	public int rgbToColor(float colorRed, float colorGreen, float colorBlue) {
		int red = (int) (colorRed * 255);
		int green = (int) (colorGreen * 255);
		int blue = (int) (colorBlue * 255);
		int color = Color.WHITE;

		double diff = 1.2;

		int i = 0;
		do {
			i++;
			if (i > 50) {
				break;
			}
		} while (red * i < 255 && green * i < 255 && blue * i < 255);
		i--;

		red = red * i + 1;
		green = green * i + 1;
		blue = blue * i + 1;

		System.out.println("R: " + red + " G: " + green + " B: " + blue + " i: " + i);

		if (red / (double) green < diff && red / (double) blue < diff && green / (double) red < diff
				&& green / (double) blue < diff && blue / (double) red < diff && blue / (double) green < diff
				&& i < 6) {
			color = Color.WHITE;
		} else if (green / (double) red > diff && green / (double) blue > diff && i < 15) {
			color = Color.GREEN;
		} else if (red / (double) green > diff && red / (double) blue > diff && i < 10) {
			color = Color.RED;
		} else {
			color = Color.BLACK;
		}

		return color;
	}

	public int sortColors(List<Integer> recentColors) {
		int maxValue = 0;
		int maxCount = 0;

		for (int color1 : recentColors) {
			int counter = 0;
			for (int color2 : recentColors) {
				if (color2 == color1)
					++counter;
			}
			if (counter > maxCount) {
				maxCount = counter;
				maxValue = color1;
			}
		}
		return maxValue;
	}

	// 0 - move; 1 - rotate
	public void colorMode(int action) {

		int size = StackSize;
		if (action == 0) {
			size *= 2;
		}

		rgbSensor.fetchSample(colorData, 0);
		colorRed = colorData[0];
		colorGreen = colorData[1];
		colorBlue = colorData[2];
		newColor = rgbToColor(colorRed, colorGreen, colorBlue);
		recentColors.add(newColor);
		System.out.println("newColor: " + newColor);

		// check list of recent colors at every iteration
		if (recentColors.size() > size) {
			recentColors.remove(0);
		}
//		System.out.println("recentColors: "+recentColors);
		currentColor = sortColors(recentColors);
		System.out.println("currentColor: " + currentColor);

//		// update color every StackSize'th iteration
//		if (recentColors.size() == size) {
//			currentColor = sortColors(recentColors);
//			recentColors.clear();
//		}
	}

	@Override
	public void move(int signalMarkAmount) {
		leftMotor.setSpeed(MovementSpeed);
		rightMotor.setSpeed(MovementSpeed);

		Direction lastDirection = Direction.RIGHT;
		int rotationTime = 1000;
		int moveTime = 200;
		long currentTime = 0;

		boolean turningMode = false;
		long rotationStartTime = 0;
		long signalMarkCounter = 0;

		recentColors.clear();
		currentColor = Color.BLACK;

		while (!ev3.getKey("Escape").isDown()) {
			currentTime = System.currentTimeMillis();

			colorMode(0);

//			System.out.println("currentColor: " + currentColor);

			if (signalMarkCounter >= signalMarkAmount) {
				Delay.msDelay(moveTime);
//				ev3.getAudio().systemSound(1);
				stopMoving();
				Delay.msDelay(ControlTime);
				break;
//				}
			} else {
				if (turningMode && isMarker(currentColor)) {
					stopMoving();
					turningMode = false;
//					System.out.println("stop turning");
					currentTime = System.currentTimeMillis();
				}

				if (isActiveMarker(currentColor)) {

					startMovingForward();

					rotationTime = 200;

					if (isSignalMarker(currentColor)) {
//							System.out.println("green point");
						ev3.getAudio().systemSound(0);
						signalMarkCounter++;
					}
				}

				if (!isActiveMarker(newColor)) {
					if (signalMarkCounter == 0) {
						if (isInactiveMarker(currentColor)) {
//							System.out.println("red point");
							startMovingBackward();
						} else {
							currentColor = Color.WHITE;
							turningMode = true;
							if (currentTime - rotationStartTime > rotationTime) {
								stopMoving();

								if (lastDirection == Direction.RIGHT) {
									lastDirection = Direction.LEFT;
								} else {
									lastDirection = Direction.RIGHT;
								}

								rotationStartTime = currentTime;
								rotationTime += 300;
								startRotation(lastDirection);
							}
						}
					}
				}
			}
		}
		stopMoving();
	}

	@Override
	public void load() {
		System.out.println("loading a cargo");
	}

	@Override
	public void stop() {
		colorSensor.close();
		gyroSensor.close();
//		IRSensor.close();
		leftMotor.close();
		rightMotor.close();
	}

	@Override
	public void start() {
		colorSensor = new EV3ColorSensor(ev3.getPort("S4"));
		rgbSensor = colorSensor.getRGBMode();

		gyroSensor = new EV3GyroSensor(ev3.getPort("S1"));
		angleSensor = gyroSensor.getAngleMode();

//		ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S2"));
//        distanceSensor = ultrasonicSensor.getDistanceMode();

		leftMotor = new EV3LargeRegulatedMotor(ev3.getPort("D"));
		rightMotor = new EV3LargeRegulatedMotor(ev3.getPort("A"));

		colorData = new float[rgbSensor.sampleSize()];
		angleData = new float[angleSensor.sampleSize()];
	}
}
