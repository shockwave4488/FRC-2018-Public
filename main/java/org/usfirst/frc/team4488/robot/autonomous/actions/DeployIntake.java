package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Intake;

public class DeployIntake implements Action {

  private Intake intake;

  public DeployIntake() {
    intake = Intake.getInstance();
  }

  @Override
  public boolean isFinished() {
    return intake.isIn();
  }

  @Override
  public void update() {}

  @Override
  public void done() {}

  @Override
  public void start() {
    intake.deployOut();
  }
}
