package org.usfirst.frc.team4488.robot.sensors;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Climber;

public class ArduinoAnalog implements Loop {

  private SerialPort serial;
  private Climber climber;

  private boolean hallEffect;
  private double bounceback;

  private static ArduinoAnalog sInstance;

  @Override
  public void onStart(double timestamp) {}

  @Override
  public void onLoop(double timestamp) {
    if (!climber.isClimbing()) {
      update();
      outputToSmartDashboard();
    }
  }

  @Override
  public void onStop(double timestamp) {}

  public static ArduinoAnalog getInstance() {
    if (sInstance == null) {
      sInstance = new ArduinoAnalog();
    }

    return sInstance;
  }

  public ArduinoAnalog() {
    reInit();
    climber = Climber.getInstance();
  }

  public void update() {
    String raw = "";
    if (serial != null) {
      raw = serial.readString(50);
    }

    try {
      String[] data = raw.split("&")[1].split(":");
      SmartDashboard.putNumber("Manip Bounceback String", Double.valueOf(data[0]));
      SmartDashboard.putString("Hall Effect String", data[1]);
      hallEffect = Integer.valueOf(data[1]) == 0;
      bounceback = Double.valueOf(data[0]);
    } catch (ArrayIndexOutOfBoundsException e) {
      bounceback = -1;
      reInit();
      System.out.println("Arduino unplugged / sending bad data!");
    } catch (NumberFormatException e) {
      bounceback = -1;
      System.out.println("Arduino sent bad data");
    }
    // toReturn = removeNoise(toReturn);
  }

  private void reInit() {
    try {
      serial = new SerialPort(9600, SerialPort.Port.kUSB);
      serial.setTimeout(0.05);
    } catch (Exception e) {
      System.out.println("Could not instantiate arduino");
    }
  }

  public void outputToSmartDashboard() {
    if (!isGoodData()) {
      SmartDashboard.putBoolean("Good arduino data", false);
      Logging.getInstance().writeToLogFormatted(this, "Bad arduino data");
    } else {
      SmartDashboard.putBoolean("Hall effect", hallEffect);
      SmartDashboard.putBoolean("Good arduino data", true);
    }
  }

  public double getBounceback() {
    return bounceback;
  }

  public boolean getHallEffect() {
    return hallEffect;
  }

  public boolean isGoodData() {
    return getBounceback() != -1;
  }
}
