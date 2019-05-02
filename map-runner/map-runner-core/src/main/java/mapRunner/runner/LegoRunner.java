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
import mapRunner.map.RunnerLocation;
import mapRunner.map.structure.Road;

public class LegoRunner implements Runner {

    final static int MovementSpeed = 200;
    final static int RotationSpeed = 100;

    final static int StackSize = 8;
    final static int ControlTime = 150;

    float[] angleData;
    float[] colorData;
    int newColor = Color.WHITE;
    int currentColor = Color.BLACK;
    float startAngle;
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
    
    public void lunge(int dir) {
        rightMotor.setSpeed(MovementSpeed);
        leftMotor.setSpeed(MovementSpeed);
        RegulatedMotor[] syncList = {rightMotor};
        leftMotor.synchronizeWith(syncList);
        leftMotor.startSynchronization();
        leftMotor.rotate(180 * dir, true);
        rightMotor.rotate(180 * dir, true);
        leftMotor.endSynchronization();
        leftMotor.waitComplete();
        rightMotor.waitComplete();
    }
    
    public void lungeTacho() {
      int countL = leftMotor.getTachoCount();
      int countR = rightMotor.getTachoCount();
      rightMotor.setSpeed(MovementSpeed);
      leftMotor.setSpeed(MovementSpeed);
      RegulatedMotor[] syncList = {rightMotor};
      leftMotor.synchronizeWith(syncList);
      leftMotor.startSynchronization();
      leftMotor.rotate(-countL, true);
      rightMotor.rotate(-countR, true);
      leftMotor.endSynchronization();
      leftMotor.waitComplete();
      rightMotor.waitComplete();
  }

    public LegoRunner() {
        ev3 = BrickFinder.getLocal();

        gyroSensor = new EV3GyroSensor(ev3.getPort("S1"));
        angleSensor = gyroSensor.getAngleMode();
        angleData = new float[angleSensor.sampleSize()];
        gyroSensor.reset();
    }

    LegoRunner(Brick ev3, EV3ColorSensor colorSensor, EV3GyroSensor gyroSensor, EV3UltrasonicSensor ultrasonicSensor,
            RegulatedMotor leftMotor, RegulatedMotor rightMotor, SampleProvider rgbSensor) {
        this.ev3 = ev3;
        this.colorSensor = colorSensor;
        this.rgbSensor = rgbSensor;
        this.gyroSensor = gyroSensor;
        this.ultrasonicSensor = ultrasonicSensor;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public RunnerLocation location;
    
    @Override
    public RunnerLocation getRunnerLocation() {
        return location;
    }
    
    @Override
    public void setRunnerLocation(RunnerLocation location) {
        this.location = location;
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
    public void rotate(int rotationNumber) {
        Direction moveDirection = Direction.RIGHT;
        if (rotationNumber > 0) {
            moveDirection = Direction.LEFT;
        } else {
            moveDirection = Direction.RIGHT;
        }

//        updateAngle();
//        startAngle = angleData[0];
        startAngle = location.getAngle();
        System.out.println("startAngle " + angleData[0]);

        startRotation(moveDirection);

        while (!ev3.getKey("Escape").isDown()) {
            updateAngle();
            if (Math.abs(angleData[0] - startAngle) >= (float) (90 * Math.abs(rotationNumber) - 3)) {                
                stopMoving();
                Delay.msDelay(ControlTime);
                break;
            }
        }
    }

    public void detectRoads(MapCreator mapCreator) {
        if (mapCreator != null) {
            // TODO: make angle step shorter, but for now keep 90

            for (int rotationCounter = 0; rotationCounter < 4; rotationCounter++) {

//                System.out.println(rotationCounter);

                leftMotor.resetTachoCount();
                rightMotor.resetTachoCount();

                startMovingForward();

                while (!ev3.getKey("Escape").isDown()) {
                    colorMode();
                    if (!isActiveMarker(newColor) || leftMotor.getTachoCount() >= 100) {
                        stopMoving();
                        Delay.msDelay(ControlTime);
                        break;
                    }
                }

                colorMode();
                if (isActiveMarker(newColor)) {
                    mapCreator.updatePosition(rotationCounter);
                    System.out.println("rotationCounter: " + rotationCounter + " road");
                }

                lungeTacho();

//                lunge(1);
//                colorMode();
//                if (isActiveMarker(currentColor)) {
//                    mapCreator.updatePosition(rotationCounter);
//                    System.out.println("rotationCounter: " + rotationCounter + " road");
//                }

//                lunge(-1);

                rotate(1);

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
    }

//  @Override
//  public void rotate(int rotationNumber, MapCreator mapCreator) {
//      {
//          int rotationCounter = 0;
//          boolean rotationFlag = false;
//
//          Direction moveDirection = Direction.RIGHT;
//          if (rotationNumber > 0) {
//              moveDirection = Direction.LEFT;
//          } else {
//              moveDirection = Direction.RIGHT;
//          }
//
//          if (mapCreator != null) {
//              mapCreator.updateGrid(rotationCounter);
//          }
//
//          recentColors.clear();
//          currentColor = Color.WHITE;
//
//          startRotation(moveDirection);
//
//          while (!ev3.getKey("Escape").isDown()) {
//
//              if (rotationCounter == Math.abs(rotationNumber)) {
//                  stopMoving();
//                  Delay.msDelay(ControlTime);
//                  break;
//              }
//
//              colorMode(1);
//
//              // if color detected
//              // if white then keep rotating
//              if (!(isMarker(currentColor))) {
//                  rotationFlag = true;
//              }
////                System.out.println("currentColor " + currentColor);
//
//              angleSensor.fetchSample(angleData, 0);
////                System.out.println("angleData[0] " + angleData[0]);
//              // if not white then robot turned for 90 degrees
//              if ((isMarker(currentColor) && rotationFlag
//                      && (Math.abs(angleData[0]) > (float) (90 * (rotationCounter + 1) - 25)))) {
//                  rotationCounter++;
//                  rotationFlag = false;
//
//                  System.out.println("rotationCounter " + rotationCounter);
//
//                  // if black or green then it's a road
//                  if (mapCreator != null && isActiveMarker(currentColor)) {
//                      mapCreator.updatePosition(rotationCounter);
//                  }
//              }
//          }
//
//          if (mapCreator != null) {
//
//              int n = mapCreator.pointGrid[mapCreator.currentPointY][mapCreator.currentPointX];
//              mapCreator.checkedPoints.add(n);
//
//              System.out.println("listOfRoads after point:");
//              Iterator<?> iterator = mapCreator.listOfRoads.roads.iterator();
//              for (int i = 0; i < mapCreator.listOfRoads.roads.size(); i++) {
//                  Road r = (Road) iterator.next();
//                  System.out.println(r.startPoint + "=" + r.finishPoint);
//              }
//              System.out.println("W: " + mapCreator.widthOfMap + " H: " + mapCreator.heightOfMap + " p: ("
//                      + mapCreator.currentPointX + ";" + mapCreator.currentPointY + ")");
//
//              if (mapCreator.checkedPoints.size() == mapCreator.widthOfMap * mapCreator.heightOfMap) {
//                  mapCreator.isMapCompleted = true;
//                  System.out.println("STOP");
//              }
//          }
//      }
//      stopMoving();
//  }
    
    public void updateAngle() {
        angleSensor.fetchSample(angleData, 0);
//        location.setAngle(angleData[0]);
    }
    
    public float[] RGBtoHSB(int r, int g, int b, float[] hsbvals) {
        float hue, saturation, brightness;
        if (hsbvals == null) {
            hsbvals = new float[3];
        }
        int cmax = (r > g) ? r : g;
        if (b > cmax) cmax = b;
        int cmin = (r < g) ? r : g;
        if (b < cmin) cmin = b;

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0)
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        else
            saturation = 0;
        if (saturation == 0)
            hue = 0;
        else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax)
                hue = bluec - greenc;
            else if (g == cmax)
                hue = 2.0f + redc - bluec;
            else
                hue = 4.0f + greenc - redc;
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public int rgbToColor(float colorRed, float colorGreen, float colorBlue) {
//        System.out.println("cR: " + colorRed + " cG: " + colorGreen + " cB: " + colorBlue);
        int red = (int) (colorRed * 255);
        int green = (int) (colorGreen * 255);
        int blue = (int) (colorBlue * 255);
        int color = Color.WHITE;
        
        float[] hsb = RGBtoHSB(red, green, blue, null);
//        System.out.println("R: " + red + " G: " + green + " B: " + blue);
        
        float hue = hsb[0] * 360;
         
        float saturation = hsb[1] * 100;
         
        float brightness = hsb[2] * 1000;
        
//        System.out.println("H: " + hue + " S: " + saturation + " B: " + brightness);
        
        if (brightness > 52) {
            if (saturation < 30) {
                color = Color.WHITE;
            } else {
                if ((hue >= 0 && hue <= 30) || (hue >= 330 && hue <= 360)) {
                    color = Color.RED;
                } else if (hue >= 80 && hue <= 220) {
                    color = Color.GREEN;
                }
            }
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
    public void colorMode() {
        rgbSensor.fetchSample(colorData, 0);
        colorRed = colorData[0];
        colorGreen = colorData[1];
        colorBlue = colorData[2];
        newColor = rgbToColor(colorRed, colorGreen, colorBlue);
        recentColors.add(newColor);
//      System.out.println("newColor: " + newColor);

        // check list of recent colors at every iteration
        if (recentColors.size() > StackSize) {
            recentColors.remove(0);
        }
//      System.out.println("recentColors: "+recentColors);
        currentColor = sortColors(recentColors);
//      System.out.println("currentColor: " + currentColor);
    }

    @Override
    public void move(int signalMarkAmount) {
        getChargeLevel();

        leftMotor.setSpeed(MovementSpeed);
        rightMotor.setSpeed(MovementSpeed);

        Direction lastDirection = Direction.RIGHT;
        int rotationTime = 0;
        int moveTime = 500;
//        int searchTime = 500;
        long currentTime = 0;

        boolean turningMode = false;
        boolean searchMode = false;
        boolean isDirectionSwitched = false;
        boolean movingForward = false;
        boolean alarmFlag = true;
        long rotationStartTime = 0;
        long moveStartTime = 0;
        long searchStartTime = 0;
        long signalMarkCounter = 0;

        recentColors.clear();
        currentColor = Color.GREEN;

        while (!ev3.getKey("Escape").isDown()) {

            colorMode();
            updateAngle();

//            System.out.println("currentColor: " + currentColor);

            currentTime = System.currentTimeMillis();
            if (!isActiveMarker(newColor)) {
//                  if (signalMarkCounter == 0) {
                if (!searchMode) {
                    stopMoving();
                    movingForward = false;
                    searchMode = true;
                    searchStartTime = currentTime;
//                        System.out.println("backward");
                    startMovingBackward();
                    if (turningMode) {
                        rotationStartTime = 0;
                    }
                } else {
//                      System.out.println("currentTime - searchStartTime: " + (currentTime - searchStartTime)
//                              + " searchStartTime - moveStartTime: " + (searchStartTime - moveStartTime));
                    if (currentTime - searchStartTime > searchStartTime - moveStartTime) {
//                            System.out.println("forward but while searching");
                        startMovingForward();
                    }
                }
                if (!turningMode) {
                    if (!isDirectionSwitched) {
//                            System.out.println("switch direction");
                        if (lastDirection == Direction.RIGHT) {
//                                System.out.println("left");
                            lastDirection = Direction.LEFT;
                        } else {
//                                System.out.println("right");
                            lastDirection = Direction.RIGHT;
                        }
                        isDirectionSwitched = true;
                    }
                }
//                  }
            } else if (isActiveMarker(currentColor)) {
                if (searchMode) {
                    stopMoving();
                }
                if (searchMode || turningMode) {
//                        System.out.println("currentTime - rotationStartTime: " + (currentTime - rotationStartTime)
//                                + " rotationTime: " + rotationTime);
                    if (currentTime - rotationStartTime > rotationTime) {
                        stopMoving();
//                            System.out.println("turningMode: " + turningMode);
                        if (!turningMode) {
                            rotationStartTime = currentTime;
                            rotationTime += 100;
                            startRotation(lastDirection);
                            turningMode = true;
                            searchMode = false;
                        } else {
                            turningMode = false;
                            isDirectionSwitched = false;
                            rotationStartTime = 0;
//                                rotationTime = 0;
                        }
                    }
                } else {
                    if (movingForward) {
//                            System.out.println("currentTime - moveStartTime: " + (currentTime - moveStartTime)
//                                    + " rotationTime: " + rotationTime);
                        if ((currentTime - moveStartTime) > moveTime) {
                            rotationTime = 0;
                        }
                    } else {
                        movingForward = true;
//                            System.out.println("forward");
                        moveStartTime = currentTime;
                        startMovingForward();
                    }
                }

                if (isSignalMarker(currentColor)) {
                    if (!alarmFlag) {
                        // This delay is needed to match centers of sensor and green point
                        Delay.msDelay(55);
                        signalMarkCounter++;
                        if (signalMarkCounter >= signalMarkAmount) {
                            stopMoving();
                            Delay.msDelay(ControlTime);
                            break;
                        }
                        alarmFlag = true;
                    }
                } else {
                    alarmFlag = false;
                }
            }
        }
        stopMoving();
    }

//  @Override
//  public void move(int signalMarkAmount) {
//      leftMotor.setSpeed(MovementSpeed);
//      rightMotor.setSpeed(MovementSpeed);
//
//      Direction lastDirection = Direction.RIGHT;
//      int rotationTime = 1000;
//      int moveTime = 200;
//      long currentTime = 0;
//
//      boolean turningMode = false;
//        boolean alarmFlag  = false;
//      long rotationStartTime = 0;
//      long signalMarkCounter = 0;
//
//      recentColors.clear();
//      currentColor = Color.BLACK;
//
//      while (!ev3.getKey("Escape").isDown()) {
//          currentTime = System.currentTimeMillis();
//
//          colorMode(0);
//
////            System.out.println("currentColor: " + currentColor);
//
//          if (signalMarkCounter >= signalMarkAmount) {
//              Delay.msDelay(moveTime);
////                ev3.getAudio().systemSound(1);
//              stopMoving();
//              Delay.msDelay(ControlTime);
//              break;
////                }
//          } else {
//              if (turningMode && isMarker(currentColor)) {
//                  stopMoving();
//                  turningMode = false;
////                    System.out.println("stop turning");
//                  currentTime = System.currentTimeMillis();
//              }
//
//              if (isActiveMarker(currentColor)) {
//
//                  startMovingForward();
//
//                  rotationTime = 200;
//
//                    if (isSignalMarker(currentColor)) {
////                      System.out.println("green point");
//                        if (!alarmFlag) {
//                            ev3.getAudio().systemSound(0);
//                            signalMarkCounter++;
//                            alarmFlag = true;
//                        }
//                    } else {
//                        alarmFlag = false;
//                    }
//              }
//
//              if (!isActiveMarker(newColor)) {
//                  if (signalMarkCounter == 0) {
//                      if (isInactiveMarker(currentColor)) {
////                            System.out.println("red point");
//                          startMovingBackward();
//                      } else {
//                          currentColor = Color.WHITE;
//                          turningMode = true;
//                          if (currentTime - rotationStartTime > rotationTime) {
//                              stopMoving();
//
//                              if (lastDirection == Direction.RIGHT) {
//                                  lastDirection = Direction.LEFT;
//                              } else {
//                                  lastDirection = Direction.RIGHT;
//                              }
//
//                              rotationStartTime = currentTime;
//                              rotationTime += 300;
//                              startRotation(lastDirection);
//                          }
//                      }
//                  }
//              }
//          }
//      }
//      stopMoving();
//  }

    @Override
    public void load() {
        System.out.println("loading a cargo");
    }

    @Override
    public void stop() {
        colorSensor.close();
//      gyroSensor.close();
//      ultrasonicSensor.close();
        leftMotor.close();
        rightMotor.close();
    }

    @Override
    public void start() {
        colorSensor = new EV3ColorSensor(ev3.getPort("S4"));
        rgbSensor = colorSensor.getRGBMode();

//      ultrasonicSensor = new EV3UltrasonicSensor(ev3.getPort("S2"));
//        distanceSensor = ultrasonicSensor.getDistanceMode();

        leftMotor = new EV3LargeRegulatedMotor(ev3.getPort("D"));
        rightMotor = new EV3LargeRegulatedMotor(ev3.getPort("A"));

        colorData = new float[rgbSensor.sampleSize()];
    }
}
