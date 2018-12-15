package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.LeftCrossLineOwnScaleDiffSidePath;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.RightSideSameScaleSecondCubePartOne;
import org.usfirst.frc.team4488.robot.app.paths.RightSideSameScaleSecondCubePartTwo;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.Clamp;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ForwardFlick;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.ParallelAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.SeriesAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;

public class LeftCrossLineOwnScaleDiffSideAndDiffSideSwitch extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new LeftCrossLineOwnScaleDiffSidePath();

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

    runAction(new DrivePathAction(new RightSideSameScaleSecondCubePartOne()));

    ArrayList<Action> driveAndIntake = new ArrayList<Action>();
    driveAndIntake.add(new DrivePathAction(new RightSideSameScaleSecondCubePartTwo()));
    driveAndIntake.add(new IntakeAndHold());

    runAction(new ParallelAction(driveAndIntake));

    // runAction(new DrivePathAction(new LeftCrossLineOwnScaleDiffSideSecondCubePartThree()));

    runAction(new ForwardFlick());
  }
}
