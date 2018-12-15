package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class SideFlick extends RunOnceAction {

  @Override
  public void runOnce() {
    Manipulator manip = Manipulator.getInstance();
    manip.setPosition(-5.5);
    manip.rotateDown();
    wait(700);
    manip.setPower(0.5);
    wait(50);
    manip.unClamp();
    wait(50);
    manip.setPower(0);
  }

  private void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
