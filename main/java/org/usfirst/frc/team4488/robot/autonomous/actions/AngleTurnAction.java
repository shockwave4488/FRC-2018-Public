package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Drive;
import org.usfirst.frc.team4488.robot.systems.SmartDrive;

public class AngleTurnAction implements Action {
  Logging logger = Logging.getInstance();

  private double desiredAngleDegrees;

  SmartDrive smartDrive;

  public AngleTurnAction(double angle) {
    smartDrive = new SmartDrive(Drive.getInstance());

    desiredAngleDegrees = angle;
  }

  @Override
  public boolean isFinished() {
    // TODO Auto-generated method stub
    return smartDrive.isTurnDone();
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub
    smartDrive.turnToAngle(desiredAngleDegrees);
  }

  @Override
  public void done() {
    // TODO Auto-generated method stub
    smartDrive.stop();
    logger.writeToLogFormatted(this, "done()");
  }

  @Override
  public void start() {
    logger.writeToLogFormatted(this, "start()");
    // TODO Auto-generated method stub

  }
}
