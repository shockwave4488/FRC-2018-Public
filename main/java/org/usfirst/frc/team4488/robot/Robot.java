package org.usfirst.frc.team4488.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.app.RobotState;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeExecuter;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeSelector;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.loops.RobotStateEstimator;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.ArduinoAnalog;
import org.usfirst.frc.team4488.robot.systems.Climber;
import org.usfirst.frc.team4488.robot.systems.Drive;
import org.usfirst.frc.team4488.robot.systems.Forklift;
import org.usfirst.frc.team4488.robot.systems.Intake;
import org.usfirst.frc.team4488.robot.systems.Manipulator;
import org.usfirst.frc.team4488.robot.systems.SubsystemManager;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the IterativeRobot documentation. If you change the name of this class
 * or the package after creating this project, you must also update the manifest file in the
 * resource directory.
 */
public class Robot extends IterativeRobot {
  private Drive drive;
  private Looper looper;
  private Looper constantLooper;

  private AutoModeExecuter mAutoModeExecuter = null;
  private RobotState robotState = RobotState.getInstance();
  private Logging logger = Logging.getInstance();
  public static Timer timer;
  private SubsystemManager subsystemManager;
  private boolean driveExists;
  public static boolean isAuto = false;
  /** Run once as soon as code is loaded onto the RoboRio */
  @Override
  public void robotInit() {
    logger.writeRaw("Robot Started!");
    logger.addTrackable(() -> Forklift.getInstance().getCurrentHeight(), "ForkliftHeight", 5);
    logger.addTrackable(() -> Manipulator.getInstance().getPosition(), "ManipPosition", 10);

    looper = new Looper();
    constantLooper = new Looper();

    subsystemManager = new SubsystemManager(RobotMap.robotSelector());

    if (RobotMap.driveExists == true) {
      drive = Drive.getInstance();
    }

    AutoModeSelector.init();

    looper.register(RobotStateEstimator.getInstance());
    subsystemManager.registerEnabledLoops(looper);

    constantLooper.register(ArduinoAnalog.getInstance());
    constantLooper.start();
  }

  /** This function is called periodically during all modes */
  @Override
  public void robotPeriodic() {
    subsystemManager.updateSmartDashboard();
    robotState.updateSmartDashboard();
    logger.update();
  }

  /** Run once at the beginning of autonomous mode */
  @Override
  public void autonomousInit() {
    isAuto = true;
    logger.writeToLogFormatted(this, "autonomousInit()");
    looper.start();
    if (mAutoModeExecuter != null) {
      mAutoModeExecuter.stop();
    }

    if (RobotMap.driveExists) {
      drive.resetAngle();
    }
    mAutoModeExecuter = null;
    Forklift.getInstance().setDoneRange(Constants.liftAutoDoneRange);
    subsystemManager.zeroSensors();
    wait(250); // Encoder values and other sensor data is not valid until 250ms after they are reset

    mAutoModeExecuter = new AutoModeExecuter();
    mAutoModeExecuter.setAutoMode(AutoModeSelector.getSelectedAutoMode());
    mAutoModeExecuter.start();
  }

  @Override
  public void teleopInit() {
    isAuto = false;
    logger.writeToLogFormatted(this, "teleopInit()");
    if (RobotMap.driveExists == true) {
      drive.configPercentVbus();
      drive.UnBreakModeAll();
    }

    looper.start();
    AutoModeSelector.updateFMS();
    Forklift.getInstance().setDoneRange(Constants.liftTeleDoneRange);
    Climber.getInstance().reset();
  }

  /** This function is called periodically during autonomous */
  @Override
  public void autonomousPeriodic() {}

  /** This function is called periodically during operator control */
  @Override
  public void teleopPeriodic() {
    Controllers xbox = Controllers.getInstance();

    SmartDashboard.putBoolean("Intake is in", Intake.getInstance().isIn());

    if (xbox.getStart(xbox.m_secondary)) {
      subsystemManager.reset();
    }

    subsystemManager.controllerUpdates();
  }

  @Override
  public void testInit() {
    logger.writeToLogFormatted(this, "testInit()");
  }

  /** This function is called periodically during test mode */
  @Override
  public void testPeriodic() {}
  /** This function is called once as soon as the robot is disabled */
  @Override
  public void disabledInit() {
    logger.writeToLogFormatted(this, "Robot Disabled!");

    if (mAutoModeExecuter != null) {
      mAutoModeExecuter.stop();
    }
    mAutoModeExecuter = null;

    subsystemManager.updatePrefs();
    subsystemManager.reset();
    subsystemManager.stop();
    looper.stop();
    drive.UnBreakModeAll();

    logger.flush();
  }

  @Override
  public void disabledPeriodic() {
    subsystemManager.zeroSensors();
  }

  private void wait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
