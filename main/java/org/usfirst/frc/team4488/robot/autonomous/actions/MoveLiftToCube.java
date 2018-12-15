package org.usfirst.frc.team4488.robot.autonomous.actions;

import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.ArduinoAnalog;
import org.usfirst.frc.team4488.robot.systems.Forklift;
import org.usfirst.frc.team4488.robot.systems.Intake;

public class MoveLiftToCube implements Action {

  private Forklift lift;
  private Intake intake;
  private ArduinoAnalog arduino;

  private double grabHeight = 7.5;
  private int updateCycles = 0;

  public MoveLiftToCube() {
    arduino = ArduinoAnalog.getInstance();
    lift = Forklift.getInstance();
    intake = Intake.getInstance();
  }

  @Override
  public boolean isFinished() {
    if (arduino.getBounceback() > 660 && arduino.getBounceback() < 700) {
      Logging.getInstance().writeToLogFormatted(this, "Finished by bounceback");
      return true;
    } else if (lift.atCubeLevel() && intake.hasCube()) {
      Logging.getInstance().writeToLogFormatted(this, "Finished by hall effect");
      return true;
    } else if (lift.atDesiredPosition() && updateCycles > 3) {
      Logging.getInstance().writeToLogFormatted(this, "Finished by PID");
      return true;
    }
    return false;
  }

  @Override
  public void update() {
    updateCycles++;
    lift.setHeight(grabHeight);
  }

  @Override
  public void done() {
    Logging.getInstance().writeToLogFormatted(this, "done()");
    lift.setHeight(lift.getCurrentHeight());
  }

  @Override
  public void start() {
    Logging.getInstance().writeToLogFormatted(this, "start()");
    lift.setHeight(grabHeight);
  }
}
