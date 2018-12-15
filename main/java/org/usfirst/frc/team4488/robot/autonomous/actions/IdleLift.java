package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Forklift;

public class IdleLift implements Action {
  Logging logger = Logging.getInstance();

  private int updateCycles = 0;

  @Override
  public boolean isFinished() {
    return Forklift.getInstance().atDesiredPosition() && updateCycles > 3;
  }

  @Override
  public void update() {
    updateCycles++;
    Forklift.getInstance().setHeight(Constants.liftIdleHeight);
  }

  @Override
  public void done() {
    Forklift.getInstance().setHeight(Forklift.getInstance().getCurrentHeight());
    logger.writeToLogFormatted(this, "end of done()");
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    Forklift.getInstance().setHeight(Constants.liftIdleHeight);
  }
}
