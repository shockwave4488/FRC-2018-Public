package org.usfirst.frc.team4488.robot.app.paths.untested_paths;

import java.util.ArrayList;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.app.math.RigidTransform2d;
import org.usfirst.frc.team4488.lib.util.app.math.Rotation2d;
import org.usfirst.frc.team4488.lib.util.app.math.Translation2d;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder;
import org.usfirst.frc.team4488.robot.app.paths.PathBuilder.Waypoint;
import org.usfirst.frc.team4488.robot.app.paths.PathContainer;

public class LeftCrossLineOwnScaleDiffSideSecondCubePartOne implements PathContainer {

  @Override
  public Path buildPath() {
    ArrayList<Waypoint> sWaypoints = new ArrayList<Waypoint>();
    sWaypoints.add(new Waypoint(287, 82, 0, 0));
    sWaypoints.add(new Waypoint(250, 80, 25, 30));
    sWaypoints.add(new Waypoint(250, 40, 0, 30));

    return PathBuilder.buildPathFromWaypoints(sWaypoints);
  }

  @Override
  public RigidTransform2d getStartPose() {
    return new RigidTransform2d(new Translation2d(287, 82), Rotation2d.fromDegrees(0.0));
  }

  @Override
  public boolean isReversed() {
    return true;
  }
  // WAYPOINT_DATA:
  // [{"position":{"x":280,"y":80},"speed":0,"radius":0,"comment":""},{"position":{"x":240,"y":80},"speed":60,"radius":25,"comment":""},{"position":{"x":240,"y":40},"speed":60,"radius":0,"comment":""}]
  // IS_REVERSED: true
  // FILE_NAME: LeftCrossLineOwnScaleDiffSideSecondCubePartOne
}
