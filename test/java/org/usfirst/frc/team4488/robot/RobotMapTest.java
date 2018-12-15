package org.usfirst.frc.team4488.robot;

import static org.junit.Assert.*;

import org.junit.Test;

public class RobotMapTest {
  // Verify Pneumatics

  @Test
  public void testInatkeSolenoid() {
    assertEquals(RobotMap.INTAKE_SOLENOID, 0);
  }

  @Test
  public void testDriveShifter() {
    assertEquals(RobotMap.DriveGearShiftSolenoid, 4);
  }

  @Test
  public void testPowerCoreShifter() {
    assertEquals(RobotMap.POWERCORE_SHIFTER, 5);
  }

  @Test
  public void testManipulatorClamp() {
    assertEquals(RobotMap.MANIPULATOR_CLAMP, 6);
  }

  @Test
  public void testManipulatorRotator() {
    assertEquals(RobotMap.MANIPULATOR_ROTATER, 7);
  }

  // Verify Talons
  @Test
  public void testRightDriveMaster() {
    assertEquals(RobotMap.DriveMotorRightM, 0);
  }

  @Test
  public void testRightDriveSlave1() {
    assertEquals(RobotMap.DriveMotorRight2, 1);
  }

  @Test
  public void testRightDriveSlave2() {
    assertEquals(RobotMap.DriveMotorRight3, 2);
  }

  @Test
  public void testLeftDriveMaster() {
    assertEquals(RobotMap.DriveMotorLeftM, 5);
  }

  @Test
  public void testLeftDriveSlave1() {
    assertEquals(RobotMap.DriveMotorLeft2, 4);
  }

  @Test
  public void testLeftDriveSlave2() {
    assertEquals(RobotMap.DriveMotorLeft3, 3);
  }

  @Test
  public void testPowerCoreMaster() {
    assertEquals(RobotMap.POWERCORE_MASTER, 6);
  }

  @Test
  public void testPowerCoreSlave1() {
    assertEquals(RobotMap.POWERCORE_SLAVE_1, 7);
  }

  @Test
  public void testPowerCoreSlave2() {
    assertEquals(RobotMap.POWERCORE_SLAVE_2, 8);
  }

  @Test
  public void testPowerCoreSlave3() {
    assertEquals(RobotMap.POWERCORE_SLAVE_3, 9);
  }

  // Verify DIO
  @Test
  public void testLiftHallEffect() {
    assertEquals(RobotMap.FORKLIFT_HALL_EFFECT, 0);
  }
}
