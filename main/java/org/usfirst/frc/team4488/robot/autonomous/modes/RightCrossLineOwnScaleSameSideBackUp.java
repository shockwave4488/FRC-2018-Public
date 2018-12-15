package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightSideSameScaleStayOutOfWay;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightSideSameScaleStayOutOfWayBackUp;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.Clamp;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.ParallelAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.SeriesAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.TimerWait;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;

public class RightCrossLineOwnScaleSameSideBackUp extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new RightSideSameScaleStayOutOfWay();

    ArrayList<Action> waitAndFlick = new ArrayList<Action>();
    waitAndFlick.add(new WaitForPathMarkerAction("Score"));
    waitAndFlick.add(new TimerWait(2400));
    waitAndFlick.add(new Clamp(true));

    ArrayList<Action> waitAndMoveLift = new ArrayList<Action>();
    waitAndMoveLift.add(new WaitForPathMarkerAction("LiftUp"));
    waitAndMoveLift.add(new MoveLift(Constants.scoreHeight));

    ArrayList<Action> parallel = new ArrayList<Action>();
    parallel.add(new SeriesAction(waitAndFlick));
    parallel.add(new SeriesAction(waitAndMoveLift));
    parallel.add(new DrivePathAction(mPath));
    ParallelAction auto = new ParallelAction(parallel);

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(auto);
    runAction(new DrivePathAction(new RightSideSameScaleStayOutOfWayBackUp()));
    runAction(new IntakeAndHold());
  }
}
