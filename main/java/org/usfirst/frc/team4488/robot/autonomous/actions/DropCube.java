package org.usfirst.frc.team4488.robot.autonomous.actions;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.systems.Manipulator;

public class DropCube extends RunOnceAction {

  @Override
  public void runOnce() {
    Manipulator.getInstance().unClamp();
    wait(100);
    SmartDashboard.putBoolean("DropCube done", true);
  }

  private void wait(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
