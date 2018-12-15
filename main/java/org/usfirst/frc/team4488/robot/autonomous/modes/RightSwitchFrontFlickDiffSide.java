package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.*;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ForwardFlick;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.ParallelAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.RotateManipulator;
import org.usfirst.frc.team4488.robot.autonomous.actions.SeriesAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.TimerWait;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;

public class RightSwitchFrontFlickDiffSide extends AutoModeBase {

  @Override
  protected void routine() throws AutoModeEndedException {
    PathContainer mPath = new RightSwitchFrontFlickDiffSidePath();
    ArrayList<Action> waitAndRotate = new ArrayList<Action>();
    waitAndRotate.add(new TimerWait(500));
    waitAndRotate.add(new RotateManipulator(true));

    ArrayList<Action> waitAndFlick = new ArrayList<Action>();
    waitAndFlick.add(new WaitForPathMarkerAction("flick"));
    waitAndFlick.add(new TimerWait(200));
    waitAndFlick.add(new ForwardFlick()); // TODO Flick doesnt work

    ArrayList<Action> parallel = new ArrayList<Action>();
    parallel.add(new SeriesAction(waitAndRotate));
    // parallel.add(new SeriesAction(waitAndFlick));
    parallel.add(new MoveLift(Constants.flickHeight));
    parallel.add(new DrivePathAction(mPath));

    ArrayList<Action> autoActions = new ArrayList<Action>();
    autoActions.add(new ParallelAction(parallel));
    autoActions.add(new ForwardFlick());
    SeriesAction auto = new SeriesAction(autoActions);

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(auto);
  }
}
