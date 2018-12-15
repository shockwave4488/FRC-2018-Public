package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.*;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class LeftCrossLineOwnSwitchDiffSide implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 260, 0, 60));
    sWaypoints.add(new Waypoint(240, 260, 30, 60));
    sWaypoints.add(new Waypoint(240, 100, 0, 60));
    sWaypoints.add(new Waypoint(240, 60, 30, 60));
    sWaypoints.add(new Waypoint(160, 60, 0, 60));

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
  // [{"position":{"x":15,"y":260},"speed":60,"radius":0,"comment":""},{"position":{"x":240,"y":260},"speed":60,"radius":30,"comment":""},{"position":{"x":240,"y":100},"speed":60,"radius":0,"comment":""},{"position":{"x":240,"y":60},"speed":60,"radius":30,"comment":""},{"position":{"x":160,"y":60},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: TopCrossLineOwnSwitchDiffSide
}
