package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.systems.Forklift;

public class MoveLiftToRange implements Action {

  private Forklift lift;
  private double lower;
  private double upper;
  private double mid;
  private int updateCounts;

  public MoveLiftToRange(double lower, double upper) {
    this.lower = lower;
    this.upper = upper;
    this.mid = lower + (upper - lower) / 2;
    this.lift = Forklift.getInstance();
  }

  @Override
  public boolean isFinished() {
    return lift.getCurrentHeight() > lower && lift.getCurrentHeight() < upper && updateCounts > 3;
  }

  @Override
  public void update() {
    updateCounts++;
    lift.setHeight(mid);
  }

  @Override
  public void done() {}

  @Override
  public void start() {}
}
