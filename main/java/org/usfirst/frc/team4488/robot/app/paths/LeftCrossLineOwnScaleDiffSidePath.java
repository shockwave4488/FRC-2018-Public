package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class LeftCrossLineOwnScaleDiffSidePath implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 280, 0, 0));
    sWaypoints.add(new Waypoint(235, 280, 60, 90));
    sWaypoints.add(new Waypoint(235, 150, 0, 60, "LiftUp"));
    sWaypoints.add(new Waypoint(235, 80, 20, 40));
    sWaypoints.add(new Waypoint(255, 80, 0, 40));
    sWaypoints.add(new Waypoint(287, 82, 0, 40));

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
  // WAYPOINT_DATA:[{"position":{"x":15,"y":280},"speed":0,"radius":0,"comment":""},{"position":{"x":235,"y":280},"speed":90,"radius":60,"comment":""},{"position":{"x":235,"y":150},"speed":60,"radius":0,"comment":""},{"position":{"x":235,"y":80},"speed":40,"radius":20,"comment":""},{"position":{"x":255,"y":80},"speed":40,"radius":0,"comment":""},{"position":{"x":287,"y":82},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftCrossLineOwnScaleDiffSidePath
}
