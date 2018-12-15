package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class InaccurateMoveManipulator implements Action {
  Logging logger = Logging.getInstance();

  private Manipulator manip;
  private double pos;
  private double startingDoneRange;
  private double updateCycles = 0;

  public InaccurateMoveManipulator(double pos) {
    manip = Manipulator.getInstance();
    this.pos = pos;
  }

  @Override
  public boolean isFinished() {
    return manip.atDesiredPosition() && updateCycles > 3;
  }

  @Override
  public void update() {
    updateCycles++;
    manip.setDoneRange(0.375);
    manip.setPosition(pos);
  }

  @Override
  public void done() {
    logger.writeToLogFormatted(this, "done()");
    manip.setDoneRange(startingDoneRange);
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    startingDoneRange = manip.getDoneRange();
    manip.setDoneRange(0.375);
    manip.setPosition(pos);
  }
}
