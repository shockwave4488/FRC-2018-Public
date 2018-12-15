package org.usfirst.frc.team4488.robot.autonomous.modes;

import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.AngleTurnAction;

public class AngleTurnTest extends AutoModeBase {

  @Override
  protected void routine() throws AutoModeEndedException {
    runAction(new AngleTurnAction(100.0));
  }
}
