package org.usfirst.frc.team4488.robot.autonomous.modes;

import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.DirectDriveStraightAction;

public class CrossLine extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {
    System.out.print("You are trying to cross theline using direct PID drive!");
    runAction(new DirectDriveStraightAction(85.0, 0)); // drives out 125 inches
  }
}
