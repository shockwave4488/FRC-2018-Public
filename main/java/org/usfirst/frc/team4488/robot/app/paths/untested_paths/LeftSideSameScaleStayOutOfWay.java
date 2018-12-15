package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;

public class LeftSideSameScaleStayOutOfWay implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 280, 0, 0));
    sWaypoints.add(new Waypoint(215, 300, 5, 80, "LiftUp"));
    sWaypoints.add(new Waypoint(280, 300, 0, 80, "Score"));
    sWaypoints.add(new Waypoint(320, 300, 30, 40));
    sWaypoints.add(new Waypoint(320, 264, 0, 20));

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
  // WAYPOINT_DATA:[{"position":{"x":15,"y":280},"speed":0,"radius":0,"comment":""},{"position":{"x":215,"y":300},"speed":80,"radius":5,"comment":""},{"position":{"x":280,"y":300},"speed":80,"radius":0,"comment":""},{"position":{"x":320,"y":300},"speed":40,"radius":30,"comment":""},{"position":{"x":320,"y":264},"speed":20,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftSideSameScaleStayOutOfWay
}
