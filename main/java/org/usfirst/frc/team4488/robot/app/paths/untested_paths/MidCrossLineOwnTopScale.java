package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.*;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class MidCrossLineOwnTopScale implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 160, 0, 60));
    sWaypoints.add(new Waypoint(35, 160, 10, 60));
    sWaypoints.add(new Waypoint(140, 273, 30, 60));
    sWaypoints.add(new Waypoint(330, 273, 0, 60));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 160), Rotation2d.fromDegrees(180.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:
  // [{"position":{"x":15,"y":160},"speed":60,"radius":0,"comment":""},{"position":{"x":35,"y":160},"speed":60,"radius":10,"comment":""},{"position":{"x":140,"y":273},"speed":60,"radius":30,"comment":""},{"position":{"x":330,"y":273},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: BotCrossLineOwnScaleDiffSide
}
