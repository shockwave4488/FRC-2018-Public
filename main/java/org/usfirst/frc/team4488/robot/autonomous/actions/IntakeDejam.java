package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Intake;

public class IntakeDejam extends RunOnceAction {

  @Override
  public void runOnce() {
    Intake.getInstance().dejam();
    wait(600);
    Intake.getInstance().stop();
  }

  private void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
