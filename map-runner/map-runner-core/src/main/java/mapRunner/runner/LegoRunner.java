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
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import mapRunner.map.structure.Road;

public class LegoRunner implements Runner {

	final static int ForwardSpeed = 200;
	final static int RotationSpeed = 100;

	final static int StackSize = 10;

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
		rightMotor.setSpeed(ForwardSpeed);
		leftMotor.setSpeed(ForwardSpeed);
		rightMotor.forward();
		leftMotor.forward();
	}

	public void stopMoving() {
		rightMotor.stop(true);
		leftMotor.stop(true);
	}

	public void turnDirection(Direction direction) {
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

	Brick ev3;

	EV3ColorSensor colorSensor;
	SampleProvider colorIdSensor;

	RegulatedMotor leftMotor;
	RegulatedMotor rightMotor;

	public boolean isMarker(int color) {
		switch (color) {
		case 1:
		case 2:
		case 3:
			return true;
		}
		return false;
	}

	public boolean isActiveMarker(int color) {
		switch (color) {
		case 1:
		case 2:
			return true;
		}
		return false;
	}
	
	public boolean isInactiveMarker(int color) {
		switch (color) {
		case 3:
			return true;
		}
		return false;
	}

	public boolean isSignalMarker(int color) {
		switch (color) {
		case 2:
			return true;
		}
		return false;
	}

	@Override
	public void rotate(int rotationNumber, MapCreator mapCreator) {
		{

			int controlTime = 150;
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

			float[] colorData = new float[colorIdSensor.sampleSize()];
			int oldColor = 0;
			int currentColor = 0;
			List<Integer> recentColors = new ArrayList<Integer>();

			float colorRed;
			float colorGreen;
			float colorBlue;
			int[] realColors;

			while (!ev3.getKey("Escape").isDown()) {
				colorIdSensor.fetchSample(colorData, 0);
				colorRed = colorData[0];
				colorGreen = colorData[1];
				colorBlue = colorData[2];
				currentColor = rgbToColor(colorRed, colorGreen, colorBlue);
				recentColors.add(currentColor);

				// update color every 60th iteration
				if (recentColors.size() == StackSize) {
					realColors = sortColors(recentColors, oldColor);
					currentColor = realColors[0];
					oldColor = realColors[1];
					recentColors.clear();
				}

				if (rotationCounter == Math.abs(rotationNumber)) {
					break;
				}

				// if color detected
				// if white then keep rotating
				if (!(isMarker(currentColor))) {
					rotationFlag = true;
				}
				System.out.println("currentColor " + currentColor);

				// if not white then robot turned for 90 degrees
				if (isMarker(currentColor) && rotationFlag) {
					rotationCounter++;
//						ev3.getAudio().systemSound(0);
					rotationFlag = false;

					System.out.println("rotationCounter " + rotationCounter);

					// if black or green then it's a road
					if (mapCreator != null && isActiveMarker(currentColor)) {
						mapCreator.updatePosition(rotationCounter);
					}
				}

				turnDirection(moveDirection);
				Delay.msDelay(controlTime);
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

				if (mapCreator.checkedPoints.size() == mapCreator.widthOfMap * mapCreator.heightOfMap - 1) {
					mapCreator.isMapCompleted = true;
					System.out.println("STOP");
				}
			}
		}
		stopMoving();
	}

	public int getColor() {
		float[] colorData = new float[colorIdSensor.sampleSize()];
		int oldColor = 0;
		int currentColor = 0;
		List<Integer> recentColors = new ArrayList<Integer>();

		float colorRed;
		float colorGreen;
		float colorBlue;
		int[] realColors;

		while (!ev3.getKey("Escape").isDown()) {
			colorIdSensor.fetchSample(colorData, 0);
			colorRed = colorData[0];
			colorGreen = colorData[1];
			colorBlue = colorData[2];
			currentColor = rgbToColor(colorRed, colorGreen, colorBlue);
			recentColors.add(currentColor);

			// update color every 60th iteration
			if (recentColors.size() == StackSize) {
				realColors = sortColors(recentColors, oldColor);
				currentColor = realColors[0];
				oldColor = realColors[1];
				recentColors.clear();
			}
		}
		return currentColor;
	}

	/*
	 * 0 - white 1 - black 2 - green 3 - red
	 */
	public int rgbToColor(float colorRed, float colorGreen, float colorBlue) {
		int red = (int) (colorRed * 255);
		int green = (int) (colorGreen * 255);
		int blue = (int) (colorBlue * 255);
		int color = 0;

		double diff = 1.2;

		int i = 0;
		do {
			i++;
		} while (red * i < 255 && green * i < 255 && blue * i < 255);
		i--;

		red = red * i + 1;
		green = green * i + 1;
		blue = blue * i + 1;

//		System.out.println("R: " + red + " G: " + green + " B: " + blue + " i: " + i);

		if (red / (double) green < diff && red / (double) blue < diff && green / (double) red < diff
				&& green / (double) blue < diff && blue / (double) red < diff && blue / (double) green < diff
				&& i < 6) {
			color = 0;
		} else if (green / (double) red > diff && green / (double) blue > diff && i < 10) {
			color = 2;
		} else if (red / (double) green > diff && red / (double) blue > diff && i < 10) {
			color = 3;
		} else {
			color = 1;
		}

		return color;
	}

	public int[] sortColors(List<Integer> recentColors, int oldColor) {
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

		if (maxValue != oldColor) {
			oldColor = maxValue;
		}
		return new int[] { maxValue, oldColor };
	}

//	@Override
//	public void move(int signalMarkAmount) {
//		leftMotor.setSpeed(ForwardSpeed);
//		rightMotor.setSpeed(ForwardSpeed);
//
//		Direction lastDirection = Direction.RIGHT;
//		int turningTime = 1000;
//		int moveTime = 500;
//		long currentTime;
//
//		boolean turningMode = false;
//		boolean alarmFlag = false;
//		long startTime = 0;
//		long signalMarkCounter = 0;
//
//		float[] colorData = new float[colorIdSensor.sampleSize()];
//		int oldColor = 0;
//		int currentColor = 0;
//		List<Integer> recentColors = new ArrayList<Integer>();
//		float colorRed;
//		float colorGreen;
//		float colorBlue;
//		
//		while ((!ev3.getKey("Escape").isDown()) & (signalMarkCounter < signalMarkAmount)) {
//			currentTime = System.currentTimeMillis();
//
//			colorIdSensor.fetchSample(colorData, 0);
//			colorRed = colorData[0];
//			colorGreen = colorData[1];
//			colorBlue = colorData[2];
//			currentColor = rgbToColor(colorRed, colorGreen, colorBlue);
//			recentColors.add(currentColor);
//
//			// update color every 60th iteration
//			if (recentColors.size() == StackSize) {
//				int[] realColors = sortColors(recentColors, currentColor, oldColor);
//				currentColor = realColors[0];
//				oldColor = realColors[1];
//				recentColors.clear();
//			}
//
////			printData(colorData);
//
//			if (isMarker(currentColor)) {
//				System.out.println("currentColor " + currentColor);
//				if (turningMode) {
//					stopMoving();
//				}
//
//				/* Проверка на прохождение метки */
//				if (isSignalMarker(currentColor)) {
//					alarmFlag = true;
//					ev3.getAudio().systemSound(0);
//				} else {
//					if (alarmFlag) {
//						alarmFlag = false;
//						signalMarkCounter++;
//					}
//				}
//				moveForward();
//				
//				if (currentTime - startTime >= moveTime) {
//					ev3.getAudio().systemSound(1);
//					stopMoving();					
//				}
//				
//				turningTime = 200;
//				turningMode = false;
//				startTime = currentTime;				
//				
//			} else {
//				if (currentTime - startTime > turningTime) {
//					stopMoving();
//
//					if (turningMode) {
//						if (lastDirection == Direction.RIGHT) {
//							lastDirection = Direction.LEFT;
//						} else {
//							lastDirection = Direction.RIGHT;
//						}
//					} else {
//						turningMode = true;
//					}
//
//					startTime = currentTime;
//					turningTime += 300;
//					turnDirection(lastDirection);
//					turningMode = true;
//				}
//			}
//		}
//
//		stopMoving();
//	}

	@Override
	public void move(int signalMarkAmount) {
		leftMotor.setSpeed(ForwardSpeed);
		rightMotor.setSpeed(ForwardSpeed);

		Direction lastDirection = Direction.RIGHT;
		int rotationTime = 1000;
		int moveTime = 450;
		long currentTime = 0;

		boolean turningMode = false;
//		boolean moveMode = false;
		long rotationStartTime = 0;
		long moveStartTime = 0;
		long signalMarkCounter = 0;

		float[] colorData = new float[colorIdSensor.sampleSize()];
		int oldColor = 0;
		int newColor = 0;
		int currentColor = 0;
		List<Integer> recentColors = new ArrayList<Integer>();
		float colorRed;
		float colorGreen;
		float colorBlue;
		int[] realColors;

		while ((!ev3.getKey("Escape").isDown()) && (signalMarkCounter < signalMarkAmount) 
//				|| ((currentTime - moveStartTime < moveTime) && (signalMarkCounter > 0))
//				|| (currentTime - moveStartTime < moveTime)
			) {
			currentTime = System.currentTimeMillis();

			colorIdSensor.fetchSample(colorData, 0);
			colorRed = colorData[0];
			colorGreen = colorData[1];
			colorBlue = colorData[2];
			newColor = rgbToColor(colorRed, colorGreen, colorBlue);
			recentColors.add(newColor);

			// update color every 60th iteration
			if (recentColors.size() == StackSize) {
				realColors = sortColors(recentColors, oldColor);
				currentColor = realColors[0];
				oldColor = realColors[1];
				recentColors.clear();
//				if (!moveMode) {
//					moveMode = true;					
//				}
			}
			System.out.println("currentColor: " + currentColor);
			
//			if (moveMode) {

				if (turningMode && isMarker(currentColor)) {
					stopMoving();
					turningMode = false;

					currentTime = System.currentTimeMillis();
				}

				if (isActiveMarker(currentColor)) {

					moveForward();
					moveStartTime = currentTime;

					rotationTime = 200;

					if (isSignalMarker(currentColor)) {
						ev3.getAudio().systemSound(0);
						signalMarkCounter++;
					}
				} else {
					if (signalMarkCounter == 0) {
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
							turnDirection(lastDirection);
						}
					} else {
						System.out.println(currentTime - moveStartTime);
						if (currentTime - moveStartTime > moveTime && !turningMode) {
							System.out.println("done");
							ev3.getAudio().systemSound(1);
							stopMoving();
							break;
						}
					}
				}
//			}
		}
		stopMoving();
	}

	@Override
	public void stop() {
		colorSensor.close();
		leftMotor.close();
		rightMotor.close();
	}

	@Override
	public void start() {
		colorSensor = new EV3ColorSensor(ev3.getPort("S1"));
		colorIdSensor = colorSensor.getRGBMode();

		leftMotor = new EV3LargeRegulatedMotor(ev3.getPort("D"));
		rightMotor = new EV3LargeRegulatedMotor(ev3.getPort("A"));
	}
}
