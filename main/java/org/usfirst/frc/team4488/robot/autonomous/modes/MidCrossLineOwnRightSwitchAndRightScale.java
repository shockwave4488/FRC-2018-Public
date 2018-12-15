package org.usfirst.frc.team4488.robot.autonomous.modes;

import java.util.ArrayList;
import org.usfirst.frc.team4488.robot.Constants;
import org.usfirst.frc.team4488.robot.app.paths.MidCrossLineOwnLeftSwitchAndLeftScalePart3;
import org.usfirst.frc.team4488.robot.app.paths.MidCrossLineOwnLeftSwitchAndLeftScalePart4;
import org.usfirst.frc.team4488.robot.app.paths.MidCrossLineOwnLeftSwitchAndRightScalePart5;
import org.usfirst.frc.team4488.robot.app.paths.MidCrossLineOwnRightSwitchAndRightScalePart1;
import org.usfirst.frc.team4488.robot.app.paths.MidCrossLineOwnRightSwitchAndRightScalePart2;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ForwardFlick;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.autonomous.actions.MoveLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.ParallelAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.RotateManipulator;
import org.usfirst.frc.team4488.robot.autonomous.actions.SeriesAction;

public class MidCrossLineOwnRightSwitchAndRightScale extends AutoModeBase {
  @Override
  protected void routine() throws AutoModeEndedException {
    // TODO run and tune this
    PathContainer mPath = new MidCrossLineOwnRightSwitchAndRightScalePart1();

    // Score First Cube
    ArrayList<Action> waitAndMoveLift = new ArrayList<Action>();
    waitAndMoveLift.add(new MoveLift(Constants.flickHeight));
    waitAndMoveLift.add(new RotateManipulator(true));

    ArrayList<Action> followPath = new ArrayList<Action>();
    followPath.add(new SeriesAction(waitAndMoveLift));
    followPath.add(new DrivePathAction(mPath));

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(new ParallelAction(followPath));
    // uncomment after tuning path
    runAction(new ForwardFlick());

    // Grab Second Cube
    ArrayList<Action> waitAndMoveLiftDown = new ArrayList<Action>();
    waitAndMoveLiftDown.add(new MoveLift(Constants.liftIdleHeight));

    ArrayList<Action> parallel = new ArrayList<Action>();
    parallel.add(new DrivePathAction(new MidCrossLineOwnRightSwitchAndRightScalePart2()));
    parallel.add(new SeriesAction(waitAndMoveLiftDown));
    runAction(new ParallelAction(parallel));

    ArrayList<Action> driveAndIntake = new ArrayList<Action>();
    driveAndIntake.add(new DrivePathAction(new MidCrossLineOwnLeftSwitchAndLeftScalePart3()));
    driveAndIntake.add(new IntakeAndHold(true));

    runAction(new ParallelAction(driveAndIntake));

    // Score Second Cube
    runAction(new DrivePathAction(new MidCrossLineOwnLeftSwitchAndLeftScalePart4()));

    ArrayList<Action> driveAndMoveLift = new ArrayList<Action>();
    driveAndMoveLift.add(new MoveLift(Constants.liftIdleHeight));
    driveAndMoveLift.add(new DrivePathAction(new MidCrossLineOwnLeftSwitchAndRightScalePart5()));

    runAction(new ParallelAction(driveAndMoveLift));
  }
}
