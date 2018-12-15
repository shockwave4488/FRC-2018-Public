package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.RightCrossLineOwnScaleSameSidePath;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightScaleBackUpTenInches;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightSideSameScaleSecondCubePartOneTest;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightSideSameScaleSecondCubePartThreeTest;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.RightSideSameScaleSecondCubePartTwoTest;
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
import org.usfirst.frc.team4488.robot.autonomous.actions.TurnToHeadingAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitForPathMarkerAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.WaitUntilIntakeIsOut;

public class RightCrossLineOwnScaleSameSide extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {

    PathContainer mPath = new RightCrossLineOwnScaleSameSidePath();

    // Score First Cube
    ArrayList<Action> waitAndMoveLift = new ArrayList<Action>();
    waitAndMoveLift.add(new WaitForPathMarkerAction("LiftUp"));

    ArrayList<Action> waitAndDropCube = new ArrayList<Action>();
    waitAndDropCube.add(new TimerWait(3000));
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
    runAction(new DrivePathAction(new RightScaleBackUpTenInches()));

    ArrayList<Action> driveAndTurn = new ArrayList<Action>();
    driveAndTurn.add(new TurnToHeadingAction(-140));
    driveAndTurn.add(new WaitUntilIntakeIsOut());
    driveAndTurn.add(new DrivePathAction(new RightSideSameScaleSecondCubePartOneTest()));

    ArrayList<Action> driveAndLowerLift = new ArrayList<Action>();
    driveAndLowerLift.add(new SeriesAction(driveAndTurn));
    driveAndLowerLift.add(new IntakeAndHold(true));
    runAction(new ParallelAction(driveAndLowerLift));

    // Score Second Cube
    ArrayList<Action> turnAndDrive = new ArrayList<Action>();
    turnAndDrive.add(new DrivePathAction(new RightSideSameScaleSecondCubePartTwoTest()));
    turnAndDrive.add(new TurnToHeadingAction(0));
    turnAndDrive.add(new DrivePathAction(new RightSideSameScaleSecondCubePartThreeTest()));

    ArrayList<Action> driveAndMoveLift = new ArrayList<Action>();
    driveAndMoveLift.add(new SeriesAction(turnAndDrive));
    driveAndMoveLift.add(new MoveLift(Constants.scoreHeight));

    runAction(new ParallelAction(driveAndMoveLift));
    runAction(new Clamp(true));
  }
}
