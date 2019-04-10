package mapRunner.runner;

import java.util.Arrays;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lejos.hardware.Brick;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.MotorRegulator;
import lejos.hardware.port.Port;
import lejos.hardware.port.TachoMotorPort;
import lejos.hardware.port.UARTPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;

public class LegoRunnerTest {

	private final Mockery context = new Mockery() {
		{
			this.setImposteriser(ClassImposteriser.INSTANCE);
		}
	};

	@After
	public void tearDown() {
		context.assertIsSatisfied();
	}

	LegoRunner testable;

	Brick ev3_mock;

	EV3ColorSensor colorSensor_mock;

	EV3GyroSensor gyroSensor_mock;

	RegulatedMotor leftMotor_mock;

	RegulatedMotor rightMotor_mock;

	@Before
	public void setUp() {
		ev3_mock = context.mock(Brick.class);
		colorSensor_mock = context.mock(EV3ColorSensor.class);
		gyroSensor_mock = context.mock(EV3GyroSensor.class);
		leftMotor_mock = context.mock(RegulatedMotor.class, "left-motor");
		rightMotor_mock = context.mock(RegulatedMotor.class, "right-motor");

		testable = new LegoRunner(ev3_mock, colorSensor_mock, gyroSensor_mock, leftMotor_mock, rightMotor_mock);
	}

	@Test
	public void start() {
		final Port port_S1_mock = context.mock(Port.class, "S1");
		final UARTPort io_port_S1_mock = context.mock(UARTPort.class, "S1-io");

		final Port port_S4_mock = context.mock(Port.class, "S4");
		final UARTPort io_port_S4_mock = context.mock(UARTPort.class, "S4-io");

		final Port port_D_mock = context.mock(Port.class, "D");
		final TachoMotorPort io_port_D_mock = context.mock(TachoMotorPort.class, "D-io");
		final MotorRegulator regulator_D_mock = context.mock(MotorRegulator.class, "D-regulator");

		final Port port_A_mock = context.mock(Port.class, "A");
		final TachoMotorPort io_port_A_mock = context.mock(TachoMotorPort.class, "A-io");
		final MotorRegulator regulator_A_mock = context.mock(MotorRegulator.class, "A-regulator");

		context.checking(new Expectations() {
			{
				oneOf(ev3_mock).getPort("S1");
				will(returnValue(port_S1_mock));

				oneOf(port_S1_mock).open(UARTPort.class);
				will(returnValue(io_port_S1_mock));

				oneOf(io_port_S1_mock).setMode(0);
				will(returnValue(true));

				oneOf(ev3_mock).getPort("S4");
				will(returnValue(port_S4_mock));

				oneOf(port_S4_mock).open(UARTPort.class);
				will(returnValue(io_port_S4_mock));

				oneOf(io_port_S4_mock).setMode(3);
				will(returnValue(true));

				oneOf(ev3_mock).getPort("D");
				will(returnValue(port_D_mock));

				oneOf(port_D_mock).open(TachoMotorPort.class);
				will(returnValue(io_port_D_mock));

				oneOf(io_port_D_mock).getRegulator();
				will(returnValue(regulator_D_mock));

				oneOf(regulator_D_mock).setControlParamaters(9, 4.0f, 0.04f, 10.0f, 2.0f, 0.02f, 8.0f, 0);

				oneOf(ev3_mock).getPort("A");
				will(returnValue(port_A_mock));

				oneOf(port_A_mock).open(TachoMotorPort.class);
				will(returnValue(io_port_A_mock));

				oneOf(io_port_A_mock).getRegulator();
				will(returnValue(regulator_A_mock));

				oneOf(regulator_A_mock).setControlParamaters(9, 4.0f, 0.04f, 10.0f, 2.0f, 0.02f, 8.0f, 0);
			}
		});

		testable.start();
	}

	@Test
	public void stop() {
		context.checking(new Expectations() {
			{
				oneOf(colorSensor_mock).close();

				oneOf(gyroSensor_mock).close();

				oneOf(leftMotor_mock).close();

				oneOf(rightMotor_mock).close();
			}
		});

		testable.stop();
	}

	@Test
	public void stopMoving() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).stop(true);

				oneOf(leftMotor_mock).stop(true);
			}
		});

		testable.stopMoving();
	}

	@Test
	public void moveForward() {
		context.checking(new Expectations() {
			{
				// TODO: change ForwardSpeed to MovementSpeed
				oneOf(rightMotor_mock).setSpeed(LegoRunner.ForwardSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.ForwardSpeed);

				oneOf(rightMotor_mock).forward();

				oneOf(leftMotor_mock).forward();
			}
		});

		// TODO: rename method to "startMovingForward"
		testable.moveForward();
	}

	@Test
	public void moveBackwards() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.ForwardSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.ForwardSpeed);

				oneOf(rightMotor_mock).backward();

				oneOf(leftMotor_mock).backward();
			}
		});

		// TODO: rename method to "startMovingBackward"
		testable.moveBackwards();
	}

	@Test
	public void printData() {
		final GraphicsLCD graphicsLCD_mock = context.mock(GraphicsLCD.class);
		final float data[] = new float[] { 1.0f, 2.0f, 3.0f };
		final int width = 100;

		context.checking(new Expectations() {
			{
				oneOf(ev3_mock).getGraphicsLCD();
				will(returnValue(graphicsLCD_mock));

				oneOf(graphicsLCD_mock).setFont(with(Font.getSmallFont()));

				oneOf(graphicsLCD_mock).clear();

				// TODO : calculate x position before cycle
				oneOf(graphicsLCD_mock).getWidth();
				will(returnValue(width));

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[0]), width / 2, 0, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).getWidth();
				will(returnValue(width));

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[1]), width / 2, 10, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).getWidth();
				will(returnValue(width));

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[2]), width / 2, 20, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).refresh();
			}
		});

		testable.printData(data);
	}

	@Test
	public void turnDirection_left() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(rightMotor_mock).forward();

				oneOf(leftMotor_mock).backward();
			}
		});

		// TODO : rename method to "startRotation"
		testable.turnDirection(Direction.LEFT);
	}

	@Test
	public void turnDirection_right() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(rightMotor_mock).backward();

				oneOf(leftMotor_mock).forward();
			}
		});

		testable.turnDirection(Direction.RIGHT);
	}

	@Test
	public void isMarker() {
		// TODO : add color constants
		int white = 0;
		int black = 1;
		int green = 2;
		int red = 3;
		// TODO : add "break" into switch statement
		Assert.assertFalse(testable.isMarker(white));
		Assert.assertTrue(testable.isMarker(black));
		Assert.assertTrue(testable.isMarker(green));
		Assert.assertTrue(testable.isMarker(red));
	}

	@Test
	@Ignore
	public void isActiveMarker() {
		// TODO : add color constants
		int white = 0;
		int black = 1;
		int green = 2;
		int red = 3;
		// TODO : add "break" into switch statement
		Assert.assertFalse(testable.isMarker(white));
		Assert.assertTrue(testable.isMarker(black));
		Assert.assertTrue(testable.isMarker(green));
		// FIXME
		Assert.assertFalse(testable.isMarker(red));
	}

	@Test
	public void isInactiveMarker() {
		// TODO : add color constants
		int white = 0;
		int black = 1;
		int green = 2;
		int red = 3;
		// TODO : add "break" into switch statement
		Assert.assertFalse(testable.isInactiveMarker(white));
		Assert.assertFalse(testable.isInactiveMarker(black));
		Assert.assertFalse(testable.isInactiveMarker(green));
		Assert.assertTrue(testable.isInactiveMarker(red));
	}

	@Test
	public void isSignalMarker() {
		// TODO : add color constants
		int white = 0;
		int black = 1;
		int green = 2;
		int red = 3;
		// TODO : add "break" into switch statement
		Assert.assertFalse(testable.isSignalMarker(white));
		Assert.assertFalse(testable.isSignalMarker(black));
		Assert.assertTrue(testable.isSignalMarker(green));
		Assert.assertFalse(testable.isSignalMarker(red));
	}

	@Test
	public void sortColors() {
		final List<Integer> colors = Arrays.asList(new Integer[] { 1, 1, 2, 2, 3, 0, 2 });

		// TODO : check optimization
		Assert.assertEquals(2, testable.sortColors(colors));
	}

	@Test
	public void colorMode_move() {
		testable.colorData = new float[] {};
		// TODO : add action constants
		testable.colorMode(0);
	}

	@Test
	public void colorMode_rotate() {
		// TODO : add action constants
		testable.colorMode(1);
	}

	@Test
	public void rgbToColor_white() {
		// TODO: add color constant
		Assert.assertEquals(0, testable.rgbToColor(0.0f, 0.0f, 0.0f));
	}

	@Test
	public void rgbToColor_black() {
		// TODO: add color constant
		Assert.assertEquals(1, testable.rgbToColor(0.10f, 0.10f, 0.10f));
	}

	@Test
	@Ignore
	public void rgbToColor_green() {
		// TODO: add color constant
		Assert.assertEquals(2, testable.rgbToColor(0.01f, 0.10f, 0.01f));
	}

	@Test
	@Ignore
	public void rgbToColor_red() {
		// TODO: add color constant
		Assert.assertEquals(3, testable.rgbToColor(0.10f, 0.01f, 0.01f));
	}
}
