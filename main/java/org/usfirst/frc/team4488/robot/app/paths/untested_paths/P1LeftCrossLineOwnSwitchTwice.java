package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.*;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class P1LeftCrossLineOwnSwitchTwice implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 260, 0, 0));
    sWaypoints.add(new Waypoint(40, 260, 2, 60));
    sWaypoints.add(new Waypoint(100, 265, 2, 60));
    sWaypoints.add(new Waypoint(170, 265, 0, 60));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 260), Rotation2d.fromDegrees(180.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:
  // [{"position":{"x":15,"y":260},"speed":0,"radius":0,"comment":""},{"position":{"x":40,"y":260},"speed":60,"radius":2,"comment":""},{"position":{"x":100,"y":265},"speed":60,"radius":2,"comment":""},{"position":{"x":170,"y":265},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: P1LeftCrossLineOwnSwitchTwice
}
