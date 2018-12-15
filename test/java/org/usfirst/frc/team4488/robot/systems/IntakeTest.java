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
import org.usfirst.frc.team4488.robot.sensors.BeamBreak;
import org.usfirst.frc.team4488.robot.sensors.HallEffect;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Intake.class, BeamBreak.class, Logging.class})
public class IntakeTest {
  WPI_TalonSRX mockTalon = PowerMockito.mock(WPI_TalonSRX.class);
  Solenoid mockSolenoid = PowerMockito.mock(Solenoid.class);
  Logging mockLog = PowerMockito.mock(Logging.class);

  // @TODO find out why I can't mock a digital input class
  BeamBreak mockBeamBreak = PowerMockito.mock(BeamBreak.class);
  HallEffect mockHallEffect = PowerMockito.mock(HallEffect.class);

  @Test
  public void testConstructor() throws Exception {
    PowerMockito.whenNew(WPI_TalonSRX.class).withAnyArguments().thenReturn(mockTalon);
    PowerMockito.whenNew(Solenoid.class).withAnyArguments().thenReturn(mockSolenoid);
    PowerMockito.whenNew(BeamBreak.class).withAnyArguments().thenReturn(mockBeamBreak);
    PowerMockito.whenNew(Logging.class).withAnyArguments().thenReturn(mockLog);
    PowerMockito.whenNew(HallEffect.class).withAnyArguments().thenReturn(mockHallEffect);

    Intake intake = new Intake();
    assertNotNull(intake);
  }
}
