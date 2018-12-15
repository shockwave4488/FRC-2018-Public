package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Intake;

public class IntakeRunOnce extends RunOnceAction {

  private boolean val;

  public IntakeRunOnce(boolean val) {
    this.val = val;
  }

  @Override
  public void runOnce() {
    if (val) {
      Intake.getInstance().intakeIn();
    } else {
      Intake.getInstance().stopIntake();
    }
  }
}
