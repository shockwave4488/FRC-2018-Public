package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class Clamp extends RunOnceAction {
  private boolean val;

  public Clamp(boolean val) {
    this.val = val;
  }

  @Override
  public void runOnce() {
    if (val) {
      Manipulator.getInstance().unClamp();
    } else {
      Manipulator.getInstance().clamp();
    }
  }
}
