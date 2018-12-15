package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightSideSameScaleSecondCubePartThree implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(215, 92, 0, 0));
    sWaypoints.add(new Waypoint(235, 60, 15, 40));
    sWaypoints.add(new Waypoint(205, 50, 0, 40));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(215, 92), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return true;
  }
  // WAYPOINT_DATA:[{"position":{"x":215,"y":92},"speed":0,"radius":0,"comment":""},{"position":{"x":235,"y":60},"speed":40,"radius":15,"comment":""},{"position":{"x":205,"y":50},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: true
  // FILE_NAME: RightSideSameScaleSecondCubePartThree
}
