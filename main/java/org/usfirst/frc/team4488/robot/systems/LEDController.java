package org.usfirst.frc.team4488.robot.systems;

import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4488.robot.loops.Loop;
import org.usfirst.frc.team4488.robot.loops.Looper;

public class LEDController extends Subsystem {
  private AnalogOutput an_out_0;
  private Timer timer;
  private Thread thread;

  private enum PinType {
    DigitalIO,
    PWM,
    AnalogIn,
    AnalogOut
  };

  private final int MAX_NAVX_MXP_DIGIO_PIN_NUMBER = 9;
  public final int MAX_NAVX_MXP_ANALOGIN_PIN_NUMBER = 3;
  public final int MAX_NAVX_MXP_ANALOGOUT_PIN_NUMBER = 1;
  public final int NUM_ROBORIO_ONBOARD_DIGIO_PINS = 10;
  public final int NUM_ROBORIO_ONBOARD_PWM_PINS = 10;
  public final int NUM_ROBORIO_ONBOARD_ANALOGIN_PINS = 4;

  private static LEDController sInstance;

  private Loop mLoop =
      new Loop() {
        @Override
        public void onStart(double timestamp) {
          stop();
          synchronized (LEDController.this) {
          }
        }

        @Override
        public void onLoop(double timestamp) {
          synchronized (LEDController.this) {
          }
        }

        @Override
        public void onStop(double timestamp) {
          stop();
        }
      };

  /* getChannelFromPin( PinType, int ) - converts from a navX-MXP */
  /* Pin type and number to the corresponding RoboRIO Channel */
  /* Number, which is used by the WPI Library functions. */
  /** @return instance of the LED controller */
  public static LEDController getInstance() {
    if (sInstance == null) {
      sInstance = new LEDController();
    }

    return sInstance;
  }

  public LEDController() {
    timer = new Timer();
    timer.reset();
    SmartDashboard.putNumber("LED Brightness %", 75);
  }
  /**
   * @param type
   * @param io_pin_number
   * @return
   * @throws IllegalArgumentException test if byte data from arduio is good
   */
  public int getChannelFromPin(PinType type, int io_pin_number) throws IllegalArgumentException {
    int roborio_channel = 0;
    if (io_pin_number < 0) {
      throw new IllegalArgumentException("Error:  navX-MXP I/O Pin #");
    }
    switch (type) {
      case DigitalIO:
        if (io_pin_number > MAX_NAVX_MXP_DIGIO_PIN_NUMBER) {
          throw new IllegalArgumentException("Error:  Invalid navX-MXP Digital I/O Pin #");
        }
        roborio_channel =
            io_pin_number + NUM_ROBORIO_ONBOARD_DIGIO_PINS + (io_pin_number > 3 ? 4 : 0);
        break;
      case PWM:
        if (io_pin_number > MAX_NAVX_MXP_DIGIO_PIN_NUMBER) {
          throw new IllegalArgumentException("Error:  Invalid navX-MXP Digital I/O Pin #");
        }
        roborio_channel = io_pin_number + NUM_ROBORIO_ONBOARD_PWM_PINS;
        break;
      case AnalogIn:
        if (io_pin_number > MAX_NAVX_MXP_ANALOGIN_PIN_NUMBER) {
          throw new IllegalArgumentException("Error:  Invalid navX-MXP Analog Input Pin #");
        }
        roborio_channel = io_pin_number + NUM_ROBORIO_ONBOARD_ANALOGIN_PINS;
        break;
      case AnalogOut:
        if (io_pin_number > MAX_NAVX_MXP_ANALOGOUT_PIN_NUMBER) {
          throw new IllegalArgumentException("Error:  Invalid navX-MXP Analog Output Pin #");
        }
        roborio_channel = io_pin_number;
        break;
    }
    return roborio_channel;
  }

  public void initializeLEDs() {
    an_out_0 = new AnalogOutput(getChannelFromPin(PinType.AnalogOut, 0));
  }

  public void setBrightness(double percent) {
    if (percent > 0) {
      double voltage = percent * 0.0205 + 0.45;
      an_out_0.setVoltage(voltage);
    } else {
      an_out_0.setVoltage(0);
    }
  }

  public void blinkLight(double onPercent) {
    thread =
        new Thread(
            () -> {
              setBrightness(75);
              timer.start();
              while (timer.get() < .5) {
                setBrightness(75);
              }
              timer.stop();
              timer.reset();
              setBrightness(0);
              timer.start();
              while (timer.get() < .5) {
                setBrightness(0);
              }
              timer.stop();
              timer.reset();
            });
    thread.start();
  }

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

  @Override
  public void controllerUpdate() {}

  @Override
  public void writeToLog() {}

  @Override
  public void reset() {}
}
