package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightCrossLineOwnScaleDiffSidePath implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 45, 0, 0));
    sWaypoints.add(new Waypoint(235, 45, 60, 60));
    sWaypoints.add(new Waypoint(235, 170, 0, 60, "LiftUp"));
    sWaypoints.add(new Waypoint(235, 245, 20, 50));
    sWaypoints.add(new Waypoint(255, 245, 0, 30));
    sWaypoints.add(new Waypoint(280, 245, 0, 40));

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
  // WAYPOINT_DATA:[{"position":{"x":15,"y":45},"speed":0,"radius":0,"comment":""},{"position":{"x":235,"y":45},"speed":60,"radius":60,"comment":""},{"position":{"x":235,"y":170},"speed":60,"radius":0,"comment":""},{"position":{"x":235,"y":245},"speed":50,"radius":20,"comment":""},{"position":{"x":255,"y":245},"speed":30,"radius":0,"comment":""},{"position":{"x":280,"y":245},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: RightCrossLineOwnScaleDiffSidePath
}
