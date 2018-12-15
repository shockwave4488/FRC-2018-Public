package org.usfirst.frc.team4488.robot.systems;

import JavaRoboticsLib.ControlSystems.SimPID;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.lib.util.PreferenceDoesNotExistException;
import org.usfirst.frc.team4488.lib.util.PreferencesParser;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.sensors.Potentiometer;

public class Turret extends Subsystem {

  public enum TurretState {
    Tracking, // Sees target, lining up to it or on it
    Scanning, // Searching for target
    OffTarget;
  }

  private TurretState state = TurretState.OffTarget;

  private int error = 0;
  private int direction;
  private Double scanStartAngle;
  private final double onTargetRange = 15;
  private final double potAngleConversion = 0.188; // Not an actual value
  private final double scanSpeed = 0.2; // Not an actual value

  private Talon motor;
  private SimPID pid;
  private Potentiometer pot;

  private static Turret sInstance;

  private Loop mLoop =
      new Loop() {

        @Override
        public void onStart(double timestamp) {}

        @Override
        public void onLoop(double timestamp) {
          SmartDashboard.putNumber("Turret Pot", getAngle());

          switch (state) {
            case OffTarget:
              if (error != Double.NaN) { // NaN = target not on camera
                state = TurretState.Tracking;
              }
              break;
            case Scanning:
              if (error != Double.NaN) { // NaN = target not on camera
                scanStartAngle = null;
                state = TurretState.Tracking;
                motor.set(0);
                break;
              }

              if (scanStartAngle == null) {
                scanStartAngle = getAngle();
                direction = scanStartAngle < 0 ? 1 : -1;
              }
              if (Math.abs(getAngle() - scanStartAngle) > 45) direction *= -1;
              motor.set(scanSpeed * direction);
              break;
            case Tracking:
              if (error == Double.NaN) {
                state = TurretState.OffTarget;
                motor.set(0);
                break;
              }
              pid.setDesiredValue(0);
              double power = pid.calcPID(error);
              SmartDashboard.putNumber("Error", error);
              SmartDashboard.putNumber("Turret power", power);
              if ((power < 0 && getAngle() < -135) || (power > 0 && getAngle() > 135)) power = 0;
              power = Math.max(Math.min(power, 0.6), -0.6);
              motor.set(power);
              SmartDashboard.putNumber("Actual power", power);
              break;
          }
        }

        @Override
        public void onStop(double timestamp) {}
      };

  public static Turret getInstance() {
    if (sInstance == null) {
      sInstance = new Turret();
    }

    return sInstance;
  }

  public Turret() {
    motor = new Talon(RobotMap.TurretMotor);
    pid = new SimPID();
    pot = new Potentiometer(RobotMap.TurretPotentiometer);

    try {
      PreferencesParser prefs = PreferencesParser.getInstance();
      pid.setConstants(
          prefs.getDouble("TurretP"), prefs.getDouble("TurretI"), prefs.getDouble("TurretD"));
    } catch (PreferenceDoesNotExistException e) {
      pid.setConstants(0.004, 0.0003, 0);
    }
  }

  public double getAngle() {
    return (pot.get() - 2000) * potAngleConversion;
  }

  public void setError(int error) {
    this.error = error;
  }

  public void scan() {
    state = TurretState.Scanning;
  }

  public boolean onTarget() {
    return state == TurretState.Tracking && Math.abs(error) < onTargetRange;
  }

  @Override
  public void writeToLog() {}

  @Override
  public void updateSmartDashboard() {}

  @Override
  public void stop() {}

  @Override
  public void zeroSensors() {}

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {}

  @Override
  public void controllerUpdate() {}

  @Override
  public void reset() {}
}
