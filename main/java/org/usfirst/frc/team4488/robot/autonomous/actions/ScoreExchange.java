package org.usfirst.frc.team4488.robot.autonomous.actions;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.operator.Logging;

public class ScoreExchange implements Action {
  Logging logger = Logging.getInstance();

  private ParallelAction routine;

  public ScoreExchange() {
    ArrayList<Action> actions = new ArrayList<Action>();
    actions.add(new DropCube());
    actions.add(new IntakeDejam());
    routine = new ParallelAction(actions);
  }

  public void start() {
    logger.writeToLogFormatted(this, "start()");
    routine.start();
  }

  public void update() {
    routine.update();
  }

  public boolean isFinished() {
    return routine.isFinished();
  }

  public void done() {
    routine.done();
    logger.writeToLogFormatted(this, "done()");
  }
}
