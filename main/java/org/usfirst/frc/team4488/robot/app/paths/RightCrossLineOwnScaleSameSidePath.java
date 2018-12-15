package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightCrossLineOwnScaleSameSidePath implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 45, 0, 0));
    sWaypoints.add(new Waypoint(130, 45, 20, 160, "LiftUp"));
    sWaypoints.add(new Waypoint(288, 82, 0, 70));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 45), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":15,"y":45},"speed":0,"radius":0,"comment":""},{"position":{"x":130,"y":45},"speed":160,"radius":20,"comment":""},{"position":{"x":288,"y":82},"speed":70,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: RightCrossLineOwnScaleSameSidePath
}
