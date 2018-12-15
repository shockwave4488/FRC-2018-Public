package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightSwitchFrontFlickSameSidePath implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 45, 0, 0));
    sWaypoints.add(new Waypoint(155, 45, 20, 60, "flick"));
    sWaypoints.add(new Waypoint(164, 70, 0, 60));
    sWaypoints.add(new Waypoint(165, 73, 0, 60));

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
  // WAYPOINT_DATA:[{"position":{"x":15,"y":45},"speed":0,"radius":0,"comment":""},{"position":{"x":155,"y":45},"speed":60,"radius":20,"comment":""},{"position":{"x":164,"y":70},"speed":60,"radius":0,"comment":""},{"position":{"x":165,"y":73},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: RightSwitchFrontFlickSameSidePath
}
