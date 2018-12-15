package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class MidCrossLineOwnRightSwitchAndRightScalePart1 implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(15, 155, 0, 0));
    sWaypoints.add(new Waypoint(50, 155, 30, 60));
    sWaypoints.add(new Waypoint(90, 105, 15, 60));
    sWaypoints.add(new Waypoint(115, 105, 0, 40));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(15, 155), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":15,"y":155},"speed":0,"radius":0,"comment":""},{"position":{"x":50,"y":155},"speed":60,"radius":30,"comment":""},{"position":{"x":90,"y":105},"speed":60,"radius":15,"comment":""},{"position":{"x":115,"y":105},"speed":40,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: MidCrossLineOwnRightSwitchAndRightScalePart1
}
