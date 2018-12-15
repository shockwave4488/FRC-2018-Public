package org.usfirst.frc.team4488.robot.autonomous.modes;

import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.PivotTestOne;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.PivotTestTwo;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.TurnToHeadingAction;

public class PivotTest extends AutoModeBase {

  @Override
  protected void routine() throws AutoModeEndedException {
    PathContainer path = new PivotTestOne();
    runAction(new ResetPoseFromPathAction(path));
    runAction(new DrivePathAction(path));
    runAction(new TurnToHeadingAction(160));
    runAction(new DrivePathAction(new PivotTestTwo()));
  }
}
