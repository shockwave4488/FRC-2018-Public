package org.usfirst.frc.team4488.robot.systems;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.autonomous.actions.Action;
import org.usfirst.frc.team4488.robot.autonomous.actions.ForwardFlick;
import org.usfirst.frc.team4488.robot.autonomous.actions.IntakeAndHold;
import org.usfirst.frc.team4488.robot.autonomous.actions.RezeroLift;
import org.usfirst.frc.team4488.robot.autonomous.actions.RezeroManipulator;
import org.usfirst.frc.team4488.robot.autonomous.actions.RumbleSecondary;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Controllers.SecondaryState;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.ArduinoAnalog;

public class RoutineManager extends Subsystem {

  private Action routine = null;
  private Action rumble = null;
  private boolean selectLastPressed = false;
  private boolean finishedIntake = false;
  private boolean finishedFlick = false;
  private boolean finishedRezeroManip = false;
  private boolean finishedAutoStack = false;
  private boolean finishedRezeroLift = false;

  private ArduinoAnalog arduinoAnalog;
  private Logging logger;
  private Controllers xbox;

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          synchronized (RoutineManager.this) {
            arduinoAnalog = ArduinoAnalog.getInstance();
            xbox = Controllers.getInstance();
          }
        }
        /** TODO */
        @Override
        public void onLoop(double timestamp) {
          synchronized (RoutineManager.this) {
            boolean runIntake =
                xbox.getLeftTrigger(xbox.m_secondary)
                    && xbox.secondaryState == SecondaryState.auto
                    && !finishedIntake;
            boolean runFlick = xbox.getRightTrigger(xbox.m_primary) && !finishedFlick;
            boolean runRezeroManip = xbox.getX(xbox.m_primary) && !finishedRezeroManip;
            boolean runRezeroLift = xbox.getB(xbox.m_primary) && !finishedRezeroLift;

            handleIntakeAndHold(runIntake);
            handleFlick(runFlick);
            handleRezeroManip(runRezeroManip);
            handleRezeroLift(runRezeroLift);

            if (!(xbox.getLeftTrigger(xbox.m_secondary)
                && xbox.secondaryState == SecondaryState.auto)) {
              finishedIntake = false;
            }

            if (!(xbox.getRightTrigger(xbox.m_primary))) {
              finishedFlick = false;
            }
            if (!(xbox.getX(xbox.m_primary))) {
              finishedRezeroManip = false;
            }
            if (!(xbox.getLeftBumper(xbox.m_secondary))) {
              finishedAutoStack = false;
            }
            if (!(xbox.getB(xbox.m_primary))) {
              finishedRezeroLift = false;
            }

            if (routine != null) {
              routine.update();
              if (routine.isFinished()) {
                routine.done();
                if (routine.getClass() == IntakeAndHold.class) {
                  finishedIntake = true;
                } else if (routine.getClass() == ForwardFlick.class) {
                  finishedFlick = true;
                } else if (routine.getClass() == RezeroManipulator.class) {
                  finishedRezeroManip = true;
                } else if (routine.getClass() == RezeroLift.class) {
                  finishedRezeroLift = true;
                }
                routine = null;
              }
            }

            if (rumble != null) {
              rumble.update();
              if (rumble.isFinished()) {
                rumble.done();
                rumble = null;
              }
            }
          }
        }

        @Override
        public void onStop(double timestamp) {
          reset();
          stop();
        }
      };

  private static RoutineManager sInstance = null;

  /** @return an instance of the routine manager */
  public static RoutineManager getInstance() {
    if (sInstance == null) {
      sInstance = new RoutineManager();
    }

    return sInstance;
  }

  public RoutineManager() {
    logger = Logging.getInstance();
  }

  @Override
  public void writeToLog() {}

  @Override
  public void updateSmartDashboard() {}

  @Override
  public void stop() {}

  @Override
  public void zeroSensors() {}

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {}
  /**
   * When select is pressed the controller rumbles and swiches to auto or manual. If right trigger
   * is presses the cube is flicked.
   */
  @Override
  public void controllerUpdate() {
    if (xbox.getSelect(xbox.m_secondary) && !selectLastPressed) {
      if (xbox.secondaryState == SecondaryState.auto) {
        xbox.secondaryState = SecondaryState.manual;
        reset();
        routine = null;
      } else {
        xbox.secondaryState = SecondaryState.auto;
      }
      rumble = new RumbleSecondary();
      rumble.start();
    }
    selectLastPressed = xbox.getSelect(xbox.m_secondary);

    /*
    if (xbox.getRightTrigger(xbox.m_primary)) {
      addAction(new ForwardFlick());
    }
    */
    // TODO Put this back in somewhere else
    SmartDashboard.putBoolean("MANUAL MODE", xbox.secondaryState == SecondaryState.manual);
  }

  private void handleIntakeAndHold(boolean val) {
    if (val) {
      if (routine == null) {
        routine = new IntakeAndHold();
        routine.start();
      }
    } else {
      if (routine != null) {
        if (routine.getClass() == IntakeAndHold.class) {
          Controllers.getInstance().m_primary.setRumble(RumbleType.kLeftRumble, 0);
          Intake.getInstance().deployIn();
          routine.done();
          routine = null;
        }
      }
    }
  }

  private void handleFlick(boolean val) {
    if (val) {
      if (routine == null) {
        routine = new ForwardFlick();
        routine.start();
      }
    } else {
      if (routine != null) {
        if (routine.getClass() == ForwardFlick.class) {
          routine = null;
        }
      }
    }
  }

  private void handleRezeroManip(boolean val) {
    if (val) {
      if (routine == null) {
        routine = new RezeroManipulator();
        routine.start();
      }
    } else {
      if (routine != null) {
        if (routine.getClass() == RezeroManipulator.class) {
          Manipulator.getInstance().unLock();
          if (routine.getClass() == RezeroLift.class) {
            routine = null;
          }
        }
      }
    }
  }

  private void handleRezeroLift(boolean val) {
    if (val) {
      if (routine == null) {
        routine = new RezeroLift();
        routine.start();
      }
    } else {
      if (routine != null) {
        if (routine.getClass() == RezeroLift.class) {
          routine = null;
          Forklift.getInstance().setPower(0);
          Forklift.getInstance().setHeight(Forklift.getInstance().getCurrentHeight());
          Forklift.getInstance().unLock();
        }
      }
    }
  }

  /**
   * @param type
   * @return finds instance in list
   */
  @Override
  public void reset() {
    Manipulator.getInstance().setHasCube(false);
    routine = null;
  }
}
