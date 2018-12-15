package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class LeftSideSameScaleSecondCubePartThree implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(215, 240, 0, 0));
    sWaypoints.add(new Waypoint(235, 265, 15, 40));
    sWaypoints.add(new Waypoint(205, 275, 0, 40));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(215, 240), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return true;
  }
  // WAYPOINT_DATA:[{"position":{"x":215,"y":240},"speed":0,"radius":0,"comment":""},{"position":{"x":235,"y":265},"speed":40,"radius":15,"comment":""},{"position":{"x":205,"y":275},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: true
  // FILE_NAME: LeftSideSameScaleSecondCubePartThree
}
