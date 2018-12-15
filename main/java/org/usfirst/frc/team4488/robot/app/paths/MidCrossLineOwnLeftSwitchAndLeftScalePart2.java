package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class MidCrossLineOwnLeftSwitchAndLeftScalePart2 implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(115, 220, 0, 0));
    sWaypoints.add(new Waypoint(90, 220, 20, 60, "LiftDown"));
    sWaypoints.add(new Waypoint(50, 155, 10, 40));
    sWaypoints.add(new Waypoint(30, 155, 0, 30));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(115, 220), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return true;
  }
  // WAYPOINT_DATA:[{"position":{"x":115,"y":220},"speed":0,"radius":0,"comment":""},{"position":{"x":90,"y":220},"speed":60,"radius":20,"comment":""},{"position":{"x":50,"y":155},"speed":40,"radius":10,"comment":""},{"position":{"x":30,"y":155},"speed":30,"radius":0,"comment":""}]
  // IS_REVERSED: true
  // FILE_NAME: MidCrossLineOwnLeftSwitchAndLeftScalePart2
}
