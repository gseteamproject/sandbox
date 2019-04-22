package mapRunner.runner;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Action;
import org.jmock.api.Invocation;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
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
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;

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

	SampleProvider rgbSensor_mock;

	EV3GyroSensor gyroSensor_mock;

	EV3UltrasonicSensor ultrasonicSensor_mock;

	RegulatedMotor leftMotor_mock;

	RegulatedMotor rightMotor_mock;

	@Before
	public void setUp() {
		ev3_mock = context.mock(Brick.class);
		colorSensor_mock = context.mock(EV3ColorSensor.class);
		rgbSensor_mock = context.mock(SampleProvider.class);
		gyroSensor_mock = context.mock(EV3GyroSensor.class);
//		ultrasonicSensor_mock = context.mock(EV3UltrasonicSensor.class);
		leftMotor_mock = context.mock(RegulatedMotor.class, "left-motor");
		rightMotor_mock = context.mock(RegulatedMotor.class, "right-motor");

		testable = new LegoRunner(ev3_mock, colorSensor_mock, gyroSensor_mock, ultrasonicSensor_mock, leftMotor_mock,
				rightMotor_mock, rgbSensor_mock);
	}

	@Test
	public void start() {
		final Port port_S1_mock = context.mock(Port.class, "S1");
		final UARTPort io_port_S1_mock = context.mock(UARTPort.class, "S1-io");

//        final Port port_S2_mock = context.mock(Port.class, "S2");
//        final UARTPort io_port_S2_mock = context.mock(UARTPort.class, "S2-io");

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

				oneOf(io_port_S1_mock).setMode(3);
				will(returnValue(true));

//                oneOf(ev3_mock).getPort("S2");
//                will(returnValue(port_S2_mock));
//
//                oneOf(port_S2_mock).open(UARTPort.class);
//                will(returnValue(io_port_S2_mock));
//
//                oneOf(io_port_S2_mock).setMode(0);
//                will(returnValue(true));

				oneOf(ev3_mock).getPort("S4");
				will(returnValue(port_S4_mock));

				oneOf(port_S4_mock).open(UARTPort.class);
				will(returnValue(io_port_S4_mock));

				oneOf(io_port_S4_mock).setMode(0);
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

//                oneOf(IRSensor_mock).close();

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
				oneOf(rightMotor_mock).setSpeed(LegoRunner.MovementSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.MovementSpeed);

				oneOf(rightMotor_mock).forward();

				oneOf(leftMotor_mock).forward();
			}
		});

		testable.startMovingForward();
	}

	@Test
	public void startMovingBackward() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.MovementSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.MovementSpeed);

				oneOf(rightMotor_mock).backward();

				oneOf(leftMotor_mock).backward();
			}
		});

		testable.startMovingBackward();
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

				oneOf(graphicsLCD_mock).getWidth();
				will(returnValue(width));

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[0]), width / 2, 0, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[1]), width / 2, 10, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).drawString(String.format("%.2f", data[2]), width / 2, 20, GraphicsLCD.HCENTER);

				oneOf(graphicsLCD_mock).refresh();
			}
		});

		testable.printData(data);
	}

	@Test
	public void startRotation_left() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(rightMotor_mock).forward();

				oneOf(leftMotor_mock).backward();
			}
		});

		testable.startRotation(Direction.LEFT);
	}

	@Test
	public void startRotation_right() {
		context.checking(new Expectations() {
			{
				oneOf(rightMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(leftMotor_mock).setSpeed(LegoRunner.RotationSpeed);

				oneOf(rightMotor_mock).backward();

				oneOf(leftMotor_mock).forward();
			}
		});

		testable.startRotation(Direction.RIGHT);
	}

	@Test
	public void isMarker() {
		Assert.assertFalse(testable.isMarker(Color.WHITE));
		Assert.assertTrue(testable.isMarker(Color.BLACK));
		Assert.assertTrue(testable.isMarker(Color.GREEN));
		Assert.assertTrue(testable.isMarker(Color.RED));
	}

	@Test
	public void isActiveMarker() {
		Assert.assertFalse(testable.isActiveMarker(Color.WHITE));
		Assert.assertTrue(testable.isActiveMarker(Color.BLACK));
		Assert.assertTrue(testable.isActiveMarker(Color.GREEN));
		Assert.assertFalse(testable.isActiveMarker(Color.RED));
	}

	@Test
	public void isInactiveMarker() {
		Assert.assertFalse(testable.isInactiveMarker(Color.WHITE));
		Assert.assertFalse(testable.isInactiveMarker(Color.BLACK));
		Assert.assertFalse(testable.isInactiveMarker(Color.GREEN));
		Assert.assertTrue(testable.isInactiveMarker(Color.RED));
	}

	@Test
	public void isSignalMarker() {
		Assert.assertFalse(testable.isSignalMarker(Color.WHITE));
		Assert.assertFalse(testable.isSignalMarker(Color.BLACK));
		Assert.assertTrue(testable.isSignalMarker(Color.GREEN));
		Assert.assertFalse(testable.isSignalMarker(Color.RED));
	}

	@Test
	public void sortColors() {
		final List<Integer> colors = Arrays.asList(new Integer[] { Color.BLACK, Color.BLACK, Color.GREEN, Color.GREEN,
				Color.RED, Color.WHITE, Color.GREEN });

		// TODO : check optimization
		Assert.assertEquals(Color.GREEN, testable.sortColors(colors));
	}

	@Test
	public void colorMode_move() {
		context.checking(new Expectations() {
			{
				oneOf(rgbSensor_mock).fetchSample(testable.colorData, 0);
				will(new Action() {
					@Override
					public void describeTo(Description arg0) {
					}

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						testable.colorData = new float[] { 1.0f, 1.0f, 1.0f };
						return null;
					}
				});
			}
		});

		// TODO : move recentColors to separate class
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);
		testable.recentColors.add(Color.WHITE);

		// TODO : add action constants
		testable.colorMode(0);
		Assert.assertEquals(testable.currentColor, Color.WHITE);
	}

	@Test
	public void colorMode_rotate() {
		context.checking(new Expectations() {
			{
				oneOf(rgbSensor_mock).fetchSample(testable.colorData, 0);
				will(new Action() {
					@Override
					public void describeTo(Description arg0) {
					}

					@Override
					public Object invoke(Invocation invocation) throws Throwable {
						testable.colorData = new float[] { 1.0f, 1.0f, 1.0f };
						return null;
					}
				});
			}
		});

		// TODO : add action constants
		testable.colorMode(1);
		Assert.assertEquals(testable.currentColor, Color.WHITE);
	}

	@Test
	public void load() {
		testable.load();
	}

	@Test
	public void rgbToColor_white() {
		Assert.assertEquals(Color.WHITE, testable.rgbToColor(1.0f, 1.0f, 1.0f));
	}

	@Test
	public void rgbToColor_black() {
		Assert.assertEquals(Color.BLACK, testable.rgbToColor(0.024f, 0.019f, 0.031f));
	}

	@Test
	public void rgbToColor_green() {
		Assert.assertEquals(Color.GREEN, testable.rgbToColor(0.10f, 0.99f, 0.1f));
	}

	@Test
	public void rgbToColor_red() {
		Assert.assertEquals(Color.RED, testable.rgbToColor(0.99f, 0.10f, 0.10f));
	}
}
