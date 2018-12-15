package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.*;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class RightCrossLineOwnSwitchDiffSide implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 60, 0, 0));
    sWaypoints.add(new Waypoint(230, 60, 25, 60));
    sWaypoints.add(new Waypoint(240, 125, 0, 60));
    sWaypoints.add(new Waypoint(240, 220, 0, 60));
    sWaypoints.add(new Waypoint(240, 280, 50, 60));
    sWaypoints.add(new Waypoint(155, 261, 0, 60));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 60), Rotation2d.fromDegrees(180.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:
  // [{"position":{"x":15,"y":60},"speed":0,"radius":0,"comment":""},{"position":{"x":230,"y":60},"speed":60,"radius":25,"comment":""},{"position":{"x":240,"y":125},"speed":60,"radius":0,"comment":""},{"position":{"x":240,"y":220},"speed":60,"radius":0,"comment":""},{"position":{"x":240,"y":280},"speed":60,"radius":50,"comment":""},{"position":{"x":155,"y":261},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: BotCrossLineOwnSwitchDiffSide
}
