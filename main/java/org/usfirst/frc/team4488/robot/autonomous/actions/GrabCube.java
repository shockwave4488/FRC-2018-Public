package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class GrabCube extends RunOnceAction {

  @Override
  public void runOnce() {
    Manipulator manip = Manipulator.getInstance();
    manip.rotateDown();
    wait(1000);
    manip.clamp();
    wait(100);
  }

  public void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
