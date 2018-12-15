package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Intake;

public class WaitUntilIntakeIsOut implements Action {

  @Override
  public boolean isFinished() {
    return Intake.getInstance().isDeployed();
  }

  @Override
  public void update() {}

  @Override
  public void done() {}

  @Override
  public void start() {}
}
