package org.usfirst.frc.team4488.robot.systems;

import static org.junit.Assert.*;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.usfirst.frc.team4488.robot.operator.Logging;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PowerCore.class, Logging.class})
public class PowerCoreTest {
  WPI_TalonSRX mockTalon = PowerMockito.mock(WPI_TalonSRX.class);
  Solenoid mockSolenoid = PowerMockito.mock(Solenoid.class);
  Logging mockLog = PowerMockito.mock(Logging.class);

  @Test
  public void test() throws Exception {
    PowerMockito.whenNew(WPI_TalonSRX.class).withAnyArguments().thenReturn(mockTalon);
    PowerMockito.whenNew(Solenoid.class).withAnyArguments().thenReturn(mockSolenoid);
    PowerMockito.whenNew(Logging.class).withAnyArguments().thenReturn(mockLog);

    PowerCore powerCore = new PowerCore();
    assertNotNull(powerCore);
  }
}
