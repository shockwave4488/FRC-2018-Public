package org.usfirst.frc.team4488.robot.systems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.autonomous.actions.Wait;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.PowerCore.ShifterState;

public class Climber extends Subsystem {
  private boolean climb = false;
  private boolean zeroLatched = false;
  private boolean finishedClimb = false;
  private boolean yPressed = false;
  private int zeroLevelEncoderTicks = 0;
  private double ticksPerInch = 262.8624;
  private double climbRamp = 0.25;

  private static Climber sInstance = null;
  private PowerCore powerCore;
  private Solenoid buddyBar;

  private Logging logger;
  private Controllers xbox;

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          stop();
          synchronized (Climber.this) {
            logger = Logging.getInstance();
            powerCore = PowerCore.getInstance();
            xbox = Controllers.getInstance();
          }
        }
        /**
         * If the power core is in the climber state, and climb is true, the climber will turn on,
         * else the climber is off.
         */
        @Override
        public void onLoop(double timestamp) {
          synchronized (Climber.this) {
            if (powerCore.getShifterState() == ShifterState.Climber) {
              if (climb) {
                climberOn();
              } else {
                climberOff();
              }
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          reset();
          stop();
        }
      };
  /** @return An instance of climber. */
  public static Climber getInstance() {
    if (sInstance == null) {
      sInstance = new Climber();
    }

    return sInstance;
  }

  public Climber() {
    logger = Logging.getInstance();
    buddyBar = new Solenoid(RobotMap.BUDDY_BAR_SOLENOID);
    climb = false;
  }
  /** @param val Sets true if climber is climbing and false if climber isn't climbing. */
  public void setClimb(boolean val) {
    climb = val;
  }

  private void climberOn() {
    logger.writeToLogFormatted(this, "Climber On!");
    powerCore.setPower(-1);
  }

  private void climberOff() {
    powerCore.setPower(0.0);
  }

  public boolean isClimbing() {
    return climb;
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber("Climber Inches", getEncoder());
  }

  @Override
  public void stop() {
    PowerCore.getInstance().setPower(0);
  }

  @Override
  public void zeroSensors() {}

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {}
  /**
   * In auto mode, when the left bumper is pressed the intake is pulled in, checks that if power
   * core state is in climber, if not state is set to climber, TODO
   */
  @Override
  public void controllerUpdate() {
    Controllers c = Controllers.getInstance();

    boolean yIsPressed = c.getY(c.m_primary);
    if (yIsPressed && !finishedClimb) {
      logger.writeToLogFormatted(this, "Climbing, height: " + getEncoder());
      Intake.getInstance().deployIn();
      Manipulator.getInstance().rotateUp();
      buddyBar.set(true);
      if (!(powerCore.getShifterState() == ShifterState.Climber)) {
        powerCore.setShifterState(ShifterState.Climber);
        powerCore.setRamprate(climbRamp);
        if (!zeroLatched) {
          setZeroLevelEncoderTicks(powerCore.getEncoder());
          zeroLatched = true;
        }
        Wait wait = new Wait(250);
        wait.runOnce();
      }

      if (getEncoder() < Constants.climbHeightInches) {
        setClimb(true);
      } else {
        climbCleanup();
        finishedClimb = true;
      }
    } else {
      if (yPressed) {
        climbCleanup();
      }
    }
    yPressed = yIsPressed;
  }

  @Override
  public void writeToLog() {}

  public int getEncoder() {
    return (int)
        ((-1 * (PowerCore.getInstance().getEncoder() - zeroLevelEncoderTicks)) / ticksPerInch);
  }

  public void setZeroLevelEncoderTicks(int ticks) {
    zeroLevelEncoderTicks = ticks;
  }

  private void climbCleanup() {
    setClimb(false);
    PowerCore.getInstance().setPower(0);
    Wait wait = new Wait(100);
    wait.runOnce();
  }

  public void reset() {
    climbCleanup();
    PowerCore.getInstance().setShifterState(ShifterState.Forklift);
    PowerCore.getInstance().setRamprate(0);
    finishedClimb = false;
    zeroLatched = false;
  }
}
