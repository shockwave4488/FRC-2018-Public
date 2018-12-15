package org.usfirst.frc.team4488.robot.systems;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.usfirst.frc.team4488.lib.util.PreferencesParser;
import org.usfirst.frc.team4488.robot.operator.Controllers;
import org.usfirst.frc.team4488.robot.operator.Logging;
import org.usfirst.frc.team4488.robot.sensors.HallEffect;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Forklift.class, Controllers.class, Logging.class, PreferencesParser.class})
public class ForkliftTest {
  PreferencesParser mockPrefs = PowerMockito.mock(PreferencesParser.class);
  Controllers mockXbox = PowerMockito.mock(Controllers.class);
  Logging mockLog = PowerMockito.mock(Logging.class);
  HallEffect mockHallEffect = PowerMockito.mock(HallEffect.class);

  @Test
  public void test() throws Exception {
    PowerMockito.whenNew(PreferencesParser.class).withAnyArguments().thenReturn(mockPrefs);
    PowerMockito.whenNew(Controllers.class).withAnyArguments().thenReturn(mockXbox);
    PowerMockito.whenNew(Logging.class).withAnyArguments().thenReturn(mockLog);
    PowerMockito.whenNew(HallEffect.class).withAnyArguments().thenReturn(mockHallEffect);

    PowerMockito.when(Controllers.getInstance()).thenReturn(mockXbox);

    Forklift lift = new Forklift();
    assertNotNull(lift);
  }
}
