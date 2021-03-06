package org.usfirst.frc.team4488.robot.systems;

import JavaRoboticsLib.Drive.DriveHelper;
import JavaRoboticsLib.Drive.Interfaces.TankDrive;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VelocityMeasPeriod;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.lib.util.NavX;
import org.usfirst.frc.team4488.lib.util.app.control.Lookahead;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.control.PathFollower;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Twist2d;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.RobotName;
import org.usfirst.frc.team4488.robot.app.Kinematics;
import org.usfirst.frc.team4488.robot.app.RobotState;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;

public class Drive extends Subsystem implements TankDrive {

  public enum DriveGear {
    LowGear,
    HighGear;
  }

  public enum DriveControlState {
    PathFollowing,
    PercentVbus,
    VelocitySetpoint;
  }

  private DriveControlState driveControlState;

  private static final double RAMPRATE = 0; // Seconds from 0 to full power

  private WPI_TalonSRX m_left;
  private WPI_TalonSRX m_right;
  private WPI_TalonSRX rightSlave1;
  private WPI_TalonSRX rightSlave2;
  private WPI_TalonSRX leftSlave1;
  private WPI_TalonSRX leftSlave2;

  private DriveGear newGear;
  private DriveGear currentGear;
  private DriveHelper driveHelper;

  private Solenoid shifter;

  private double wheelDiameter;
  private double ticksPerRotation;

  private NavX m_navx;

  private Controllers xbox;

  private PathFollower mPathFollower;
  private Path mCurrentPath = null;
  private RobotState mRobotState = RobotState.getInstance();

  private Logging logger;

  private SmartDrive smartDrive;

  /*
   * Main State Machine Loop for Drive.
   */
  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {

          logger.writeToLogFormatted(this, "begin Loop.onStart");

          if (RobotMap.driveExists == true) {
            driveHelper = new DriveHelper(Drive.getInstance(), 0.1, 0.15);
          }

          stop();
          synchronized (Drive.this) {
            xbox = Controllers.getInstance();
          }
        }
        /**
         * Check if we want to change the gear, if the gear wants to be changed, it updates the
         * gears. TODO rest of it.
         */
        @Override
        public void onLoop(double timestamp) {
          synchronized (Drive.this) {
            if (newGear != currentGear) {
              gearShiftUpdate();
            }
            switch (driveControlState) {
              case VelocitySetpoint:
                return;
              case PercentVbus:
                return;
              case PathFollowing:
                if (mPathFollower != null) {
                  updatePathFollower(timestamp);
                }
                return;
              default:
                break;
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          stop();
          logger.writeToLogFormatted(this, "ending Loop.onStop");
        }
      };

  private static Drive sInstance = null;
  /** @return An instance of drive. */
  public static Drive getInstance() {
    if (sInstance == null) {
      sInstance = new Drive();
    }

    return sInstance;
  }

  public Drive() {

    logger = Logging.getInstance();
    logger.writeToLogFormatted(this, "constructor started");
    m_navx = NavX.getInstance();

    if (RobotMap.robotName == RobotName.PracticeBot) {
      wheelDiameter = Constants.practiceWheelDiameter;
      ticksPerRotation = Constants.practiceEncoderTicks;
    } else if (RobotMap.robotName == RobotName.ProgrammingPlatform) {
      wheelDiameter = Constants.programmingWheelDiameter;
      ticksPerRotation = Constants.programmingEncoderTicks;
    } else {
      wheelDiameter = 0;
      ticksPerRotation = 0;
    }

    try {

      m_left = new WPI_TalonSRX(RobotMap.DriveMotorLeftM);
      leftSlave1 = new WPI_TalonSRX(RobotMap.DriveMotorLeft2);
      leftSlave2 = new WPI_TalonSRX(RobotMap.DriveMotorLeft3);
      leftSlave1.follow(m_left);
      leftSlave2.follow(m_left);

      m_right = new WPI_TalonSRX(RobotMap.DriveMotorRightM);
      rightSlave1 = new WPI_TalonSRX(RobotMap.DriveMotorRight2);
      rightSlave2 = new WPI_TalonSRX(RobotMap.DriveMotorRight3);
      rightSlave1.follow(m_right);
      rightSlave2.follow(m_right);

      NeutralMode neutralModeToUse = NeutralMode.Brake;
      m_left.setNeutralMode(neutralModeToUse);
      leftSlave1.setNeutralMode(neutralModeToUse);
      leftSlave2.setNeutralMode(neutralModeToUse);
      m_right.setNeutralMode(neutralModeToUse);
      rightSlave1.setNeutralMode(neutralModeToUse);
      rightSlave2.setNeutralMode(neutralModeToUse);

      m_left.configOpenloopRamp(RAMPRATE, 0);
      leftSlave1.configOpenloopRamp(RAMPRATE, 0);
      leftSlave2.configOpenloopRamp(RAMPRATE, 0);
      m_right.configOpenloopRamp(RAMPRATE, 0);
      rightSlave1.configOpenloopRamp(RAMPRATE, 0);
      rightSlave2.configOpenloopRamp(RAMPRATE, 0);

      // inverts for test platform and practice bot are switched
      if (RobotMap.robotName == RobotMap.robotName.PracticeBot) {
        m_left.setInverted(true);
        leftSlave1.setInverted(true);
        leftSlave2.setInverted(true);
        m_right.setInverted(false);
        rightSlave1.setInverted(false);
        rightSlave2.setInverted(false);

        // right sensor phase = true for practice & = false for comp
        m_right.setSensorPhase(false);
        m_left.setSensorPhase(false);
      } else {
        m_left.setInverted(false);
        leftSlave1.setInverted(false);
        leftSlave2.setInverted(false);
        m_right.setInverted(true);
        rightSlave1.setInverted(true);
        rightSlave2.setInverted(true);

        m_right.setSensorPhase(false);
        m_left.setSensorPhase(true);
      }
      m_left.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, 0); // 254 uses 10 MS
      m_left.configVelocityMeasurementWindow(64, 0);
      m_right.configVelocityMeasurementPeriod(VelocityMeasPeriod.Period_100Ms, 0); // 254 uses 10 MS
      m_right.configVelocityMeasurementWindow(64, 0);

      // Set the feedback encoder type for the close-loop velocity control
      m_left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
      m_right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

      reloadGains(); // update PID constants in talons
      if (RobotMap.robotName == RobotName.PracticeBot) {
        shifter = new Solenoid(RobotMap.DriveGearShiftSolenoid);
      }

      newGear = DriveGear.LowGear;
      currentGear = DriveGear.LowGear;
      driveControlState = DriveControlState.PathFollowing;

      configPercentVbus();

      resetEncoders();
      resetAngle();

      smartDrive = new SmartDrive(this);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  /**
   * Sets the CIM motors on the West Coast Drive train to the leftPower and rightPower. Essential
   * for the TankDrive interface
   */
  public void setPowers(double leftPower, double rightPower) {
    if (driveControlState == DriveControlState.PercentVbus) {
      m_left.set(ControlMode.PercentOutput, leftPower);
      m_right.set(ControlMode.PercentOutput, rightPower * Constants.driftCorrection);
    }
  }

  /** @return Angle of robot. */
  public double getAngle() {
    return m_navx.getAHRS().getYaw();
  }

  public void setGyroAngle(Rotation2d angle) {
    m_navx.reset();
    m_navx.setAngleAdjustment(angle);
  }

  /*
   * Returns a Rotation2d object that contains the sin and cos of the current
   * robot angle. Used by RobotState to determine current robot angle
   */
  public Rotation2d getAngleRotation2d() {
    return m_navx.getYaw();
  }

  public void resetAngle() {
    m_navx.zeroYaw();
  }
  /** @return Gyroscope. */
  public AHRS getGyroscope() {
    return m_navx.getAHRS();
  }
  /** @return Left talon. */
  public WPI_TalonSRX getLeftTalon() {
    return m_left;
  }
  /** @return Right talon. */
  public WPI_TalonSRX getRightTalon() {
    return m_right;
  }
  /** @return Left distance traveled in inches. */
  public double getLeftDistance() {
    return (m_left.getSelectedSensorPosition(0)) * wheelDiameter * Math.PI / ticksPerRotation;
  }
  /** @return Right distance traveled in inches. */
  public double getRightDistance() {
    return (m_right.getSelectedSensorPosition(0))
        * wheelDiameter
        * Math.PI
        / ticksPerRotation; // In
    // inches
  }

  /** @return Left Speed in inches per second. */
  public double getLeftSpeed() {
    return ticksPer100MsToInchesPerSecond(m_left.getSelectedSensorVelocity(0));
  }
  /** @return Right speed in inches per second. */
  public double getRightSpeed() {
    return ticksPer100MsToInchesPerSecond(m_right.getSelectedSensorVelocity(0));
  }
  /** @return Linear distance in inches. */
  public double getLinearDistance() {
    return (getLeftDistance() + getRightDistance()) / 2;
  }
  /** @return Linear speed in inches per second. */
  public double getLinearSpeed() {
    double leftSpeed = getLeftSpeed();
    double rightSpeed = getRightSpeed();
    double linearSpeed = (leftSpeed + rightSpeed) / 2;
    return linearSpeed;
  }

  public void resetEncoders() {
    m_left.getSensorCollection().setQuadraturePosition(0, 0);
    m_right.getSensorCollection().setQuadraturePosition(0, 0);
  }

  public void BreakModeAll() {
    logger.writeToLogFormatted(this, "start of BreakModeAll");
    m_left.setNeutralMode(NeutralMode.Brake);
    leftSlave1.setNeutralMode(NeutralMode.Brake);
    leftSlave2.setNeutralMode(NeutralMode.Brake);
    m_right.setNeutralMode(NeutralMode.Brake);
    rightSlave1.setNeutralMode(NeutralMode.Brake);
    rightSlave2.setNeutralMode(NeutralMode.Brake);
  }

  public void UnBreakModeAll() {
    logger.writeToLogFormatted(this, "start of UnBreakModeAll");
    m_left.setNeutralMode(NeutralMode.Coast);
    leftSlave1.setNeutralMode(NeutralMode.Coast);
    leftSlave2.setNeutralMode(NeutralMode.Coast);
    m_right.setNeutralMode(NeutralMode.Coast);
    rightSlave1.setNeutralMode(NeutralMode.Coast);
    rightSlave2.setNeutralMode(NeutralMode.Coast);
  }

  private void gearShiftUpdate() {
    if (RobotMap.robotName == RobotName.PracticeBot) {
      if (newGear == DriveGear.HighGear) {
        shifter.set(true);
        currentGear = DriveGear.HighGear;
      } else {
        shifter.set(false);
        currentGear = DriveGear.LowGear;
      }
    }
  }

  public void setDriveShiftButton(boolean val) {
    if (val) {
      newGear = DriveGear.HighGear;
    } else {
      newGear = DriveGear.LowGear;
    }
  }
  /** @return newGear as GearState. */
  public DriveGear GearState() {
    return newGear;
  }

  /** @return True once an APP path following operation is complete. */
  public boolean isDoneWithPath() {
    if (driveControlState == DriveControlState.PathFollowing && mPathFollower != null) {
      return mPathFollower.isFinished();
    } else {

      return true;
    }
  }

  /*
   * Called at the beginning of an APP path following operation. Creates the
   * PathFollower that will be used to determine robot behavior during the main
   * loop
   */
  public void setWantDrivePath(Path path, boolean reversed) {
    logger.writeToLogFormatted(this, "start of setWantDrivePath");
    if (mCurrentPath != path || driveControlState != DriveControlState.PathFollowing) {
      configureTalonsForSpeedControl();
      mRobotState.resetDistanceDriven();
      mPathFollower =
          new PathFollower(
              path,
              reversed,
              new PathFollower.Parameters(
                  new Lookahead(
                      Constants.kMinLookAhead,
                      Constants.kMaxLookAhead,
                      Constants.kMinLookAheadSpeed,
                      Constants.kMaxLookAheadSpeed),
                  Constants.kInertiaSteeringGain,
                  Constants.kPathFollowingProfileKp,
                  Constants.kPathFollowingProfileKi,
                  Constants.kPathFollowingProfileKv,
                  Constants.kPathFollowingProfileKffv,
                  Constants.kPathFollowingProfileKffa,
                  Constants.kPathFollowingMaxVel,
                  Constants.kPathFollowingMaxAccel,
                  Constants.kPathFollowingGoalPosTolerance,
                  Constants.kPathFollowingGoalVelTolerance,
                  Constants.kPathStopSteeringDistance));
      driveControlState = DriveControlState.PathFollowing;
      mCurrentPath = path;
    } else {
      setVelocitySetpoint(0, 0);
    }
  }
  /**
   * @param marker
   * @return whether we have passed the marker or not in our APP path
   */
  public synchronized boolean hasPassedMarker(String marker) {
    if (driveControlState == DriveControlState.PathFollowing && mPathFollower != null) {
      return mPathFollower.hasPassedMarker(marker);
    } else {
      return false;
    }
  }

  @Override
  public void updateSmartDashboard() {

    double left_speed = getLeftSpeed();
    double right_speed = getRightSpeed();
    SmartDashboard.putNumber("DriveL Speed", left_speed);
    SmartDashboard.putNumber("DriveR Speed", right_speed);
    SmartDashboard.putNumber("Drive Speed", getLinearSpeed());
    SmartDashboard.putNumber("DriveL Pos", getLeftDistance());
    SmartDashboard.putNumber("DriveR Pos", getRightDistance());
    SmartDashboard.putNumber("Drive Pos", getLinearDistance());
    SmartDashboard.putNumber("Drive Angle", getAngle());
    SmartDashboard.putNumber("NavX YAW", m_navx.getAHRS().getYaw());
    SmartDashboard.putNumber("Raw Ticks", m_left.getSelectedSensorPosition(0));

    if (usesTalonVelocityControl(driveControlState)) {
      SmartDashboard.putNumber(
          "left speed error (ips)",
          ticksPer100MsToInchesPerSecond(m_left.getClosedLoopTarget(0)) - left_speed);
      SmartDashboard.putNumber(
          "right speed error (ips)",
          ticksPer100MsToInchesPerSecond(m_right.getClosedLoopTarget(0)) - right_speed);
    } else {
      SmartDashboard.putNumber("left speed error (ips)", 0.0);
      SmartDashboard.putNumber("right speed error (ips)", 0.0);
    }
    SmartDashboard.putNumber("Lticks/100ms", m_left.getSelectedSensorVelocity(0));

    SmartDashboard.putNumber("Rticks/100ms", m_right.getSelectedSensorVelocity(0));

    synchronized (this) {
      if (usesTalonVelocityControl(DriveControlState.PathFollowing) && mPathFollower != null) {
        /*Cross Track Error is the distance from the tip of the look ahead to the nearest point on the path
         * If things are going smoothly this will be very small
         */
        SmartDashboard.putNumber("drive CTE", mPathFollower.getCrossTrackError());
        SmartDashboard.putNumber("drive ATE", mPathFollower.getAlongTrackError());
      } else {
        SmartDashboard.putNumber("drive CTE", 0.0);
        SmartDashboard.putNumber("drive ATE", 0.0);
      }
    }
  }

  @Override
  public void stop() {}

  @Override
  public void zeroSensors() {
    resetEncoders();
  }

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {
    smartDrive.updatePrefs();
  }

  /*
   * Called from onLoop during an APP path following operation. Reads the
   * RobotState and determines what velocities should be set in the left and right
   * motors in order to stay on track.
   */
  private void updatePathFollower(double timestamp) {
    RigidTransform2d robot_pose = mRobotState.getLatestFieldToVehicle().getValue();
    Twist2d command =
        mPathFollower.update(
            timestamp,
            robot_pose,
            mRobotState.getDistanceDriven(),
            mRobotState.getPredictedVelocity().dx);
    if (!mPathFollower.isFinished()) {
      Kinematics.DriveVelocity setpoint = Kinematics.inverseKinematics(command);
      updateVelocitySetpoint(setpoint.left, setpoint.right);
    } else {
      updateVelocitySetpoint(0, 0);
    }
  }

  /*
   * Sets both the left and right motor velocities when the robot is in velocity
   * control mode. This is used during an APP path following operation to
   * continually set the motor speeds.
   */
  private synchronized void updateVelocitySetpoint(
      double left_inches_per_sec, double right_inches_per_sec) {
    if (usesTalonVelocityControl(driveControlState)) {
      final double max_desired =
          Math.max(Math.abs(left_inches_per_sec), Math.abs(right_inches_per_sec));
      final double scale =
          max_desired > Constants.kDriveHighGearMaxSetpoint
              ? Constants.kDriveHighGearMaxSetpoint / max_desired
              : 1.0;
      double leftVelocityTicksPer100MS =
          inchesPerSecondToTicksPer100MS(left_inches_per_sec * scale);
      double rightVelocityTicksPer100MS =
          inchesPerSecondToTicksPer100MS(right_inches_per_sec * scale);
      m_left.set(ControlMode.Velocity, leftVelocityTicksPer100MS);
      m_right.set(ControlMode.Velocity, rightVelocityTicksPer100MS);
    } else {
      m_left.set(0);
      m_right.set(0);
    }
  }

  /**
   * @return True if we are in velcoity control mode (mode used during an APP path following
   *     operation)
   */
  protected static boolean usesTalonVelocityControl(DriveControlState state) {
    if (state == DriveControlState.VelocitySetpoint || state == DriveControlState.PathFollowing) {
      return true;
    }
    return false;
  }

  private double ticksPer100MsToInchesPerSecond(double ticksPer100Ms) {
    double inchesPerSecond;
    inchesPerSecond = (ticksPer100Ms / ticksPerRotation) * wheelDiameter * Math.PI * 10; // Gives
    return inchesPerSecond;
  }

  private double inchesPerSecondToTicksPer100MS(double inches_per_second) {
    return (inches_per_second / (wheelDiameter * Math.PI) * ticksPerRotation) / 10;
  }

  /**
   * @ Used to switch our motor controllers into velocity control mode (called before we begin
   * following a path in APP). In velocity mode the Talons use a PID with feedforward gain to
   * maintain a specified velocity. Gains are set in the Drive constructor, velocities are updated
   * in updateVelocitySetpoints()
   */
  public void configureTalonsForSpeedControl() {
    logger.writeToLogFormatted(this, "start of configureTalonsForSpeedControl");
    if (!usesTalonVelocityControl(driveControlState)) {
      // We entered a velocity control state.
      m_left.set(ControlMode.Velocity, 0);
      m_left.configNominalOutputReverse(0, 0);
      m_left.configNominalOutputForward(0, 0);
      m_left.configPeakOutputReverse(-1, 0);
      m_left.configPeakOutputForward(1, 0);
      m_left.selectProfileSlot(0, 0);

      m_right.set(ControlMode.Velocity, 0);
      m_right.configNominalOutputReverse(0, 0);
      m_right.configNominalOutputForward(0, 0);
      m_right.configPeakOutputReverse(-1, 0);
      m_right.configPeakOutputForward(1, 0);
      m_right.selectProfileSlot(0, 0);
      BreakModeAll();
      reloadGains();
    }
  }

  public synchronized void setVelocitySetpoint(
      double left_inches_per_sec, double right_inches_per_sec) {
    configureTalonsForSpeedControl();
    driveControlState = DriveControlState.VelocitySetpoint;
    updateVelocitySetpoint(left_inches_per_sec, right_inches_per_sec);
  }
  /**
   * Checks if we want to change the gear. If drive exists and xbox isn't null, sets left and right
   * power. If the power doesn't equal 0, we log the power in the logger. Uses drive helper to drive
   * using the left and right power.
   */
  public void controllerUpdate() {
    setDriveShiftButton(xbox.getLeftBumper(xbox.m_primary));

    if (RobotMap.driveExists == true && xbox != null) {
      double[] powers =
          calcPowers(xbox.getLeftStickY(xbox.m_primary), xbox.getRightStickX(xbox.m_primary));
      driveHelper.Drive(powers[0], powers[1]); // throttle, turn
    }
  }

  public double[] calcPowers(double throttle, double turn) {
    double rightPower = Math.pow(turn, 3) / (-0.25 * Math.abs(throttle) + 1.25);
    double leftPower = Math.pow(throttle, 3);
    return new double[] {leftPower, rightPower};
  }

  public void configPercentVbus() {
    logger.writeToLogFormatted(this, "start of configPercentVbus");
    driveControlState = DriveControlState.PercentVbus;
  }

  public void configPathFollowing() {
    driveControlState = DriveControlState.PathFollowing;
  }

  /*
   * initializes the PID and feed forward gain for the Talons when they are used
   * in velocity control mode.
   */
  public synchronized void reloadGains() {
    logger.writeToLogFormatted(this, "start of reloadGains");
    if (RobotMap.robotName == RobotMap.robotName.PracticeBot) {
      m_left.config_kP(0, Constants.kDriveHighGearVelocityKp, 0);
      m_left.config_kI(0, Constants.kDriveHighGearVelocityKi, 0);
      m_left.config_kD(0, Constants.kDriveHighGearVelocityKd, 0);
      m_left.config_kF(0, Constants.kDriveHighGearVelocityKf, 0);
      m_left.config_IntegralZone(0, Constants.kDriveHighGearVelocityIZone, 0);
      m_left.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, 0);

      m_right.config_kP(0, Constants.kDriveHighGearVelocityKp, 0);
      m_right.config_kI(0, Constants.kDriveHighGearVelocityKi, 0);
      m_right.config_kD(0, Constants.kDriveHighGearVelocityKd, 0);
      m_right.config_kF(0, Constants.kDriveHighGearVelocityKf, 0);
      m_right.config_IntegralZone(0, Constants.kDriveHighGearVelocityIZone, 0);
      m_right.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, 0);
    } else {
      m_left.config_kP(0, Constants.kDriveProgPlatVelocityKp, 0);
      m_left.config_kI(0, Constants.kDriveProgPlatVelocityKi, 0);
      m_left.config_kD(0, Constants.kDriveProgPlatVelocityKd, 0);
      m_left.config_kF(0, Constants.kDriveProgPlatVelocityKf, 0);
      m_left.config_IntegralZone(0, Constants.kDriveHighGearVelocityIZone, 0);
      m_left.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, 0);

      m_right.config_kP(0, Constants.kDriveProgPlatVelocityKp, 0);
      m_right.config_kI(0, Constants.kDriveProgPlatVelocityKi, 0);
      m_right.config_kD(0, Constants.kDriveProgPlatVelocityKd, 0);
      m_right.config_kF(0, Constants.kDriveProgPlatVelocityKf, 0);
      m_right.config_IntegralZone(0, Constants.kDriveHighGearVelocityIZone, 0);
      m_right.configClosedloopRamp(Constants.kDriveHighGearVelocityRampRate, 0);
    }
  }

  @Override
  public void writeToLog() {}

  @Override
  public void reset() {}
}
