package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class LeftSideCrossLineStopEarlyPath implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 280, 0, 0));
    sWaypoints.add(new Waypoint(235, 280, 60, 60));
    sWaypoints.add(new Waypoint(235, 160, 0, 60));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 280), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":15,"y":280},"speed":0,"radius":0,"comment":""},{"position":{"x":235,"y":280},"speed":60,"radius":60,"comment":""},{"position":{"x":235,"y":160},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftSideCrossLineStopEarlyPath
}
