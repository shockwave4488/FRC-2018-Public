package org.usfirst.frc.team4488.robot.autonomous.modes;

import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.DirectDriveStraightAction;

public class DirectDriveTest extends AutoModeBase {

  @Override
  protected void routine() throws AutoModeEndedException {
    runAction(new DirectDriveStraightAction(100.0, 0));
  }
}
