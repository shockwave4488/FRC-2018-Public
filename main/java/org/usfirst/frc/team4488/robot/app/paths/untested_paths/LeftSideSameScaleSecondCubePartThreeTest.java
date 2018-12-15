package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;

public class LeftSideSameScaleSecondCubePartThreeTest implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(225, 240, 0, 0));
    sWaypoints.add(new Waypoint(290, 245, 0, 60));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(225, 240), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":225,"y":240},"speed":0,"radius":0,"comment":""},{"position":{"x":290,"y":245},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftSideSameScaleSecondCubePartThreeTest
}
