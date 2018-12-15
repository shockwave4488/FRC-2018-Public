package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class MidCrossLineOwnLeftSwitchAndRightScalePart5 implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(40, 155, 0, 0));
    sWaypoints.add(new Waypoint(90, 145, 40, 90));
    sWaypoints.add(new Waypoint(90, 55, 30, 90));
    sWaypoints.add(new Waypoint(140, 55, 0, 90));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(40, 155), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":40,"y":155},"speed":0,"radius":0,"comment":""},{"position":{"x":90,"y":145},"speed":90,"radius":40,"comment":""},{"position":{"x":90,"y":55},"speed":90,"radius":30,"comment":""},{"position":{"x":140,"y":55},"speed":90,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: MidCrossLineOwnLeftSwitchAndRightScalePart5
}
