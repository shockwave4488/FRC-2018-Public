package org.usfirst.frc.team4488.robot.systems;

import JavaRoboticsLib.ControlSystems.SimPID;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.lib.util.PreferenceDoesNotExistException;
import org.usfirst.frc.team4488.lib.util.PreferencesParser;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.Robot;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.ArduinoAnalog;
import org.usfirst.frc.team4488.robot.systems.PowerCore.ShifterState;

public class Manipulator extends Subsystem {

  private WPI_TalonSRX m_slider1;
  private Solenoid clamp;
  private Solenoid rotater;

  private ArduinoAnalog arduinoAnalog;

  private SimPID pid;

  private PreferencesParser prefs = PreferencesParser.getInstance();

  private boolean canRotateDown = false;

  private int RAMPRATE = 0;
  private double desiredPosition = 0;
  private double desiredSpeed = 0; // Ticks per 100 MS
  private double ticksToInches = 835;
  private double ticksPerRev = 4096;
  private double diameter = 1.75;
  private double powerCeiling = 1;
  private boolean xLastPressed = false;
  private boolean rightBumperLastPressed = false;
  private boolean rightTriggerLastPressed = false;
  private boolean isClamped = false;
  private boolean isRotated = false;
  private boolean isLatched = false;
  private boolean hasCube = false;
  private boolean locked = false;

  private boolean scoreRoutineRun = false;

  private static Manipulator sInstance;

  private Logging logger = Logging.getInstance();
  private Controllers xbox;

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          canRotateDown = false;
          logger.writeToLogFormatted(this, "begin Manipulator.onStart");
          synchronized (Manipulator.this) {
            setPosition(getPosition());

            desiredPosition = getPosition();

            clamp();
            rotate(false);
            setHasCube(false);

            scoreRoutineRun = false;

            arduinoAnalog = ArduinoAnalog.getInstance();
            xbox = Controllers.getInstance();
          }
        }
        /** TODO */
        @Override
        public void onLoop(double timestamp) {

          synchronized (Manipulator.this) {
            if (Forklift.getInstance().getCurrentHeight() < Constants.overrideManipHeight
                && !Robot.isAuto
                && canRotateDown
                && PowerCore.getInstance().getShifterState() == ShifterState.Forklift) {
              rotateDown();
              setPosition(0);
            }

            if (!Robot.isAuto
                && Forklift.getInstance().getCurrentHeight() > Constants.liftSafetyCheckHeight) {
              canRotateDown = true;
            }
            if (!locked) {
              pid.setDesiredValue(desiredPosition);
              double power = pid.calcPID(getPosition());

              SmartDashboard.putNumber("ManipPower", power);
              if (getPosition() >= Constants.manipulatorMax && power > 0) {
                power = 0;
              } else if (getPosition() <= Constants.manipulatorMin && power < 0) {
                power = 0;
              }

              set(power);
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          rotateUp();
          clamp.set(false);
          isClamped = true;
          stop();
          logger.writeToLogFormatted(this, "ending Manipulator.onStop");
        }
      };
  /** @return gets instance of manipulator */
  public static Manipulator getInstance() {
    if (sInstance == null) {
      sInstance = new Manipulator();
    }

    return sInstance;
  }

  public Manipulator() {
    logger.writeToLogFormatted(this, "begin Manipulator Constructor");
    m_slider1 = new WPI_TalonSRX(RobotMap.MANIPULATOR_MOTOR);
    clamp = new Solenoid(RobotMap.MANIPULATOR_CLAMP);
    rotater = new Solenoid(RobotMap.MANIPULATOR_ROTATER);

    pid = new SimPID();

    try {
      pid.setConstants(
          prefs.getDouble("ManipulatorP"),
          prefs.getDouble("ManipulatorI"),
          prefs.getDouble("ManipulatorD"));
      pid.setErrorEpsilon(prefs.getDouble("ManipulatorEps"));
    } catch (PreferenceDoesNotExistException e) {
      pid.setConstants(0.01, 0, 0);
      System.out.println("PID caught");
    }
    pid.setDoneRange(0.125);

    m_slider1.configOpenloopRamp(RAMPRATE, 0);
    m_slider1.setNeutralMode(NeutralMode.Coast);
    m_slider1.setSensorPhase(true);
    m_slider1.setInverted(true);

    logger = Logging.getInstance();
  }

  public void lock() {
    locked = true;
  }

  public void unLock() {
    locked = false;
  }

  public void setPosition(double newPosition) {
    if (newPosition != desiredPosition)
      logger.writeToLogFormatted(this, "setPosition(" + newPosition + ")");
    if (desiredPosition > Constants.manipulatorSafeMax) {
      desiredPosition = Constants.manipulatorSafeMax;
    } else if (desiredPosition < Constants.manipulatorSafeMin) {
      desiredPosition = Constants.manipulatorSafeMin;
    } else {
      desiredPosition = newPosition;
    }
  }

  /** @return retrives the desired position of the manipulator */
  public double getDesiredPosition() {
    return pid.getDesiredVal();
  }

  /** @return gets the position of the manipulator */
  public double getPosition() {
    return m_slider1.getSensorCollection().getQuadraturePosition() / ticksToInches;
  }

  /** @return tells of the manipulator is at its wanted position */
  public boolean atDesiredPosition() {
    return pid.isDone();
  }

  public void setDoneRange(double doneRange) {
    pid.setDoneRange(doneRange);
  }

  public void clamp() {
    if (Forklift.getInstance().getCurrentHeight() > Constants.liftSafetyCheckHeight
        || Intake.getInstance().isDeployed()) {
      clamp.set(false);
      isClamped = true;
    }
  }

  public void unClamp() {
    if (Forklift.getInstance().getCurrentHeight() > Constants.liftSafetyCheckHeight
        || Intake.getInstance().isDeployed()) {
      clamp.set(true);
      isClamped = false;
      hasCube = false;
    }
  }
  /** @return tells if the clamp is clamped */
  public boolean isClamped() {
    return isClamped;
  }

  private void rotate(boolean val) {
    rotater.set(val);
    isRotated = val;
  }

  public void rotateDown() {
    rotate(true);
  }

  public void rotateUp() {
    rotate(false);
  }

  public boolean isRotated() {
    return isRotated;
  }

  private void set(double power) {
    if ((getPosition() > Constants.manipulatorMax && power > 0)
        || (getPosition() < Constants.manipulatorMin && power < 0)) {
      m_slider1.set(ControlMode.PercentOutput, 0);
    } else {
      m_slider1.set(ControlMode.PercentOutput, power);
    }
  }

  public void setPower(double power) {
    set(power);
  }

  public void bypassSetPower(double power) {
    m_slider1.set(ControlMode.PercentOutput, power);
  }

  public void slide(double throttle) {
    throttle = handleDeadband(throttle, .1);

    double pwm;
    double linearPower;

    linearPower = throttle;
    pwm = linearPower;

    if (pwm > 1.0) {
      pwm = 1.0;
    } else if (pwm < -1.0) {
      pwm = -1.0;
    }
    set(pwm);
  }

  /**
   * @param val
   * @param deadband
   * @return handles controller deadband
   */
  private double handleDeadband(double val, double deadband) {
    if (Math.abs(val) > Math.abs(deadband)) {
      if (val > 0) {
        return (val - deadband) / (1 - deadband);
      } else {
        return (val + deadband) / (1 - deadband);
      }
    } else {
      return 0.0;
    }
  }

  // Doesnt work
  public void driveToSpeed(double ips) {
    configSpeedControl();
    double ticksPer100MS =
        (ips * ticksToInches) / 10; // Divide by 10 to turn ticks per second into ticks per 100MS
    System.out.println(ticksPer100MS);
    desiredPosition = getPosition();
    m_slider1.set(ControlMode.Velocity, ticksPer100MS);
  }

  /** @return Status of the hall effect */
  public boolean getHallEffect() {
    return arduinoAnalog.getHallEffect();
  }

  @Override
  public void stop() {
    set(0);
    desiredPosition = getPosition();
    m_slider1.setNeutralMode(NeutralMode.Coast);
    logger.writeToLogFormatted(this, "stop()");
  }

  public void zeroAtCurrent() {
    m_slider1
        .getSensorCollection()
        .setQuadraturePosition((int) (Constants.manipulatorZeroOffset * ticksToInches), 0);
    logger.writeToLogFormatted(this, "Manipulator re-zeroed");
  }

  @Override
  public void zeroSensors() {
    arduinoAnalog = ArduinoAnalog.getInstance();
    if (!isLatched) {
      SmartDashboard.putBoolean("hall effect", getHallEffect());
      if (getHallEffect() && arduinoAnalog.isGoodData()) {
        m_slider1
            .getSensorCollection()
            .setQuadraturePosition((int) (Constants.manipulatorZeroOffset * ticksToInches), 0);
        isLatched = true;
        logger.writeToLogFormatted(this, "Manip zeroed");
      }
    }
  }

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {
    try {
      pid.setConstants(
          prefs.getDouble("ManipulatorP"),
          prefs.getDouble("ManipulatorI"),
          prefs.getDouble("ManipulatorD"));
      pid.setErrorEpsilon(prefs.getDouble("ManipulatorEps"));
    } catch (PreferenceDoesNotExistException e) {
      pid.setConstants(0.01, 0, 0);
      System.out.println("PID caught");
    }
    pid.setErrorEpsilon(0.125);
  }
  /**
   * If the x button is presses, the manipulator will clamp or unclamp. If the right stick is
   * pressed the manip will go to zero. If the right stick is moved right, the manip goes right. If
   * the right stick goes left, the manip goes left. If in auto state and the a button is pressed,
   * the routine to score on scale will run. If in manual mode and the b button is pressed the manip
   * will rotate or unrotate.
   */
  @Override
  public void controllerUpdate() {
    if (xbox.getX(xbox.m_secondary) && !xLastPressed) {
      if (isClamped) {
        unClamp();
      } else {
        clamp();
      }
    }
    xLastPressed = xbox.getX(xbox.m_secondary);

    if (xbox.getDPadPressed(xbox.m_secondary)) {
      rotateUp();
    }

    if (xbox.getRightStickPress(xbox.m_secondary)) {
      // RoutineManager.getInstance().addAction(new MoveManipulator(0));
      setPosition(0);
    }

    if (xbox.getRightBumper(xbox.m_secondary) && !rightBumperLastPressed) {
      rotate(!isRotated);
    }
    rightBumperLastPressed = xbox.getRightBumper(xbox.m_secondary);

    if (!locked && Forklift.getInstance().getCurrentHeight() > Constants.minSafeLiftHeight) {
      double stickValue = handleDeadband(xbox.getRightStickX(xbox.m_secondary), 0.15) / -1.5;
      stickValue = Math.pow(stickValue, 3);
      stickValue *= 2;
      if (stickValue != 0) {
        setPosition(getDesiredPosition() + stickValue);
      }
    }
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber(
        "Manipulator ticks", m_slider1.getSensorCollection().getQuadraturePosition());
    SmartDashboard.putNumber("Manipulator inches", getPosition());
    SmartDashboard.putNumber("Manipulator Desired Position", desiredPosition);
    SmartDashboard.putNumber("Manipulator Speed", m_slider1.getSelectedSensorVelocity(0));
    SmartDashboard.putBoolean("Manip Has Cube", hasCube);
  }

  @Override
  public void writeToLog() {}

  private void configSpeedControl() {
    m_slider1.set(ControlMode.Velocity, 0);
    m_slider1.configNominalOutputReverse(0, 0);
    m_slider1.configNominalOutputForward(0, 0);
    m_slider1.configPeakOutputReverse(-1, 0);
    m_slider1.configPeakOutputForward(1, 0);
    m_slider1.selectProfileSlot(0, 0);
    try {
      m_slider1.config_kP(0, prefs.getDouble("ManipulatorVelocityP"), 0);
      m_slider1.config_kI(0, prefs.getDouble("ManipulatorVelocityI"), 0);
      m_slider1.config_kD(0, prefs.getDouble("ManipulatorVelocityD"), 0);
      m_slider1.config_kF(0, prefs.getDouble("ManipulatorVelocityF"), 0);
    } catch (PreferenceDoesNotExistException e) {
      m_slider1.config_kP(0, 0.01, 0);
      m_slider1.config_kI(0, 0, 0);
      m_slider1.config_kD(0, 0, 0);
      m_slider1.config_kF(0, 0, 0);
    }

    m_slider1.config_IntegralZone(0, 0, 0);
    m_slider1.configClosedloopRamp(0, 0);
  }

  public void setCeiling(double newCeiling) {
    powerCeiling = newCeiling;
  }

  public void setHasCube(boolean val) {
    hasCube = val;
  }

  public boolean hasCube() {
    return hasCube;
  }

  @Override
  public void reset() {
    isLatched = false;
    hasCube = false;
  }

  public double getDoneRange() {
    return pid.getDoneRangeVal();
  }
}
