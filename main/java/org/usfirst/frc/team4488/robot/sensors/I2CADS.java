package org.usfirst.frc.team4488.robot.sensors;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class I2CADS {

  private I2C ads;
  private int defaultAddress = 0x48;
  private int configDelay = 1;

  private byte conversionPointer = 0b00000000;
  private byte configPointer = 0b00000001;

  private int queue = 0b00000011; // LSB
  private int latch = 0b00000000; // LSB
  private int alert = 0b00000000; // LSB
  private int comparator = 0b00000000; // LSB
  private int sampleRate = 0b10000000; // LSB
  private int readMode = 0b01000000; // MSB
  private int pga = 0b00000000; // MSB
  private int os = 0b10000000; // MSB

  private int analog0 = 0b00000000;
  private int analog1 = 0b00010000;
  private int analog2 = 0b00100000;
  private int analog3 = 0b00110000;

  public I2CADS() {
    // 0x48 is the default address
    ads = new I2C(Port.kOnboard, defaultAddress);
  }

  public I2CADS(int address) {
    ads = new I2C(Port.kOnboard, address);
  }

  public void setSensor(int sensor) {
    byte configLSB = (byte) (queue | latch | alert | comparator | sampleRate);
    byte configMSB = (byte) (os | pga | readMode);
    switch (sensor) {
      case 0:
        configMSB |= analog0;
        break;
      case 1:
        configMSB |= analog1;
        break;
      case 2:
        configMSB |= analog2;
        break;
      case 3:
        configMSB |= analog3;
        break;
    }

    byte[] toWrite = new byte[] {configPointer, configMSB, configLSB};
    ads.writeBulk(toWrite);
  }

  public double read(int sensor) {
    setSensor(sensor);
    wait(configDelay);
    return read();
  }

  public double read() {
    byte[] buffer = new byte[2];
    ads.read(conversionPointer, buffer.length, buffer);
    int msb = (buffer[0] << 8);
    int total = msb | buffer[1];
    total >>= 4;
    return total;
  }

  private void wait(int milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
