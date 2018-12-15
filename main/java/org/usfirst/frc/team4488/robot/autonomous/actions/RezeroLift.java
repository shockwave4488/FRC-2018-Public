package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Forklift;

public class RezeroLift implements Action {

  private TimerWait timeout;
  private Forklift lift;
  private double power = -0.22;

  public RezeroLift() {
    timeout = new TimerWait(7000);
    lift = Forklift.getInstance();
  }

  @Override
  public boolean isFinished() {
    return lift.getHallEffect() || timeout.isFinished();
  }

  @Override
  public void update() {
    lift.lock();
    lift.setPower(power);
    timeout.update();
  }

  @Override
  public void done() {
    lift.setPower(0);
    lift.zeroAtCurrent();
    lift.unLock();
  }

  @Override
  public void start() {
    timeout.start();
    lift.lock();
    lift.setPower(power);
  }
}
