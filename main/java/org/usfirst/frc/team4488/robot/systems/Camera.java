package org.usfirst.frc.team4488.robot.systems;

import edu.wpi.first.wpilibj.SerialPort;
import org.usfirst.frc.team4488.robot.loops.Loop;

public class Camera implements Loop {

  private SerialPort serial;

  private Double lastXRead = Double.NaN;
  private Double lastYRead = Double.NaN;

  private static Camera sInstance;

  public static Camera getInstance() {
    if (sInstance == null) {
      sInstance = new Camera();
    }

    return sInstance;
  }

  public Camera() {
    init();
  }

  private void init() {
    try {
      serial = new SerialPort(115200, SerialPort.Port.kUSB1);
      serial.setTimeout(0.05);
    } catch (Exception e) {
      System.out.println("Could not instantiate camera serial port");
    }
  }

  public double getX() {
    return lastXRead;
  }

  public double getY() {
    return lastYRead;
  }

  @Override
  public void onStart(double timestamp) {}

  @Override
  public void onLoop(double timestamp) {
    if (serial == null) {
      init();
      return;
    }

    String raw = serial.readString(50);
    try {
      String message = raw.split("&")[1];
      if (message.equals("None")) {
        lastXRead = Double.NaN;
        lastYRead = Double.NaN;
        return;
      }
      lastXRead = Double.valueOf(message.split(",")[0]);
      lastYRead = Double.valueOf(message.split(",")[1]);
    } catch (ArrayIndexOutOfBoundsException e) {
      System.out.println("Camera came unplugged / sent bad data");
      lastXRead = Double.NaN;
      lastYRead = Double.NaN;
      init();
    } catch (NumberFormatException e) {
      // Camera either sent bad data or doesnt see target
      lastXRead = Double.NaN;
      lastYRead = Double.NaN;
    }
  }

  @Override
  public void onStop(double timestamp) {}
}
