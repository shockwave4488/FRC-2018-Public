package org.usfirst.frc.team4488.robot.autonomous.actions;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.operator.Logging;

public class IntakeAndHoldForExchange implements Action {
  Logging logger = Logging.getInstance();

  private SeriesAction routine;

  public IntakeAndHoldForExchange() {
    ArrayList<Action> actions = new ArrayList<Action>();
    actions.add(new IntakeCube());
    actions.add(new MoveManipulator(0));
    actions.add(new MoveLift(1.75));
    actions.add(new GrabCube());
    actions.add(new MoveLift(6));
    routine = new SeriesAction(actions);
  }

  public void start() {
    logger.writeToLogFormatted(this, "start()");
    routine.start();
  }

  public void update() {
    routine.update();
  }

  @Override
  public boolean isFinished() {
    return routine.isFinished();
  }

  @Override
  public void done() {
    routine.done();
    logger.writeToLogFormatted(this, "done()");
  }
}
