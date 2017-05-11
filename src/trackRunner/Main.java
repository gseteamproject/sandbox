
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Main {
	
	final static float MaxReflectedLight = 0.2f;
	final static int ForwardSpeed = 300;
	final static int RotationSpeed = 100;
	
	public static void main(String[] args){
		
		Brick ev3 = BrickFinder.getLocal();

		EV3ColorSensor cs = new EV3ColorSensor(ev3.getPort("S1"));
		SensorMode sensor = cs.getRedMode();
		
		RegulatedMotor left = new EV3LargeRegulatedMotor(ev3.getPort("D"));
		RegulatedMotor right = new EV3LargeRegulatedMotor(ev3.getPort("A"));
		
		left.setSpeed(ForwardSpeed);
		right.setSpeed(ForwardSpeed);
		
		Direction lastDirection = Direction.RIGHT;	
		int turningTime = 1000;
		
		boolean turningMode = false;
		long startTime = 0;
		
		while(!ev3.getKey("Escape").isDown()) {
			long currentTime = System.currentTimeMillis();
			
			float[] data = new float[sensor.sampleSize()];
			sensor.fetchSample(data, 0);
			
			printData(ev3, data);
			
			float reflectedLight = data[0];
			
			if (reflectedLight < MaxReflectedLight){
				if (turningMode){
					stopMoving(left, right);
				}
				moveForward(left, right);
				turningTime = 200;
				turningMode = false;
				startTime = currentTime;
			}
			else{
				if (currentTime - startTime > turningTime){					
					stopMoving(left, right);
					
					if (turningMode){
						if (lastDirection == Direction.RIGHT){
							lastDirection = Direction.LEFT;
						} else {
							lastDirection = Direction.RIGHT;
						}
					} 
					else {
						turningMode = true;
					}
					
					startTime = currentTime;
					turningTime += 300;
					turnDirection(left, right, lastDirection);
					turningMode = true;
				}
			}
		}
	}
	
	public static void printData(Brick ev3, float[] data){
		GraphicsLCD lcd = ev3.getGraphicsLCD();
		lcd.setFont(Font.getSmallFont());
	
		
		lcd.clear();
		for (int i = 0; i < data.length; ++i){
			lcd.drawString(String.format("%.2f", data[i]), lcd.getWidth()/2, i*10, GraphicsLCD.HCENTER);
		}
		lcd.refresh();
	}
	
	public static void moveForward(RegulatedMotor left, RegulatedMotor right){
		right.setSpeed(ForwardSpeed);
		left.setSpeed(ForwardSpeed);
		left.forward();
		right.forward();
	}
	
	public static void stopMoving(RegulatedMotor left, RegulatedMotor right){
		left.stop(true);
		right.stop(true);
	}
	
	public static void turnDirection(RegulatedMotor left, RegulatedMotor right, Direction direction){
		right.setSpeed(RotationSpeed);
		left.setSpeed(RotationSpeed);
		if (direction == Direction.LEFT){
			right.forward();
			left.backward();
		}
		else{
			left.forward();
			right.backward();
		}
	}
	
	private enum Direction { LEFT, RIGHT }
}
