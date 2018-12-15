package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class RotateManipulator extends RunOnceAction {

  private boolean val;

  public RotateManipulator(boolean val) {
    this.val = val;
  }

  @Override
  public void runOnce() {
    if (val) {
      Manipulator.getInstance().rotateDown();
    } else {
      Manipulator.getInstance().rotateUp();
    }
  }
}
