package org.usfirst.frc.team4488.robot.systems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.RobotMap;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;
import org.usfirst.frc.team4488.robot.operator.Logging;

public class PowerCore extends Subsystem {

  public enum ShifterState {
    Forklift,
    Climber;
  }

  private WPI_TalonSRX master;
  private WPI_TalonSRX slave1;
  private WPI_TalonSRX slave2;
  private WPI_TalonSRX slave3;

  private Solenoid shifter;

  private ShifterState shifterState = ShifterState.Forklift;

  private Forklift forklift;

  private Logging logger;

  private double ramprate = 0;

  public PowerCore() {

    logger = Logging.getInstance();

    master = new WPI_TalonSRX(RobotMap.POWERCORE_MASTER);
    slave1 = new WPI_TalonSRX(RobotMap.POWERCORE_SLAVE_1);
    slave2 = new WPI_TalonSRX(RobotMap.POWERCORE_SLAVE_2);
    slave3 = new WPI_TalonSRX(RobotMap.POWERCORE_SLAVE_3);

    slave1.follow(master);
    slave2.follow(master);
    slave3.follow(master);

    master.setInverted(true);
    slave1.setInverted(true);
    slave2.setInverted(true);
    slave3.setInverted(true);

    master.setNeutralMode(NeutralMode.Brake);
    slave1.setNeutralMode(NeutralMode.Brake);
    slave2.setNeutralMode(NeutralMode.Brake);
    slave3.setNeutralMode(NeutralMode.Brake);

    master.configOpenloopRamp(ramprate, 0);
    slave1.configOpenloopRamp(ramprate, 0);
    slave2.configOpenloopRamp(ramprate, 0);
    slave3.configOpenloopRamp(ramprate, 0);

    shifter = new Solenoid(RobotMap.POWERCORE_SHIFTER);
  }

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          synchronized (PowerCore.this) {
            forklift = Forklift.getInstance();
          }
        }
        /** Update the functions that shifts from climber to forklift and vice versa. */
        @Override
        public void onLoop(double timestamp) {
          synchronized (PowerCore.this) {
            shift();
          }
        }

        @Override
        public void onStop(double timestamp) {
          stop();
        }
      };

  private static PowerCore instance = null;

  /** @return an instance of power core */
  public static PowerCore getInstance() {
    if (instance == null) {
      instance = new PowerCore();
    }

    return instance;
  }

  public void shift() {
    // @TODO Check if this is dangerous, are we going to shift back into the
    // other gear accidentally?
    if (shifterState == ShifterState.Climber /*&& forklift.getHallEffect()*/) {
      shifter.set(true);
    } else {
      shifter.set(false);
    }
  }

  public void setPower(double power) {
    master.set(ControlMode.PercentOutput, power);
  }

  /** @return encoder ticks */
  public int getEncoder() {
    return -1 * master.getSelectedSensorPosition(0);
  }

  public void setShifterState(ShifterState state) {
    shifterState = state;
  }

  /** @return shifter state for power core */
  public ShifterState getShifterState() {
    return shifterState;
  }

  @Override
  public void updateSmartDashboard() {
    SmartDashboard.putNumber("PowerCore Encoder Ticks", getEncoder());
  }

  @Override
  public void stop() {
    setPower(0);
  }

  @Override
  public void zeroSensors() {}

  @Override
  public void registerEnabledLoops(Looper enabledLooper) {
    enabledLooper.register(mLoop);
  }

  @Override
  public void updatePrefs() {}

  public void flipShifterState() {
    if (shifterState == ShifterState.Forklift) {
      shifterState = ShifterState.Climber;
    } else {
      shifterState = ShifterState.Forklift;
    }
  }

  @Override
  public void controllerUpdate() {}

  @Override
  public void writeToLog() {}

  public void setRamprate(double ramprate) {
    master.configOpenloopRamp(ramprate, 0);
    slave1.configOpenloopRamp(ramprate, 0);
    slave2.configOpenloopRamp(ramprate, 0);
    slave3.configOpenloopRamp(ramprate, 0);
  }

  @Override
  public void reset() {}
}
