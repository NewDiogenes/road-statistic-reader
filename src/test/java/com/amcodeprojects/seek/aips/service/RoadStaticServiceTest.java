package com.amcodeprojects.seek.aips.service;

import com.amcodeprojects.seek.aips.model.CarTotal;
import com.amcodeprojects.seek.aips.service.analyse.StatisticAnalyserService;
import com.amcodeprojects.seek.aips.service.file.FileService;
import com.amcodeprojects.seek.aips.service.parse.InputParserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RoadStaticServiceTest {

  private RoadStaticService roadStaticService;

  @Mock
  private FileService fileService;
  @Mock
  private InputParserService inputParserService;
  @Mock
  private StatisticAnalyserService statisticAnalyserService;
  private DateTimeFormatter formatter = DateTimeFormatter.BASIC_ISO_DATE;

  private String testFile = "testFile";
  private List<String> testInputList;

  private CarTotal totalOne;
  private CarTotal totalTwo;

  @Before
  public void setup() {
    initMocks(this);
    testInputList = Arrays.asList("statisticOne", "statisticTwo");
    totalOne = getTotal();
    totalTwo = getTotal();

    when(fileService.readRawFile(eq(testFile))).thenReturn(testInputList.stream());

    when(inputParserService.parseInputString(eq(testInputList.get(0))))
      .thenReturn(totalOne);
    when(inputParserService.parseInputString(eq(testInputList.get(1))))
      .thenReturn(totalTwo);

    when(statisticAnalyserService.getTotalNumberOfCars(any()))
      .thenReturn(0);
    when(statisticAnalyserService.getTotalsPerDay(any()))
      .thenReturn(Arrays.asList(getTotal()));
    when(statisticAnalyserService.getBusiestThreeTimePeriods(any()))
      .thenReturn(Arrays.asList(getTotal(), getTotal(), getTotal()));

    when(statisticAnalyserService.getRollingPeriodWithFewestCars(any()))
      .thenReturn(getTotal());

    roadStaticService = new RoadStaticService(fileService, inputParserService, statisticAnalyserService, formatter);
  }

  @Test
  public void printStatistics_shouldPassTheFileNameToTheFileServiceToRead() throws IOException {
    roadStaticService.printStatistics(testFile);

    verify(fileService).readRawFile(eq(testFile));
  }

  @Test
  public void givenFileServiceReturnsANonEmptyStream_printStatistics_shouldPassEachInputToInputParser() {
    roadStaticService.printStatistics(testFile);

    testInputList.forEach(in -> verify(inputParserService).parseInputString(eq(in)));
  }

  @Test
  public void givenInputParserReturnsValidCarTotals_printStatistics_shouldGetTheTotalNumberOfCars() {
    roadStaticService.printStatistics(testFile);

    verify(statisticAnalyserService).getTotalNumberOfCars(eq(Arrays.asList(totalOne, totalTwo)));
  }

  @Test
  public void givenInputParserReturnsValidCarTotals_printStatistics_shouldGetTheTotalNumberOfCarsPerDay() {
    roadStaticService.printStatistics(testFile);

    verify(statisticAnalyserService).getTotalsPerDay(eq(Arrays.asList(totalOne, totalTwo)));
  }

  @Test
  public void givenInputParserReturnsValidCarTotals_printStatistics_shouldGetTheThreeBusiestPeriods() {
    roadStaticService.printStatistics(testFile);

    verify(statisticAnalyserService).getBusiestThreeTimePeriods(eq(Arrays.asList(totalOne, totalTwo)));
  }

  @Test
  public void givenInputParserReturnsValidCarTotals_printStatistics_shouldGetTheNinetyMinutePeriodWithTheLeastCars() {
    roadStaticService.printStatistics(testFile);

    verify(statisticAnalyserService).getRollingPeriodWithFewestCars(eq(Arrays.asList(totalOne, totalTwo)));
  }

  private CarTotal getTotal() {
    CarTotal total = new CarTotal();
    total.setTimePeriod(LocalDateTime.now());
    total.setNumberOfCars(1);
    return total;
  }
}