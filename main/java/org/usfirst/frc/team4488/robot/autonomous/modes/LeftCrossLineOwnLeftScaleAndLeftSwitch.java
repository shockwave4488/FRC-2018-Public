package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.LeftCrossLineOwnScaleSameSidePath;
import org.usfirst.frc.team4488.robot.app.paths.LeftSideSameScaleSecondCubePartOne;
import org.usfirst.frc.team4488.robot.app.paths.LeftSideSameScaleSecondCubePartTwo;
import org.usfirst.frc.team4488.robot.app.paths.LeftSideSameScaleSecondCubeSwitchPartThree;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
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

public class LeftCrossLineOwnLeftScaleAndLeftSwitch extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new LeftCrossLineOwnScaleSameSidePath();

    // Score First Cube
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

    // Grab Second Cube
    ArrayList<Action> waitAndMoveLiftDown = new ArrayList<Action>();
    waitAndMoveLiftDown.add(new WaitForPathMarkerAction("liftDown"));
    waitAndMoveLiftDown.add(new MoveLift(Constants.liftIdleHeight));

    ArrayList<Action> parallel = new ArrayList<Action>();
    parallel.add(new DrivePathAction(new LeftSideSameScaleSecondCubePartOne()));
    parallel.add(new SeriesAction(waitAndMoveLiftDown));
    runAction(new ParallelAction(parallel));

    ArrayList<Action> driveAndIntake = new ArrayList<Action>();
    driveAndIntake.add(new DrivePathAction(new LeftSideSameScaleSecondCubePartTwo()));
    driveAndIntake.add(new IntakeAndHold());

    runAction(new ParallelAction(driveAndIntake));

    // Score Second Cube
    runAction(new DrivePathAction(new LeftSideSameScaleSecondCubeSwitchPartThree()));

    runAction(new MoveLift(Constants.flickHeight));
    runAction(new ForwardFlick());
  }
}
