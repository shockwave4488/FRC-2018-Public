package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class LockManipulator extends RunOnceAction {

  @Override
  public void runOnce() {
    Manipulator.getInstance().lock();
  }
}
