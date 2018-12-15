package org.usfirst.frc.team4488.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import java.util.List;
import org.usfirst.frc.team4488.robot.systems.Climber;
import org.usfirst.frc.team4488.robot.systems.Drive;
import org.usfirst.frc.team4488.robot.systems.Forklift;
import org.usfirst.frc.team4488.robot.systems.Intake;
import org.usfirst.frc.team4488.robot.systems.Manipulator;
import org.usfirst.frc.team4488.robot.systems.PneumaticCompressorModule;
import org.usfirst.frc.team4488.robot.systems.PowerCore;
import org.usfirst.frc.team4488.robot.systems.RoutineManager;
import org.usfirst.frc.team4488.robot.systems.Subsystem;

public class RobotMap {
  // should never change this line
  public static final RobotName robotName = RobotName.PracticeBot;

  // talons
  public static final int DriveMotorRightM = 0;
  public static final int DriveMotorRight2 = 1;
  public static final int DriveMotorRight3 = 2;
  public static final int DriveMotorLeft3 = 3;
  public static final int DriveMotorLeft2 = 4;
  public static final int DriveMotorLeftM = 5;
  public static final int POWERCORE_MASTER = 6;
  public static final int POWERCORE_SLAVE_1 = 7;
  public static final int POWERCORE_SLAVE_2 = 8;
  public static final int POWERCORE_SLAVE_3 = 9;
  public static final int INTAKE_RIGHT_MOTOR = 10;
  public static final int INTAKE_LEFT_MOTOR = 11;
  public static final int MANIPULATOR_MOTOR = 12;

  // solenoids
  public static final int INTAKE_PANCAKE = 0;
  public static final int INTAKE_SOLENOID = 0;
  public static final int DriveGearShiftSolenoid = 4;
  public static final int POWERCORE_SHIFTER = 5;
  public static final int MANIPULATOR_CLAMP = 6;
  public static final int MANIPULATOR_ROTATER = 7;
  public static final int SolenoidLED = 7;
  public static final int BUDDY_BAR_SOLENOID = 2;

  // beambreaks
  public static final int INTAKE_HAS_CUBE_1 = 2;

  // ultrasonics
  public static final int kUltraSonicLeft = 0;
  public static final int kUltraSonicRight = 1;

  // halleffects
  public static final int FORKLIFT_HALL_EFFECT = 0;
  public static final int AT_CUBE_LEVEL_HALL_EFFECT = 1;
  public static final int INTAKE_HALL_EFFECT_1 = 3;

  // logic to determine which features to enable
  public static boolean driveExists = false;
  public static List<Subsystem> subsystemsToUse;

  private static String roboNameKey = "RobotName";

  public static List<Subsystem> robotSelector() {
    switch (RobotMap.robotName) {
      case BareRoboRIO:
        driveExists = false;
        subsystemsToUse = Arrays.asList();
        SmartDashboard.putString(roboNameKey, "BareRoboRIO");
        break;

      case ProgrammingPlatform:
        driveExists = true;
        subsystemsToUse = Arrays.asList(Drive.getInstance());
        SmartDashboard.putString(roboNameKey, "ProgrammingPlatform");
        break;

      case PracticeBot:

      default:
        driveExists = true;
        subsystemsToUse =
            Arrays.asList(
                Drive.getInstance(),
                PowerCore.getInstance(),
                PneumaticCompressorModule.getInstance(),
                Forklift.getInstance(),
                Manipulator.getInstance(),
                Intake.getInstance(),
                Climber.getInstance(),
                RoutineManager.getInstance());
        SmartDashboard.putString(roboNameKey, "Practice Bot");
        break;
    }
    return subsystemsToUse;
  }
}
