package com.amcodeprojects.seek.aips.service.analyse;

import com.amcodeprojects.seek.aips.model.CarTotal;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StatisticAnalyserServiceTest {

  private StatisticAnalyserService statisticAnalyserService;
  private List<CarTotal> testList;
  private String testDataFileName = "src/test/resources/testInputData.txt";

  @Before
  public void setup() throws IOException {
    statisticAnalyserService = new StatisticAnalyserService();

    testList = Files.lines(Paths.get(testDataFileName))
    .map(line -> {
      String[] inputValues = line.split(" ");

      CarTotal carTotal = new CarTotal();

      carTotal.setTimePeriod(LocalDateTime.parse(inputValues[0], DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      carTotal.setNumberOfCars(Integer.parseInt(inputValues[1]));
      return carTotal;
    }).collect(Collectors.toList());
  }

  @Test
  public void givenAListOfCarTotals_getTotalNumberOfCars_shouldReturnTheSUmOfAllCarTotals() {
    Integer expectedSum = 398;

    assertEquals(expectedSum, statisticAnalyserService.getTotalNumberOfCars(testList));
  }

  @Test
  public void givenAListOfCarTotals_getCarTotalPerDay_shouldReturnTheCarTotalsSummedFOrEachDay() {
    CarTotal expectedTotalOne = getTestTotal("2016-12-01T00:00", 179);
    CarTotal expectedTotalTwo = getTestTotal("2016-12-05T00:00", 81);
    CarTotal expectedTotalThree = getTestTotal("2016-12-08T00:00", 134);
    CarTotal expectedTotalFour = getTestTotal("2016-12-09T00:00", 4);

    List<CarTotal> expectedTotals =
      Arrays.asList(expectedTotalOne, expectedTotalTwo, expectedTotalThree, expectedTotalFour);

    assertEquals(expectedTotals, statisticAnalyserService.getTotalsPerDay(testList));
  }

  @Test
  public void givenAListOfCarTotals_getBusiestThreeTimePeriods_shouldReturnAListWIthTheThreeBusiestPeriods() {
    CarTotal expectedTotalOne = getTestTotal("2016-12-01T07:30:00", 46);
    CarTotal expectedTotalTwo = getTestTotal("2016-12-01T08:00:00", 42);
    CarTotal expectedTotalThree = getTestTotal("2016-12-08T18:00:00", 33);

    List<CarTotal> expectedTotals =
      Arrays.asList(expectedTotalOne, expectedTotalTwo, expectedTotalThree);

    assertEquals(expectedTotals, statisticAnalyserService.getBusiestThreeTimePeriods(testList));
  }

  @Test
  public void givenAListOfCarTotals_getRollingPeriodWithFewestCars_shouldReturnTheNinetyMinutePeriodWithTheFewestCars() {
    CarTotal expectedTotal = getTestTotal("2016-12-01T07:00", 113);

    assertEquals(expectedTotal, statisticAnalyserService.getRollingPeriodWithFewestCars(testList));
  }

  private CarTotal getTestTotal(String dateTime, Integer carNum) {
    CarTotal carTotal = new CarTotal();
    carTotal.setTimePeriod(LocalDateTime.parse(dateTime));
    carTotal.setNumberOfCars(carNum);
    return carTotal;
  }
}