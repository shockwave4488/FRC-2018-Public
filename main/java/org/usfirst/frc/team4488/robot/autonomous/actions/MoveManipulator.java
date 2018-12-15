package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Forklift;
import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class MoveManipulator implements Action {
  Logging logger = Logging.getInstance();

  private Manipulator manip;
  private double pos;
  private double height;
  private boolean usingHeight = false;
  private double updateCycles = 0;

  public MoveManipulator(double pos) {
    manip = Manipulator.getInstance();
    this.pos = pos;
  }

  public MoveManipulator(double pos, double height) {
    manip = Manipulator.getInstance();
    this.pos = pos;
    this.height = height;
    usingHeight = true;
  }

  @Override
  public boolean isFinished() {
    return manip.atDesiredPosition() && updateCycles > 3;
  }

  @Override
  public void update() {
    if (usingHeight) {
      if (Forklift.getInstance().getCurrentHeight() > height) {
        updateCycles++;
        manip.setPosition(pos);
      }
    } else {
      updateCycles++;
      manip.setPosition(pos);
    }
  }

  @Override
  public void done() {
    logger.writeToLogFormatted(this, "done()");
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    manip.setDoneRange(0.125);
    if (!usingHeight) {
      manip.setPosition(pos);
    }
  }
}
