package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Intake;

public class IntakeCube implements Action {
  Logging logger = Logging.getInstance();

  private Intake intake;
  private boolean running = false;

  public IntakeCube() {
    intake = Intake.getInstance();
  }

  @Override
  public boolean isFinished() {
    return intake.hasCube();
  }

  @Override
  public void update() {
    if (running) {
      if (!isFinished()) {
        intake.intakeIn();
      } else {
        done();
      }
    }
  }

  @Override
  public void done() {
    running = false;
    System.out.println("Done()");
    logger.writeToLogFormatted(this, "done()");
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    System.out.println("Start()");
    running = true;
  }
}
