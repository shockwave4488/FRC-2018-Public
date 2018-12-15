package org.usfirst.frc.team4488.robot.app.paths;

import static org.junit.Assert.*;

import org.junit.Test;
import org.usfirst.frc.team4488.lib.util.app.control.Path;
import org.usfirst.frc.team4488.lib.util.waypointCommentParser;
import org.usfirst.frc.team4488.robot.app.paths.untested_paths.*;

public class TestWaypointComments {

  public <T extends PathContainer> void testClass(Class<T> cls) {
    PathContainer actualDrivePath;
    try {
      actualDrivePath = cls.newInstance();
      Path actual = actualDrivePath.buildPath();
      assertNotNull(actualDrivePath);
      Path expected = waypointCommentParser.readWaypointComments(actualDrivePath.getClass());
      assertEquals(actual.toString(), expected.toString());
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testDriveFiveInchesLeft() {
    testClass(DriveFiveInchesLeft.class);
  }

  @Test
  public void testRightCrossLineOwnScaleDiffSide() {
    testClass(RightCrossLineOwnScaleDiffSidePath.class);
  }

  public void DriveFiveInchesRight() {
    testClass(DriveFiveInchesRight.class);
  }

  @Test
  public void DriveFiveInchesLeft() {
    testClass(DriveFiveInchesLeft.class);
  }

  @Test
  public void LeftCrossLineOwnScaleDiffSidePath() {
    testClass(LeftCrossLineOwnScaleDiffSidePath.class);
  }

  @Test
  public void LeftCrossLineOwnScaleSameSidePath() {
    testClass(LeftCrossLineOwnScaleSameSidePath.class);
  }

  @Test
  public void LeftSideCrossLineStopEarlyPath() {
    testClass(LeftSideCrossLineStopEarlyPath.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartFour() {
    testClass(LeftSideSameScaleSecondCubePartFour.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartOne() {
    testClass(LeftSideSameScaleSecondCubePartOne.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartThree() {
    testClass(LeftSideSameScaleSecondCubePartThree.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartTwo() {
    testClass(LeftSideSameScaleSecondCubePartTwo.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubeSwitchPartThree() {
    testClass(LeftSideSameScaleSecondCubeSwitchPartThree.class);
  }

  @Test
  public void LeftSwitchFrontFlickDiffSidePath() {
    testClass(LeftSwitchFrontFlickDiffSidePath.class);
  }

  @Test
  public void LeftSwitchFrontFlickSameSidePath() {
    testClass(LeftSwitchFrontFlickSameSidePath.class);
  }

  @Test
  public void LeftTurnTest() {
    testClass(LeftTurnTest.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndLeftScalePart1() {
    testClass(MidCrossLineOwnLeftSwitchAndLeftScalePart1.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndLeftScalePart2() {
    testClass(MidCrossLineOwnLeftSwitchAndLeftScalePart2.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndLeftScalePart3() {
    testClass(MidCrossLineOwnLeftSwitchAndLeftScalePart3.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndLeftScalePart4() {
    testClass(MidCrossLineOwnLeftSwitchAndLeftScalePart4.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndLeftScalePart5() {
    testClass(MidCrossLineOwnLeftSwitchAndLeftScalePart5.class);
  }

  @Test
  public void MidCrossLineOwnLeftSwitchAndRightScalePart5() {
    testClass(MidCrossLineOwnLeftSwitchAndRightScalePart5.class);
  }

  @Test
  public void MidCrossLineOwnRightSwitchAndRightScalePart1() {
    testClass(MidCrossLineOwnRightSwitchAndRightScalePart1.class);
  }

  @Test
  public void MidCrossLineOwnRightSwitchAndRightScalePart2() {
    testClass(MidCrossLineOwnRightSwitchAndRightScalePart2.class);
  }

  @Test
  public void RightCrossLineOwnScaleDiffSidePath() {
    testClass(RightCrossLineOwnScaleDiffSidePath.class);
  }

  @Test
  public void RightCrossLineOwnScaleSameSidePath() {
    testClass(RightCrossLineOwnScaleSameSidePath.class);
  }

  @Test
  public void RightSideCrossLineStopEarlyPath() {
    testClass(RightSideCrossLineStopEarlyPath.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartFour() {
    testClass(RightSideSameScaleSecondCubePartFour.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartOne() {
    testClass(RightSideSameScaleSecondCubePartOne.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartThree() {
    testClass(RightSideSameScaleSecondCubePartThree.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartTwo() {
    testClass(RightSideSameScaleSecondCubePartTwo.class);
  }

  @Test
  public void RightSideSameScaleSecondCubeSwitchPartThree() {
    testClass(RightSideSameScaleSecondCubeSwitchPartThree.class);
  }

  @Test
  public void RightSwitchFrontFlickDiffSidePath() {
    testClass(RightSwitchFrontFlickDiffSidePath.class);
  }

  @Test
  public void RightSwitchFrontFlickSameSidePath() {
    testClass(RightSwitchFrontFlickSameSidePath.class);
  }

  @Test
  public void RightTurnTest() {
    testClass(RightTurnTest.class);
  }

  @Test
  public void Straight() {
    testClass(Straight.class);
  }

  @Test
  public void LeftScaleBackUpTenInches() {
    testClass(LeftScaleBackUpTenInches.class);
  }

  @Test
  public void LeftSideDiffScaleSecondCubePartOne() {
    testClass(LeftSideDiffScaleSecondCubePartOne.class);
  }

  @Test
  public void LeftSideSameScaleBackUp() {
    testClass(LeftSideSameScaleBackUp.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartOneTest() {
    testClass(LeftSideSameScaleSecondCubePartOneTest.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartThreeTest() {
    testClass(LeftSideSameScaleSecondCubePartThreeTest.class);
  }

  @Test
  public void LeftSideSameScaleSecondCubePartTwoTest() {
    testClass(LeftSideSameScaleSecondCubePartTwoTest.class);
  }

  @Test
  public void LeftSideSameScaleStayOutOfWay() {
    testClass(LeftSideSameScaleStayOutOfWay.class);
  }

  @Test
  public void LeftSideSameScaleStayOutOfWayBackUp() {
    testClass(LeftSideSameScaleStayOutOfWayBackUp.class);
  }

  @Test
  public void PivotTestOne() {
    testClass(PivotTestOne.class);
  }

  @Test
  public void PivotTestTwo() {
    testClass(PivotTestTwo.class);
  }

  @Test
  public void RightScaleBackUpTenInches() {
    testClass(RightScaleBackUpTenInches.class);
  }

  @Test
  public void RightSideDiffScaleSecondCubePartOne() {
    testClass(RightSideDiffScaleSecondCubePartOne.class);
  }

  @Test
  public void RightSideSameScaleBackUp() {
    testClass(RightSideSameScaleBackUp.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartOneTest() {
    testClass(RightSideSameScaleSecondCubePartOneTest.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartTwoTest() {
    testClass(RightSideSameScaleSecondCubePartTwoTest.class);
  }

  @Test
  public void RightSideSameScaleSecondCubePartThreeTest() {
    testClass(RightSideSameScaleSecondCubePartThreeTest.class);
  }

  @Test
  public void RightSideSameScaleStayOutOfWay() {
    testClass(RightSideSameScaleStayOutOfWay.class);
  }

  @Test
  public void RightSideSameScaleStayOutOfWayBackUp() {
    testClass(RightSideSameScaleStayOutOfWayBackUp.class);
  }
}
