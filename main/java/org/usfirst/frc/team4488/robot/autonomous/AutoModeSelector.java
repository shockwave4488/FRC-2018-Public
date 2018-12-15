package org.usfirst.frc.team4488.robot.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import openrio.powerup.MatchData;
import openrio.powerup.MatchData.GameFeature;
import openrio.powerup.MatchData.OwnedSide;
import org.usfirst.frc.team4488.robot.autonomous.modes.*;

/*
 * Adds radio buttons to the SmartDashboard interface that allow the user to select starting position
 * and desired type of auto routine. Decision logic is used to combine these choices with the
 * Field Management Data (FMS) to select the type of auto routine you want to run.
 */
public class AutoModeSelector {

  public static final String AUTO_OPTIONS_DASHBOARD_KEY = "auto_options";
  public static final String SELECTED_AUTO_MODE_DASHBOARD_KEY = "selected_auto_mode";
  public static final String left = "Left";
  public static final String right = "Right";
  public static final String middle = "Middle";
  public static final String crossLine = "CrossLine";
  public static final String ownSwitch = "Priority Switch";
  public static final String ownScale = "Priority Scale";
  public static final String red = "Red";
  public static final String blue = "Blue";
  private static SendableChooser<AutoModeBase> autoChooser = new SendableChooser<AutoModeBase>();
  private static SendableChooser<String> autoSideChooser = new SendableChooser<String>();
  private static SendableChooser<Boolean> stayOnSideChooser = new SendableChooser<Boolean>();
  private static SendableChooser<Boolean> stayOutOfWayChooser = new SendableChooser<Boolean>();
  private static SendableChooser<Integer> amountOfCubesChooser = new SendableChooser<Integer>();
  public static OwnedSide currentNearSwitchSideOwned = OwnedSide.UNKNOWN;
  public static OwnedSide currentScaleSideOwned = OwnedSide.UNKNOWN;
  public static OwnedSide currentFarSwitchSideOwned = OwnedSide.UNKNOWN;
  private static boolean runCrossLine;
  private static boolean runOwnSwitch;
  private static boolean runOwnScale;

  private static AutoModeBase[][] autos;

  /*
   * returns the auto mode that you will run for your autonomous session
   */
  public static AutoModeBase getSelectedAutoMode() {
    AutoModeBase selectedAutoMode = determineAutoModeToUse();
    displayAutoModeDetails(selectedAutoMode);
    return selectedAutoMode;
  }
  /*
   * displays radio buttons on SmartDashboard. See determineAutoModeToUse() for details
   */
  public static void init() {
    initAutoModeSelector();
    initSideSelector();
    initStayOnSideSelector();
    initStayOutOfWaySelector();
    initAmountOfCubesSelector();
    smartDashboardMatchData();

    RightSwitchFrontFlickSameSide rightSameSwitch = new RightSwitchFrontFlickSameSide();
    RightSwitchFrontFlickDiffSide rightDiffSwitch = new RightSwitchFrontFlickDiffSide();
    RightCrossLineOwnScaleSameSide rightSameScale = new RightCrossLineOwnScaleSameSide();
    RightCrossLineOwnScaleDiffSide rightDiffScale = new RightCrossLineOwnScaleDiffSide();
    RightSideCrossLineStopEarly crossLineRight = new RightSideCrossLineStopEarly();
    LeftSwitchFrontFlickSameSide leftSameSwitch = new LeftSwitchFrontFlickSameSide();
    LeftSwitchFrontFlickDiffSide leftDiffSwitch = new LeftSwitchFrontFlickDiffSide();
    LeftCrossLineOwnScaleSameSide leftSameScale = new LeftCrossLineOwnScaleSameSide();
    LeftCrossLineOwnScaleDiffSide leftDiffScale = new LeftCrossLineOwnScaleDiffSide();
    LeftSideCrossLineStopEarly crossLineLeft = new LeftSideCrossLineStopEarly();
    LeftCrossLineOwnScaleDiffSideAndDiffSideSwitch LeftDiffScaleAndDiffSwitch =
        new LeftCrossLineOwnScaleDiffSideAndDiffSideSwitch();
    CrossLine crossLine = new CrossLine();
    MidCrossLineOwnLeftSwitchAndLeftScale centerLeftSwitchLeftScale =
        new MidCrossLineOwnLeftSwitchAndLeftScale();
    MidCrossLineOwnLeftSwitchAndRightScale centerLeftSwitchRightScale =
        new MidCrossLineOwnLeftSwitchAndRightScale();
    MidCrossLineOwnRightSwitchAndRightScale centerRightSwitchRightScale =
        new MidCrossLineOwnRightSwitchAndRightScale();
    MidCrossLineOwnRightSwitchAndLeftScale centerRightSwitchLeftScale =
        new MidCrossLineOwnRightSwitchAndLeftScale();
    RightCrossLineOwnRightScaleAndRightSwitch RightSameScaleAndSameSwitch =
        new RightCrossLineOwnRightScaleAndRightSwitch();
    LeftCrossLineOwnLeftScaleAndLeftSwitch LeftSameScaleAndSameSwitch =
        new LeftCrossLineOwnLeftScaleAndLeftSwitch();

    autos =
        new AutoModeBase[][] {
          new AutoModeBase[] { // y = 0, RRR
            rightSameSwitch,
            rightSameSwitch,
            rightSameScale,
            rightSameScale,
            crossLineLeft,
            leftDiffSwitch,
            crossLineLeft,
            leftDiffScale,
            centerRightSwitchRightScale // TODO Should be Center Right Switch
          },
          new AutoModeBase[] { // y = 1, RLR
            rightSameSwitch,
            rightSameSwitch,
            rightSameSwitch,
            rightDiffScale,
            leftSameScale,
            leftDiffSwitch,
            leftSameScale,
            leftSameScale,
            centerRightSwitchLeftScale // TODO Should be Center Right Switch
          },
          new AutoModeBase[] { // y = 2, LRR
            rightSameScale,
            rightDiffSwitch,
            rightSameScale,
            rightSameScale,
            leftSameSwitch,
            leftSameSwitch,
            leftSameSwitch,
            leftDiffScale,
            centerLeftSwitchRightScale // TODO Should be Center Left Switch
          },
          new AutoModeBase[] { // y = 3, LLR
            crossLineRight,
            rightDiffSwitch,
            crossLineRight,
            rightDiffScale,
            leftSameSwitch,
            leftSameSwitch,
            leftSameScale,
            leftSameScale,
            centerLeftSwitchLeftScale // TODO Should be Center Left Switch
          }
        };
  }

  private static void initAutoModeSelector() {
    autoChooser.addDefault(crossLine, new CrossLine());
    autoChooser.addObject(ownSwitch, new RightSwitchFrontFlickSameSide());
    autoChooser.addObject(ownScale, new RightCrossLineOwnScaleSameSide());
    SmartDashboard.putData("Auto Modes", autoChooser);
  }

  private static void initStayOutOfWaySelector() {
    stayOutOfWayChooser.addDefault("Dont stay out of way", false);
    stayOutOfWayChooser.addObject("Stay out of way", true);
    SmartDashboard.putData("Stay Out Of Way Selector", stayOutOfWayChooser);
  }

  private static void initSideSelector() {
    autoSideChooser.addDefault(right, right);
    autoSideChooser.addObject(left, left);
    autoSideChooser.addObject(middle, middle);
    SmartDashboard.putData("SideSelect", autoSideChooser);
  }

  private static void initStayOnSideSelector() {
    stayOnSideChooser.addDefault("Stay On Side", new Boolean(true));
    stayOnSideChooser.addObject("Dont Stay On Side", new Boolean(false));
    SmartDashboard.putData("StayOnSide", stayOnSideChooser);
  }

  private static void initAmountOfCubesSelector() {
    amountOfCubesChooser.addDefault("1 Cube", new Integer(1));
    SmartDashboard.putData("AmountOfCubes", amountOfCubesChooser);
  }
  /*
   * Makes a decision about which auto mode to use based on three sets of variables:
   * 1) what the user selected in SmartDashboard (crossLine, crossLineOwnSwitch etc)
   * 2) what the robot starting position is (right, middle, left)
   * 3) What the FMS data indicates for ownership of the switches and scale
   * NOTE: because the FMS data reports the switch and scale ownership relative to your alliance,
   * we do not need to worry about color when picking our auto routine. We also don't care about the far switch as none of our auto routines will try to go there.
   */
  /** @return */
  private static AutoModeBase determineAutoModeToUse() {
    waitForValidFMSData(5.0);
    /*
     * Assign Conditionals
     */
    currentNearSwitchSideOwned = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
    currentScaleSideOwned = MatchData.getOwnedSide(GameFeature.SCALE);
    // Comment out 183 to not force left scale
    // currentScaleSideOwned = OwnedSide.LEFT;
    currentFarSwitchSideOwned = MatchData.getOwnedSide(GameFeature.SWITCH_FAR);
    runCrossLine = (autoChooser.getSelected().getClass() == CrossLine.class);
    runOwnSwitch = (autoChooser.getSelected().getClass() == RightSwitchFrontFlickSameSide.class);
    runOwnScale = (autoChooser.getSelected().getClass() == RightCrossLineOwnScaleSameSide.class);
    boolean stayOnSide = stayOnSideChooser.getSelected().booleanValue();
    boolean stayOutOfWay = stayOutOfWayChooser.getSelected().booleanValue();

    AutoModeBase autoModeToRun = new CrossLine(); // guarantee a default to crossLine
    if (!runCrossLine) {
      autoModeToRun = autos[getOwnershipIndex()][getDesiredAutoIndex()];
    }

    if (autoModeToRun.getClass() == RightCrossLineOwnScaleSameSide.class
        && stayOnSide
        && stayOutOfWay) {
      autoModeToRun = new RightCrossLineOwnScaleSameSideBackUp();
    }
    if (autoModeToRun.getClass() == LeftCrossLineOwnScaleSameSide.class
        && stayOnSide
        && stayOutOfWay) {
      autoModeToRun = new LeftCrossLineOwnScaleSameSideBackUp();
    }
    return autoModeToRun;
  }

  private static int getDesiredAutoIndex() {
    boolean startOnRight = (autoSideChooser.getSelected().equals(right));
    boolean startInMiddle = (autoSideChooser.getSelected().equals(middle));
    boolean stayOnSide = stayOnSideChooser.getSelected().booleanValue();

    int autoIndex = 0;
    if (!startOnRight) {
      autoIndex += 4;
    }
    if (runOwnScale) {
      autoIndex += 2;
    }
    if (!stayOnSide) {
      autoIndex += 1;
    }
    if (startInMiddle) {
      autoIndex = 8;
    }

    return autoIndex;
  }

  private static int getOwnershipIndex() {
    boolean ownRightSideOfNearSwitch = (currentNearSwitchSideOwned == OwnedSide.RIGHT);
    boolean ownRightSideOfScale = (currentScaleSideOwned == OwnedSide.RIGHT);

    int ownershipIndex = 0;
    if (!ownRightSideOfNearSwitch) {
      ownershipIndex += 2;
    }
    if (!ownRightSideOfScale) {
      ownershipIndex += 1;
    }

    return ownershipIndex;
  }

  /*
   * used to display the auto mode details, what starting side we selected,
   * what the interpreted FMS data said about the ownership of switch and scale,
   * and finally what actual auto mode was selected.
   */
  private static void displayAutoModeDetails(AutoModeBase selectedAutoMode) {
    System.out.println(
        "You are starting on the " + autoSideChooser.getSelected().toString() + " of the field");
    System.out.println(
        "You own the  " + currentNearSwitchSideOwned.toString() + " side of the Near Switch");
    System.out.println("You own the  " + currentScaleSideOwned.toString() + " side of the Scale");
    System.out.println(
        "You own the  " + currentFarSwitchSideOwned.toString() + " side of the Far Switch");

    if (runOwnSwitch) {
      System.out.println("Prioritized Feature: Switch");
    } else if (runOwnScale) {
      System.out.println("Prioritized Feature: Scale");
    } else {
      System.out.println("Prioritized Feature: None, crossing line");
    }

    System.out.println("You are running: " + selectedAutoMode.getClass().getSimpleName());
  }

  private static void waitForValidFMSData(double seconds) {
    MatchData.OwnedSide testingFMS = MatchData.OwnedSide.UNKNOWN;
    double currentTime = Timer.getFPGATimestamp();
    double waitLimit = seconds + currentTime; // 5 seconds for FMS to get it's act together
    while (currentTime < waitLimit && testingFMS != MatchData.OwnedSide.UNKNOWN) {
      testingFMS = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
      currentTime = Timer.getFPGATimestamp();
    }
  }

  private static void smartDashboardMatchData() {
    String sideOfNearSwitch = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR).toString();
    String sideOfScale = MatchData.getOwnedSide(MatchData.GameFeature.SCALE).toString();
    String sideOfFarSwitch = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_FAR).toString();
    SmartDashboard.putString("Near Switch", sideOfNearSwitch);
    SmartDashboard.putString("Scale", sideOfScale);
    SmartDashboard.putString("Far Switch", sideOfFarSwitch);
  }

  public static void updateFMS() {
    waitForValidFMSData(2);
    currentNearSwitchSideOwned = MatchData.getOwnedSide(GameFeature.SWITCH_NEAR);
    currentScaleSideOwned = MatchData.getOwnedSide(GameFeature.SCALE);
    currentFarSwitchSideOwned = MatchData.getOwnedSide(GameFeature.SWITCH_FAR);
  }
}
