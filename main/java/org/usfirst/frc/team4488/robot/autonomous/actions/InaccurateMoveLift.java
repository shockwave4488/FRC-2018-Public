package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Forklift;

public class InaccurateMoveLift implements Action {

  private Forklift lift;

  private int startingCycles;
  private double startingDoneRange;
  private double height;
  private int updateCycles = 0;

  public InaccurateMoveLift(double height) {
    this.height = height;
    lift = Forklift.getInstance();
  }

  @Override
  public boolean isFinished() {
    return lift.atDesiredPosition() && updateCycles > 3;
  }

  @Override
  public void update() {
    updateCycles++;
    lift.setHeight(height);
  }

  @Override
  public void done() {
    lift.setDoneRange(startingDoneRange);
    lift.changeMinDoneCycleCount(startingCycles);
  }

  @Override
  public void start() {
    startingCycles = lift.getMinDoneCycleCount();
    startingDoneRange = lift.getDoneRange();
    lift.setDoneRange(5);
    lift.changeMinDoneCycleCount(5);
  }
}
