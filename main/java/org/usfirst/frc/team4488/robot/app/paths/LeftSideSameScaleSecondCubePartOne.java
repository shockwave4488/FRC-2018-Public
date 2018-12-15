package org.usfirst.frc.team4488.robot.app.paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;

public class LeftSideSameScaleSecondCubePartOne implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(274, 247, 0, 0));
    sWaypoints.add(new Waypoint(250, 265, 30, 45, "liftDown"));
    sWaypoints.add(new Waypoint(250, 300, 0, 30));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(274, 247), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return false;
  }
  // WAYPOINT_DATA:[{"position":{"x":274,"y":247},"speed":0,"radius":0,"comment":""},{"position":{"x":250,"y":265},"speed":45,"radius":30,"comment":""},{"position":{"x":250,"y":300},"speed":30,"radius":0,"comment":""}]
  // IS_REVERSED: false
  // FILE_NAME: LeftSideSameScaleSecondCubePartOne
}
