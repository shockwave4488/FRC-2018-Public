package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class ForwardFlick extends RunOnceAction {

  @Override
  public void runOnce() {
    Manipulator manip = Manipulator.getInstance();
    if (!manip.isRotated()) {
      manip.rotateDown();
      wait(600);
    }
    manip.rotateUp();
    wait(300);
    manip.unClamp();
  }

  private void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
