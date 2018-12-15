package org.usfirst.frc.team4488.robot;

public class Constants {

  /*
   *  Drive Subsystem Constants
   */

  public static final double driftCorrection = 1.0;

  public static final double practiceEncoderTicks = 5500; // needs to be verified (2/23/2018)
  public static final double programmingEncoderTicks = 1024; // needs to be verified (2/23/2018)

  public static double turnPowerPercentage = 80;
  public static double programmingWheelDiameter = 6; // inches
  public static double practiceWheelDiameter = 4; // inches
  /*
   * APP Constants
   */
  public static double brakeFactor = 1.0; // used to limit APP speeds during testing

  public static double kTrackWidthInches = 27.375; // Distance from left to right tank drive wheels
  public static double kTrackScrubFactor =
      0.924; // Factor to compensate for slipping of wheels while turning, must be adjusted
  // experimentally

  public static double kDriveHighGearMaxSetpoint =
      17.0 * 12.0 / brakeFactor; // inches per second, speed limit for our robot
  public static double kDriveLowGearPositionRampRate = 1.0; // translates to 12V/s
  public static double kDriveHighGearVelocityRampRate = 0.25;

  // Path following constants
  public static double kMinLookAhead = 12.0; // inches
  public static double kMinLookAheadSpeed = 9.0; // inches per second
  public static double kMaxLookAhead = 48.0; // inches
  public static double kMaxLookAheadSpeed = 120.0; // inches per second
  public static double kDeltaLookAhead = kMaxLookAhead - kMinLookAhead; // inches
  public static double kDeltaLookAheadSpeed =
      kMaxLookAheadSpeed - kMinLookAheadSpeed; // inches per second

  public static double kDriveHighGearVelocityKp = 0.01; // 1.2
  public static double kDriveHighGearVelocityKi = 0.0;
  public static double kDriveHighGearVelocityKd = 0.1;
  public static double kDriveHighGearVelocityKf = 0.34;

  public static double kDriveProgPlatVelocityKp = 0.7; // 1.2
  public static double kDriveProgPlatVelocityKi = 0.0;
  public static double kDriveProgPlatVelocityKd = 6.0;
  public static double kDriveProgPlatVelocityKf = 2.4;
  public static int kDriveHighGearVelocityIZone = 0;
  // Path Follower Motion Profiling Constants
  public static double kInertiaSteeringGain = 0.0;
  public static double kSegmentCompletionTolerance = 0.1; // inches
  public static double kPathFollowingMaxAccel = 65.0 / brakeFactor; // inches per second^2
  public static double kPathFollowingMaxVel = 100.0 / brakeFactor; // inches per second
  public static double kPathFollowingProfileKp = 5.00;
  public static double kPathFollowingProfileKi = 0.03;
  public static double kPathFollowingProfileKv = 0.02;
  public static double kPathFollowingProfileKffv = 1.0;
  public static double kPathFollowingProfileKffa = 0.05;
  public static double kPathFollowingGoalPosTolerance = 0.75;
  public static double kPathFollowingGoalVelTolerance = 12.0;
  public static double kPathStopSteeringDistance = 9.0;

  public static double kMaxGoalTrackAge = 1.0;
  public static double kMaxTrackerDistance = 18.0;
  public static double kTimeoutMs = 0.0;
  // Our constants
  public static double kLooperDt = 0.005;
  public static final String prefsPath = "/home/admin/Preferences.json";
  public static final double forkliftDeadband = 0.2;

  /*
   *  lift constants
   *
   */

  public static double liftIdleHeight = 20;
  public static double liftSafetyCheckHeight = 15.5;
  public static double liftHeightMaxInches = 85; // divide by 2 for safety
  public static double liftHeightMinInches = 1; // added 10 for safety
  public static double minHeightWithIntakeOut = 15.5;
  public static double overrideManipHeight = 35;
  public static double pulleyDiameterInches = 1.28;

  public static double climberSoftCurrentLimit = 0; // this is wrong
  public static double climberHardCurrentLimit = 0; // this is wrong
  public static double climbBarHeight = 35.75;
  public static double climbHeightInches = 205;

  public static double powercoreForkliftMax = 5000; // @TODO fix me.
  public static double powercoreForkliftMin = 0;
  public static double powercoreClimberMax = 5000; // @TODO fix me.
  public static double powercoreClimberMin = 0;

  public static double practiceBotLiftTicksToInches = 0.01758;

  public static double manipulatorMin = -8;
  public static double manipulatorMax = 8;
  public static double manipulatorSafetyMargin = 0.5;
  public static double manipulatorSafeMin = (manipulatorMin + manipulatorSafetyMargin);
  public static double manipulatorSafeMax = (manipulatorMax - manipulatorSafetyMargin);
  public static double manipulatorZeroOffset = -9.6;
  public static double minSafeLiftHeight = 30;

  public static double scoreHeight = 77;

  public static double flickHeight = 46;
  public static double liftTeleDoneRange = 2;
  public static double liftAutoDoneRange = 1.5;
}
