package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class RezeroManipulator implements Action {

  private boolean foundHallEffect = false;
  private TimerWait waitAction;
  private Manipulator manip;

  public RezeroManipulator() {
    waitAction = new TimerWait(5000);
    manip = Manipulator.getInstance();
  }

  @Override
  public boolean isFinished() {
    return foundHallEffect || waitAction.isFinished();
  }

  @Override
  public void update() {
    manip.lock();
    manip.bypassSetPower(-0.15);
    waitAction.update();
    if (manip.getHallEffect()) {
      foundHallEffect = true;
    }
  }

  @Override
  public void done() {
    manip.setPower(0);
    manip.zeroAtCurrent();
    try {
      Thread.sleep(250);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    manip.unLock();
  }

  @Override
  public void start() {
    waitAction.start();
    manip.lock();
    manip.bypassSetPower(-0.15);
  }
}
