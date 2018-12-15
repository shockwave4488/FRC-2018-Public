package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.RightCrossLineOwnScaleDiffSidePath;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.LeftScaleBackUpTenInches;
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
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;

public class RightCrossLineOwnScaleDiffSide extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new RightCrossLineOwnScaleDiffSidePath();

    ArrayList<Action> waitAndMoveLift = new ArrayList<Action>();
    waitAndMoveLift.add(new WaitForPathMarkerAction("LiftUp"));
    waitAndMoveLift.add(new MoveLift(Constants.scoreHeight));

    ArrayList<Action> followPath = new ArrayList<Action>();
    followPath.add(new SeriesAction(waitAndMoveLift));
    followPath.add(new DrivePathAction(mPath));

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(new ParallelAction(followPath));
    // uncomment after tuning path
    runAction(new Clamp(true));
    runAction(new DrivePathAction(new LeftScaleBackUpTenInches()));
    runAction(new IntakeAndHold());
  }
}
