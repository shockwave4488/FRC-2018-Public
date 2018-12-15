package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;

public class RightSideSameScaleStayOutOfWay implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 45, 0, 0));
    sWaypoints.add(new Waypoint(215, 25, 5, 120, "LiftUp"));
    sWaypoints.add(new Waypoint(280, 25, 0, 120, "Score"));
    sWaypoints.add(new Waypoint(320, 25, 30, 40));
    sWaypoints.add(new Waypoint(320, 60, 0, 40));

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
  // WAYPOINT_DATA:[{"position":{"x":15,"y":45},"speed":0,"radius":0,"comment":""},{"position":{"x":215,"y":25},"speed":120,"radius":5,"comment":""},{"position":{"x":280,"y":25},"speed":120,"radius":0,"comment":""},{"position":{"x":320,"y":25},"speed":40,"radius":30,"comment":""},{"position":{"x":320,"y":60},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: RightSideSameScaleStayOutOfWay
}
