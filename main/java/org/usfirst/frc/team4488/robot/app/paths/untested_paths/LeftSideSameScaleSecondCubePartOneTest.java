package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;

public class LeftSideSameScaleSecondCubePartOneTest implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(268, 265, 0, 0));
    sWaypoints.add(new Waypoint(245, 242, 15, 40));
    sWaypoints.add(new Waypoint(215, 238, 0, 30));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(268, 265), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":268,"y":265},"speed":0,"radius":0,"comment":""},{"position":{"x":245,"y":242},"speed":40,"radius":15,"comment":""},{"position":{"x":215,"y":238},"speed":30,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftSideSameScaleSecondCubePartOneTest
}
