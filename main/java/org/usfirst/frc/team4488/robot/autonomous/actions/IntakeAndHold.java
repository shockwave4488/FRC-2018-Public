package org.usfirst.frc.team4488.robot.autonomous.actions;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.systems.Forklift;
import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class IntakeAndHold implements Action {
  Logging logger = Logging.getInstance();

  private ParallelAction routine;

  private double readyHeight = 15.5;
  private double startHeight = 40;
  private boolean inAuto;

  public IntakeAndHold() {
    inAuto = false;
    buildTeleopRoutine();
  }

  public IntakeAndHold(boolean inAuto) {
    // updated intake in hold good enough for it not to be changed
    /*this.inAuto = inAuto;
    if (inAuto) {
      Forklift.getInstance().setDoneRange(5);
      Forklift.getInstance().changeMinDoneCycleCount(1);
      buildAutoRoutine();
    } else {*/
    buildTeleopRoutine();
    // }
  }

  private void buildTeleopRoutine() {
    ArrayList<Action> routineActions = new ArrayList<Action>();

    ArrayList<Action> setup = new ArrayList<Action>();
    if (Forklift.getInstance().getCurrentHeight() > startHeight) {
      setup.add(new InaccurateMoveLift(startHeight));
    }

    setup.add(new Clamp(false));
    setup.add(new RotateManipulator(true));
    setup.add(new InaccurateMoveManipulator(0));

    routineActions.add(new ParallelAction(setup));
    routineActions.add(new MoveLiftToRange(10, 21));
    routineActions.add(new IntakeCube());
    routineActions.add(new Clamp(true));
    routineActions.add(new MoveLiftToCube());
    routineActions.add(new Clamp(false));

    ArrayList<Action> finishGrab = new ArrayList<Action>();
    finishGrab.add(new RetractIntake());
    finishGrab.add(new IdleLift());

    ArrayList<Action> finishGrabAndRumble = new ArrayList<Action>();
    finishGrabAndRumble.add(new SeriesAction(finishGrab));
    finishGrabAndRumble.add(new RumblePrimary());

    routineActions.add(new ParallelAction(finishGrabAndRumble));

    ArrayList<Action> routineAndMoveManip = new ArrayList<Action>();
    routineAndMoveManip.add(new SeriesAction(routineActions));
    routineAndMoveManip.add(new MoveManipulator(0));
    routine = new ParallelAction(routineAndMoveManip);
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
    logger.writeToLogFormatted(this, "end of done()");
    Manipulator.getInstance().setHasCube(true);
    Manipulator.getInstance().unLock();
  }
}
