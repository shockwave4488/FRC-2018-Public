package org.usfirst.frc.team4488.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import java.util.List;
import org.usfirst.frc.team4488.robot.systems.Drive;
import org.usfirst.frc.team4488.robot.systems.Shooter;
import org.usfirst.frc.team4488.robot.systems.Subsystem;
import org.usfirst.frc.team4488.robot.systems.Turret;

public class RobotMap {
  // should never change this line
  public static final RobotName robotName = RobotName.Mock;

  // talons
  public static final int DriveMotorRightM = 0;
  public static final int DriveMotorRight2 = 1;
  public static final int DriveMotorRight3 = 2;
  public static final int DriveMotorLeft3 = 3;
  public static final int DriveMotorLeft2 = 4;
  public static final int DriveMotorLeftM = 5;
  public static final int TurretMotor = 0; // SR not SRX

  public static final int ShooterWheel = 6;

  // solenoids
  public static final int DriveGearShiftSolenoid = 4;

  // analog sensors
  public static final int TurretPotentiometer = 0;

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

      case Mock:
        driveExists = true;
        subsystemsToUse =
            Arrays.asList(Drive.getInstance(), Turret.getInstance(), Shooter.getInstance());

        SmartDashboard.putString(roboNameKey, "Mock");
    }

    return subsystemsToUse;
  }
}
