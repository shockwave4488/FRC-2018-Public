package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Forklift;

public class MoveLift implements Action {
  Logging logger = Logging.getInstance();

  private Forklift lift;
  private double height;
  private int updateCount = 0;

  public MoveLift(double height) {
    lift = Forklift.getInstance();
    this.height = height;
  }

  @Override
  public boolean isFinished() {
    return lift.atDesiredPosition() && updateCount > 3;
  }

  @Override
  public void update() {
    updateCount++;
    if (!isFinished()) {
      lift.setHeight(height);
    }
  }

  @Override
  public void done() {
    System.out.println("Move lift finished");
    logger.writeToLogFormatted(this, "done()");
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    System.out.println("Move lift started");
    lift.setHeight(height);
  }
}
