package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.LeftCrossLineOwnScaleSameSidePath;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.LeftScaleBackUpTenInches;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.LeftSideSameScaleSecondCubePartOneTest;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.LeftSideSameScaleSecondCubePartThreeTest;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.LeftSideSameScaleSecondCubePartTwoTest;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.Clamp;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveManipulator;
import org.usfirst.frc.team4488.robot.autonomous.actions.ParallelAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.SeriesAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.TimerWait;
import org.usfirst.frc.team4488.robot.autonomous.actions.TurnToHeadingAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitUntilIntakeIsOut;

public class LeftCrossLineOwnScaleSameSide extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new LeftCrossLineOwnScaleSameSidePath();

    // Score First Cube

    ArrayList<Action> waitAndMoveLift = new ArrayList<Action>();
    waitAndMoveLift.add(new WaitForPathMarkerAction("LiftUp"));

    ArrayList<Action> waitAndDropCube = new ArrayList<Action>();
    waitAndDropCube.add(new TimerWait(2300));
    waitAndDropCube.add(new Clamp(true));

    ArrayList<Action> dropCubeAndMoveLift = new ArrayList<Action>();
    dropCubeAndMoveLift.add(new SeriesAction(waitAndDropCube));
    dropCubeAndMoveLift.add(new MoveLift(Constants.scoreHeight));

    waitAndMoveLift.add(new ParallelAction(dropCubeAndMoveLift));

    ArrayList<Action> followPath = new ArrayList<Action>();
    followPath.add(new SeriesAction(waitAndMoveLift));
    followPath.add(new DrivePathAction(mPath));

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(new ParallelAction(followPath));
    // uncomment after tuning path
    runAction(new Clamp(true));
    runAction(new DrivePathAction(new LeftScaleBackUpTenInches()));

    ArrayList<Action> driveAndTurn = new ArrayList<Action>();
    driveAndTurn.add(new TurnToHeadingAction(140));
    driveAndTurn.add(new WaitUntilIntakeIsOut());
    driveAndTurn.add(new DrivePathAction(new LeftSideSameScaleSecondCubePartOneTest()));

    ArrayList<Action> driveAndLowerLift = new ArrayList<Action>();
    driveAndLowerLift.add(new SeriesAction(driveAndTurn));
    driveAndLowerLift.add(new IntakeAndHold(true));
    runAction(new ParallelAction(driveAndLowerLift));

    // Score Second Cube
    ArrayList<Action> turnAndDrive = new ArrayList<Action>();
    turnAndDrive.add(new DrivePathAction(new LeftSideSameScaleSecondCubePartTwoTest()));
    turnAndDrive.add(new TurnToHeadingAction(0));
    turnAndDrive.add(new DrivePathAction(new LeftSideSameScaleSecondCubePartThreeTest()));

    ArrayList<Action> driveAndMoveLift = new ArrayList<Action>();
    driveAndMoveLift.add(new SeriesAction(turnAndDrive));
    driveAndMoveLift.add(new MoveLift(Constants.scoreHeight));
    driveAndMoveLift.add(new MoveManipulator(-7));

    runAction(new ParallelAction(driveAndMoveLift));
    runAction(new Clamp(true));
  }
}
