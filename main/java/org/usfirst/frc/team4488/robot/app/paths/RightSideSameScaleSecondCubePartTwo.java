package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightSideSameScaleSecondCubePartTwo implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(265, 25, 0, 0));
    sWaypoints.add(new Waypoint(255, 92, 30, 40));
    sWaypoints.add(new Waypoint(215, 92, 0, 40));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(265, 25), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":265,"y":25},"speed":0,"radius":0,"comment":""},{"position":{"x":255,"y":92},"speed":40,"radius":30,"comment":""},{"position":{"x":215,"y":92},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: RightSideSameScaleSecondCubePartTwo
}
