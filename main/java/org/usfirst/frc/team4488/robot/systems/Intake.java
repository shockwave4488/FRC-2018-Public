package org.usfirst.frc.team4488.robot.systems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Controllers.SecondaryState;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.BeamBreak;
import org.usfirst.frc.team4488.robot.sensors.HallEffect;

public class Intake extends Subsystem {

  private WPI_TalonSRX mLeft;
  private WPI_TalonSRX mRight;
  private Solenoid deployer;
  private BeamBreak hasCube1;
  private HallEffect leftHallEffect;

  private double RAMPRATE = 0.5;
  private boolean wantsDejam = false;
  private boolean ranExchangeIntake = false;
  private boolean leftTriggerPressed = false;
  private boolean leftTriggerLastPressed = false;
  private boolean rightBumperPressed = false;

  private Controllers xbox;

  private static Intake sInstance;

  private Logging logger;

  private boolean aLastPressed = false;

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          logger.writeToLogFormatted(this, "begin Intake onStart");
          stop();
          synchronized (Intake.this) {
            xbox = Controllers.getInstance();
          }
        }
        /**
         * Checks if we want to intake cube, if so we set motor to intake, else motors will not
         * move. If we want to dejam, we sent motors to dejam. If the manipulator is clamped TODO
         */
        @Override
        public void onLoop(double timestamp) {
          synchronized (Intake.this) {
            if (isDeployed() && !hasCube() && !wantsDejam) {
              mLeft.set(0.75);
              mRight.set(-0.3);
            } else if (wantsDejam) {
              mLeft.set(-1);
              mRight.set(1);
            } else if (isDeployed() && hasCube()) {
              mLeft.set(0.2);
              mRight.set(0);
            } else {
              mLeft.set(0);
              mRight.set(0);
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          reset();
          stop();
          logger.writeToLogFormatted(this, "ending Intake onStop");
        }
      };
  /** @return An instance of intake. */
  public static Intake getInstance() {
    if (sInstance == null) {
      sInstance = new Intake();
    }

    return sInstance;
  }

  public Intake() {
    logger = Logging.getInstance();
    logger.writeToLogFormatted(this, "begin constructor");

    mLeft = new WPI_TalonSRX(RobotMap.INTAKE_LEFT_MOTOR);
    mRight = new WPI_TalonSRX(RobotMap.INTAKE_RIGHT_MOTOR);
    deployer = new Solenoid(RobotMap.INTAKE_SOLENOID);

    mLeft.setNeutralMode(NeutralMode.Brake);
    mRight.setNeutralMode(NeutralMode.Brake);
    mLeft.configOpenloopRamp(RAMPRATE, 0);
    mRight.configOpenloopRamp(RAMPRATE, 0);

    hasCube1 = new BeamBreak(RobotMap.INTAKE_HAS_CUBE_1);
    leftHallEffect = new HallEffect(RobotMap.INTAKE_HALL_EFFECT_1);
  }

  private void deploy(boolean value) {
    if (value != deployer.get()) logger.writeToLogFormatted(this, "deploy()");
    deployer.set(value);
  }

  public void deployOut() {
    deploy(true);
  }

  public void deployIn() {
    if (Manipulator.getInstance().isClamped()
        || Forklift.getInstance().getCurrentHeight() > Constants.liftIdleHeight) {
      deploy(false);
    }
  }

  /** @return Tells if the intake is deployed or not. */
  public boolean isDeployed() {
    return deployer.get();
  }

  private void setDejam(boolean val) {
    wantsDejam = val;
  }

  public void intakeIn() {
    deploy(true);
    setDejam(false);
  }

  public void dejam() {
    deploy(true);
    setDejam(true);
  }

  public void stopIntake() {
    deploy(false);
    setDejam(false);
  }

  /** @return Tells if the intake has a cube or not. */
  public boolean hasCube() {
    return !hasCube1.get();
  }

  @Override
  public void stop() {
    mLeft.set(0);
    mRight.set(0);
    logger.writeToLogFormatted(this, "stop()");
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
   * If in manual state, and the left trigger is held the wantsIntake will be true. If right trigger
   * is held, the wantsdejam will be true. If the a button is pressed, intake will deploy or
   * undeploy.
   */
  @Override
  public void controllerUpdate() {
    if (xbox.getRightBumper(xbox.m_primary)) {
      dejam();
    } else if (rightBumperPressed) {
      stopIntake();
    }
    rightBumperPressed = xbox.getRightBumper(xbox.m_primary);

    if (xbox.secondaryState == SecondaryState.auto) {
      if (xbox.getRightTrigger(xbox.m_secondary)) {
        intakeIn();
      } else if (leftTriggerPressed && !hasCube()) {
        stopIntake();
      }
      leftTriggerPressed = xbox.getRightTrigger(xbox.m_secondary);
    } else {
      if (xbox.getLeftTrigger(xbox.m_secondary) && !leftTriggerLastPressed) {
        deploy(true);
      } else if (leftTriggerLastPressed) {
        deploy(false);
      }
      leftTriggerLastPressed = xbox.getLeftTrigger(xbox.m_secondary);

      wantsDejam = xbox.getRightTrigger(xbox.m_secondary);
      if (xbox.getA(xbox.m_secondary) && !aLastPressed) {
        deploy(!deployer.get());
      }
      aLastPressed = xbox.getA(xbox.m_secondary);
    }
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putBoolean("Has Cube 1", !hasCube1.get());
  }

  @Override
  public void writeToLog() {}

  @Override
  public void reset() {
    stopIntake();
    wantsDejam = false;
    aLastPressed = false;
  }

  public boolean isIn() {
    return leftHallEffect.get();
  }
}
