package org.usfirst.frc.team4488.robot.systems;

import static org.junit.Assert.*;

import com.ctre.phoenix.motorcontrol.SensorCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.usfirst.frc.team4488.lib.util.NavX;
import org.usfirst.frc.team4488.lib.util.PreferencesParser;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  Drive.class,
  NavX.class,
  Controllers.class,
  SmartDrive.class,
  Logging.class,
  PreferencesParser.class
})
public class DriveTest {

  WPI_TalonSRX mockTalon = PowerMockito.mock(WPI_TalonSRX.class);
  Solenoid mockSolenoid = PowerMockito.mock(Solenoid.class);
  Controllers mockXbox = PowerMockito.mock(Controllers.class);
  PreferencesParser mockPrefs = PowerMockito.mock(PreferencesParser.class);
  Logging mockLog = PowerMockito.mock(Logging.class);

  SensorCollection mockSensorCollection = PowerMockito.mock(SensorCollection.class);
  // @TODO why can't I mock an actual NavX device, i.e. the AHRS
  NavX mockNavX = PowerMockito.mock(NavX.class);

  @Test
  public void testConstructor() throws Exception {
    PowerMockito.whenNew(WPI_TalonSRX.class).withAnyArguments().thenReturn(mockTalon);
    PowerMockito.whenNew(Solenoid.class).withAnyArguments().thenReturn(mockSolenoid);
    PowerMockito.whenNew(NavX.class).withAnyArguments().thenReturn(mockNavX);
    PowerMockito.when(mockTalon.getSensorCollection()).thenReturn(mockSensorCollection);
    PowerMockito.whenNew(Controllers.class).withAnyArguments().thenReturn(mockXbox);
    PowerMockito.whenNew(PreferencesParser.class).withAnyArguments().thenReturn(mockPrefs);
    PowerMockito.when(mockPrefs.getDouble("DriveUSP")).thenReturn(0.0);
    PowerMockito.whenNew(Logging.class).withAnyArguments().thenReturn(mockLog);

    Drive drive = new Drive();
    assertNotNull(drive);
  }
}
