package org.usfirst.frc.team4488.robot.autonomous.modes;

import org.usfirst.frc.team4488.robot.app.paths.PathContainer;
import org.usfirst.frc.team4488.robot.app.paths.RightSideCrossLineStopEarlyPath;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeBase;
import org.usfirst.frc.team4488.robot.autonomous.AutoModeEndedException;
import org.usfirst.frc.team4488.robot.autonomous.actions.DrivePathAction;
import org.usfirst.frc.team4488.robot.autonomous.actions.ResetPoseFromPathAction;

public class RightSideCrossLineStopEarly extends AutoModeBase {

  @Override
  protected void routine() throws AutoModeEndedException {
    PathContainer mPath = new RightSideCrossLineStopEarlyPath();

    runAction(new ResetPoseFromPathAction(mPath));
    runAction(new DrivePathAction(mPath));
  }
}
