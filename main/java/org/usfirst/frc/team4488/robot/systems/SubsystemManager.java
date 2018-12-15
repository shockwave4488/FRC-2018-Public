package org.usfirst.frc.team4488.robot.systems;

import java.util.List;
import org.usfirst.frc.team4488.robot.loops.Looper;

/** Used to reset, start, stop, and update all subsystems at once */
public class SubsystemManager {

  private final List<Subsystem> mAllSubsystems;

  public SubsystemManager(List<Subsystem> allSubsystems) {
    mAllSubsystems = allSubsystems;
  }

  public void updateSmartDashboard() {
    mAllSubsystems.forEach((s) -> s.updateSmartDashboard());
  }

  public void writeToLog() {
    mAllSubsystems.forEach((s) -> s.writeToLog());
  }

  public void stop() {
    mAllSubsystems.forEach((s) -> s.stop());
  }

  public void zeroSensors() {
    mAllSubsystems.forEach((s) -> s.zeroSensors());
  }

  public void registerEnabledLoops(Looper enabledLooper) {
    mAllSubsystems.forEach((s) -> s.registerEnabledLoops(enabledLooper));
  }

  public void controllerUpdates() {
    mAllSubsystems.forEach((s) -> s.controllerUpdate());
  }

  public void updatePrefs() {
    mAllSubsystems.forEach((s) -> s.updatePrefs());
  }

  public void reset() {
    mAllSubsystems.forEach((s) -> s.reset());
  }
}
