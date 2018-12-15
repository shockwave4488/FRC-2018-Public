package org.usfirst.frc.team4488.robot.systems;

import JavaRoboticsLib.ControlSystems.SimPID;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;

public class Shooter extends Subsystem {

  private double shooterPower;

  private WPI_TalonSRX shooterControl;
  private Logging logger;

  private Controllers xbox;

  private double desiredSpeed;

  private static Shooter sInstance;

  private SimPID pid;

  private int ticksPerRotation;
  private double scaleFactor;

  private double autoRPM;

  private double powerMultiplier;

  private double shooterP;
  private double shooterI;
  private double shooterD;

  public Shooter() {
    ticksPerRotation = 1024;
    scaleFactor = 331 / 270; // 0.0166666666666666;
    powerMultiplier = 0.003;
    shooterPower = 0;
    autoRPM = 2000; // find RPM
    pid = new SimPID();

    shooterControl = new WPI_TalonSRX(RobotMap.ShooterWheel);
    shooterControl.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    shooterControl.setSensorPhase(false);
    shooterControl.config_kF(0, 0.125, 0); // 0.09
    shooterControl.config_kP(0, 0.5, 0);
    shooterControl.config_kI(0, 0.001, 0); // 0.0125
    shooterControl.config_kD(0, 0.06, 0);

    SmartDashboard.putNumber("F", 0.125);
    SmartDashboard.putNumber("P", 0.5);
    SmartDashboard.putNumber("I", 0.001);
    SmartDashboard.putNumber("D", 0.06);

    logger = Logging.getInstance();
    logger.writeToLogFormatted(this, "start constructor");

    pid.setConstants(shooterP, shooterI, shooterD);
    pid.setDesiredValue(autoRPM);
  }

  private Loop mloop =
      new Loop() {
        public void onStart(double timestamp) {
          logger.writeToLogFormatted(this, "start of onStart");

          shooterControl.config_kF(0, SmartDashboard.getNumber("F", 0.125), 0);
          shooterControl.config_kP(0, SmartDashboard.getNumber("P", 0.5), 0);
          shooterControl.config_kI(0, SmartDashboard.getNumber("I", 0.001), 0);
          shooterControl.config_kD(0, SmartDashboard.getNumber("D", 0.06), 0);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }

          xbox = Controllers.getInstance();
        }

        public void onLoop(double timestamp) {

          double leftYstick =
              Controllers.getInstance().getLeftStickY(Controllers.getInstance().m_secondary);
          double motorOutput = shooterControl.getMotorOutputPercent();

          if (Controllers.getInstance().getY(Controllers.getInstance().m_secondary)) {

            double targetVelocity_unitsPer100ms = leftYstick * ticksPerRotation * 5000 / 600;
            shooterControl.set(ControlMode.Velocity, targetVelocity_unitsPer100ms);
            SmartDashboard.putNumber("targetVel", targetVelocity_unitsPer100ms);
          }

          // add code to react to camera

          shooterControl.set(ControlMode.Velocity, 3400);
          // shooterControl.set(1);
        }

        public void onStop(double timestamp) {
          shooterControl.set(0);
          logger.writeToLogFormatted(this, "end of onStop");
          stop();
        }
      };

  public static Shooter getInstance() {
    if (sInstance == null) {
      sInstance = new Shooter();
    }
    return sInstance;
  }

  private void setSpeed(double newSpeed) {
    if (desiredSpeed != newSpeed) {
      logger.writeToLogFormatted(this, "setSpeed(" + newSpeed + ")");
      desiredSpeed = newSpeed;
    }
  }

  public double getSpeed() {
    // return (shooterControl.getSensorCollection().getQuadratureVelocity() * (scaleFactor));
    return (shooterControl.getSensorCollection().getQuadratureVelocity());
  }

  public void on() {
    shooterControl.set(1);
  }

  public void off() {
    shooterControl.set(0);
  }

  @Override
  public void writeToLog() {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateSmartDashboard() {
    // TODO Auto-generated method stub
    SmartDashboard.putNumber("shooterRPM", getSpeed());
    SmartDashboard.putNumber("error", shooterControl.getClosedLoopError(0));
  }

  @Override
  public void stop() {
    // TODO Auto-generated method stub
    setSpeed(0);
    logger.writeToLogFormatted(this, "stopped");
  }

  @Override
  public void zeroSensors() {
    // TODO Auto-generated method stub

  }

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    // TODO Auto-generated method stub
    enabledLooper.register(mloop);
  }

  @Override
  public void updatePrefs() {
    // TODO Auto-generated method stub

  }

  @Override
  public void controllerUpdate() {
    // TODO Auto-generated method stub

  }

  @Override
  public void reset() {
    // TODO Auto-generated method stub

  }
}
