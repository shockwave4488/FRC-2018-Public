package org.usfirst.frc.team4488.robot.systems;

import JavaRoboticsLib.ControlSystems.SimPID;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.lib.util.PreferenceDoesNotExistException;
import org.usfirst.frc.team4488.lib.util.PreferencesParser;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Controllers.SecondaryState;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.HallEffect;
import org.usfirst.frc.team4488.robot.systems.PowerCore.ShifterState;

public class Forklift extends Subsystem {

  private PreferencesParser prefs;

  private SimPID pid;

  private HallEffect hallEffect;
  private HallEffect atCubeLevel;

  private double zeroLevelEncoderTicks;

  private double forkliftP;
  private double forkliftI;
  private double forkliftD;
  private double epsilon = 0;

  private double lowScoreHeight = 48.3; // A
  private double midScoreHeight = 58.3; // B
  private double highScoreHeight = 68.5; // Y

  private double power;

  private double desiredHeight;

  private double upPowerCeiling = 0.6;
  private double downPowerCeiling = -0.4;

  private boolean startedFirstHalfClimbSetup = false;
  private boolean isLocked = false;
  private boolean aPressed = false;
  private double climberDeployHeight = 1;
  private Controllers xbox;

  private static Forklift sInstance = null;

  private boolean routineRun = false;

  private IntakeAndHold routine;

  private Logging logger;
  private PowerCore powerCore;
  private Intake intake;

  public Forklift() {
    logger = Logging.getInstance();
    logger.writeToLogFormatted(this, "start of constructor");

    prefs = PreferencesParser.getInstance();
    try {
      forkliftP = prefs.getDouble("ForkliftP");
      forkliftI = prefs.getDouble("ForkliftI");
      forkliftD = prefs.getDouble("ForkliftD");
    } catch (PreferenceDoesNotExistException e) {
      forkliftP = 0.08;
      forkliftI = 0.006;
      forkliftD = 0.001;
      logger.writeToLogFormatted(this, "preferences error");
    }

    try {
      upPowerCeiling = prefs.getDouble("ForkliftUpCeiling");
      downPowerCeiling = prefs.getDouble("ForkliftDownCeiling");
    } catch (PreferenceDoesNotExistException e) {
      upPowerCeiling = 0.3;
      downPowerCeiling = -0.2;
      logger.writeToLogFormatted(this, "Couldn't find forklift power ceilings in prefs");
      System.out.println("Couldn't find forklift power ceilings in prefs");
    }

    pid = new SimPID(forkliftP, forkliftI, forkliftD, true);
    pid.setDoneRange(Constants.liftTeleDoneRange);
    pid.setErrorEpsilon(epsilon);
    pid.setMinDoneCycles(15);
    hallEffect = new HallEffect(RobotMap.FORKLIFT_HALL_EFFECT);
    atCubeLevel = new HallEffect(RobotMap.AT_CUBE_LEVEL_HALL_EFFECT);

    zeroLevelEncoderTicks = 0;
  }

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          logger.writeToLogFormatted(this, "start of Forklift onStart");
          // debugForklift(timestamp);
          setHeight(getCurrentHeight());
          powerCore = PowerCore.getInstance();
          intake = Intake.getInstance();
          xbox = Controllers.getInstance();
        }
        /**
         * If the power core is in the forklift state, it continuously updates to make sure the lift
         * is at the desired height. Calculates power to be used and sets power only if it passes
         * the safety check.
         */
        @Override
        public void onLoop(double timestamp) {

          if (powerCore.getShifterState() == ShifterState.Forklift) {
            if (!isLocked) {
              if (power < 0
                  && getCurrentHeight() < Constants.minHeightWithIntakeOut
                  && Math.abs(Manipulator.getInstance().getPosition()) > 1) {
                setHeight(Constants.minHeightWithIntakeOut);
              }

              if (power < 0
                  && getCurrentHeight() < Constants.liftSafetyCheckHeight
                  && !intake.isDeployed()
                  && !Manipulator.getInstance().isClamped()) {
                setHeight(Constants.liftSafetyCheckHeight);
              }

              pid.setDesiredValue(getDesiredHeight());
              power = pid.calcPID(getCurrentHeight());
              // @ TODO Need a dynamic way to change power depending on if we're going up or down
              if (power > upPowerCeiling) {
                power = upPowerCeiling;
              }
              if (power < downPowerCeiling) {
                power = downPowerCeiling;
              }
              SmartDashboard.putNumber("Power", power);
              if (safetyCheck(power)) {
                powerCore.setPower(power);
              } else {
                powerCore.setPower(0.0);
              }
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          reset();
          stop();
          routineRun = false;
          routine = null;
          logger.writeToLogFormatted(this, "ending Forklift onStop");
        }
      };
  /** @return Instance of forklift. */
  public static Forklift getInstance() {
    if (sInstance == null) {
      sInstance = new Forklift();
    }

    return sInstance;
  }

  public void setPower(double power) {
    powerCore.setPower(power);
  }

  /**
   * @param power
   * @return Safety checks for lift so it doesn't break.
   */
  private boolean safetyCheck(double power) {
    // make sure lift never extends over max height
    if (getCurrentHeight() >= Constants.liftHeightMaxInches && power > 0) {
      return false;
    }

    // make sure lift never goes lower than min height
    if (getCurrentHeight() <= Constants.liftHeightMinInches && power < 0) {
      return false;
    }

    return true;
  }

  public void changeMinDoneCycleCount(int cycles) {
    pid.setMinDoneCycles(cycles);
  }
  /** @return gets the minimun cycle count */
  public int getMinDoneCycleCount() {
    return pid.getMinDoneCycles();
  }

  public void setDoneRange(double doneRange) {
    pid.setDoneRange(doneRange);
  }

  public double getDoneRange() {
    return pid.getDoneRangeVal();
  }
  /** @return the current height of lift */
  public double getCurrentHeight() {
    /*
        double height;
        double rawTicks = powerCore.getEncoder(); // read encoder values for powercore
        rawTicks -= zeroLevelEncoderTicks;
        double beltDistanceInches =
            rawTicks
                / 1024
                * Constants.pulleyDiameterInches
                * Math.PI; // convert encoder values to inches
        height = beltDistanceInches * 3; // multiply by 3 to account for lift geometry
    */
    return (PowerCore.getInstance().getEncoder() - zeroLevelEncoderTicks)
        * Constants.practiceBotLiftTicksToInches;
  }

  public void setHeight(double desiredLiftHeight) {
    if (desiredLiftHeight != desiredHeight)
      logger.writeToLogFormatted(this, "setHeight(" + desiredLiftHeight + ")");
    if (desiredLiftHeight > Constants.liftHeightMaxInches) {
      desiredHeight = Constants.liftHeightMaxInches;
    } else if (desiredLiftHeight < Constants.liftHeightMinInches) {
      desiredHeight = Constants.liftHeightMinInches;
    } else {
      desiredHeight = desiredLiftHeight;
    }
  }

  public boolean atDesiredPosition() {
    return pid.isDone();
  }

  public void lift(double throttle) {
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
    powerCore.setPower(pwm);
  }

  /**
   * @param val
   * @param deadband
   * @return controller deadband handling
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

  /** @return gets the desired height */
  public double getDesiredHeight() {
    return desiredHeight;
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber("Forklift Current Height (Inches)", getCurrentHeight());
    SmartDashboard.putNumber(
        "Forklift Currrent Height (Ticks)",
        PowerCore.getInstance().getEncoder() - zeroLevelEncoderTicks);
    SmartDashboard.putNumber("Forklift Desired Height (Inches)", desiredHeight);
    SmartDashboard.putBoolean("Hall effect value", hallEffect.get());
    SmartDashboard.putBoolean("At Desired", atDesiredPosition());
    SmartDashboard.putBoolean("Lift Hall Effect", atCubeLevel());
  }

  @Override
  public void stop() {
    PowerCore.getInstance().setPower(0);
    logger.writeToLogFormatted(this, "stop()");
  }

  public void zeroAtCurrent() {
    zeroLevelEncoderTicks = PowerCore.getInstance().getEncoder();
  }

  @Override
  public void zeroSensors() {
    double val = PowerCore.getInstance().getEncoder();
    if (val < zeroLevelEncoderTicks) {
      zeroLevelEncoderTicks = val;
      // logger.writeToLogFormatted(this, "Zeroed Forklift");
    }
    if (hallEffect.get()) {
      // logger.writeToLogFormatted(this, "Zeroed Forklift");
      zeroLevelEncoderTicks = val;
    }
  }

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {
    prefs = PreferencesParser.getInstance();
    try {
      forkliftP = prefs.getDouble("ForkliftP");
      forkliftI = prefs.getDouble("ForkliftI");
      forkliftD = prefs.getDouble("ForkliftD");
    } catch (PreferenceDoesNotExistException e) {
      forkliftP = 0.01;
      forkliftI = 0;
      forkliftD = 0;
    }

    pid.setConstants(forkliftP, forkliftI, forkliftD);

    try {
      upPowerCeiling = prefs.getDouble("ForkliftUpCeiling");
      downPowerCeiling = prefs.getDouble("ForkliftDownCeiling");
    } catch (PreferenceDoesNotExistException e) {
      upPowerCeiling = 0.3;
      downPowerCeiling = -0.2;
      logger.writeToLogFormatted(this, "Couldn't find forklift power ceilings in prefs");
      System.out.println("Couldn't find forklift power ceilings in prefs");
    }
  }
  /**
   * When in auto mode and button x is pressed, lift moves to 48 inches. When in manual mode and the
   * left stick is pushed positive up, lift goes up, and when stick is pulled negative down, lift
   * goes down.
   */
  @Override
  public void controllerUpdate() {
    if (!isLocked) {
      double stickValue =
          handleDeadband(xbox.getLeftStickY(xbox.m_secondary), Constants.forkliftDeadband);
      if ((getDesiredHeight() + stickValue < Constants.minHeightWithIntakeOut && stickValue < 0)
          && (Math.abs(Manipulator.getInstance().getPosition()) > 1
              || !Manipulator.getInstance().isClamped() && !Intake.getInstance().isDeployed())) {
        setHeight(Constants.minHeightWithIntakeOut);
      }
      setHeight(getDesiredHeight() + stickValue);
    }

    if (xbox.getLeftStickPress(xbox.m_secondary)) {
      // RoutineManager.getInstance().addAction(new IdleLift());
      Manipulator.getInstance().clamp();
      Manipulator.getInstance().setPosition(0);
      setHeight(Constants.liftIdleHeight);
    }
    if (xbox.secondaryState == SecondaryState.auto) {
      if (xbox.getB(xbox.m_secondary)) {
        // RoutineManager.getInstance().addAction(new MoveLift(48));
        setHeight(midScoreHeight);
        Manipulator.getInstance().rotateUp();
      } else if (xbox.getA(xbox.m_secondary)) {
        setHeight(lowScoreHeight);
        Manipulator.getInstance().rotateUp();
      } else if (xbox.getY(xbox.m_secondary)) {
        setHeight(highScoreHeight);
        Manipulator.getInstance().rotateUp();
      }
    } else {
    }
    if (xbox.getA(xbox.m_primary)) {
      startedFirstHalfClimbSetup = true;
      if (!aPressed) {
        setHeight(Constants.climbBarHeight);
        intake.deployIn();
        Manipulator.getInstance().rotateUp();
      }
    } else if (startedFirstHalfClimbSetup) {
      setHeight(climberDeployHeight);
      if (atDesiredPosition()) {
        startedFirstHalfClimbSetup = false;
      }
    }

    aPressed = xbox.getA(xbox.m_primary);
  }

  /** @return return the status of the hall effect on the forklift */
  public boolean getHallEffect() {
    return hallEffect.get();
  }

  @Override
  public void writeToLog() {}

  @Override
  public void reset() {
    startedFirstHalfClimbSetup = false;
  }

  public void lock() {
    isLocked = true;
  }

  public void unLock() {
    isLocked = false;
  }

  public boolean atCubeLevel() {
    return atCubeLevel.get();
  }
}
