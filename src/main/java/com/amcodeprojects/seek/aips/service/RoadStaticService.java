package com.amcodeprojects.seek.aips.service;

import com.amcodeprojects.seek.aips.model.CarTotal;
import com.amcodeprojects.seek.aips.service.analyse.StatisticAnalyserService;
import com.amcodeprojects.seek.aips.service.file.FileService;
import com.amcodeprojects.seek.aips.service.parse.InputParserService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class RoadStaticService {

  private FileService fileService;
  private InputParserService inputParserService;
  private StatisticAnalyserService statisticAnalyserService;
  private DateTimeFormatter outputDateTimeFormatter;

  private static final String TOTAL_CARS_MESSAGE = "Total Number of Cars: %d";
  private static final String TOTAL_CARS_PER_DAY_MESSAGE = "Total Number of Cars per Day:";
  private static final String THREE_BUSIEST_PERIODS_MESSAGE = "Three Busiest Periods:";
  private static final String LEAST_BUSY_NINETY_MINUTE_PERIOD_MESSAGE = "Least Busy Ninety Minute Period: %s %d";
  private static final String CAR_TOTAL_FORMAT = "%s %d";
  private static final DateTimeFormatter TOTAL_CARS_PER_DAY_DATE_FORMAT =
    DateTimeFormatter.ofPattern("yyyy-mm-dd");

  public RoadStaticService(FileService fileService, InputParserService inputParserService,
                           StatisticAnalyserService statisticAnalyserService, DateTimeFormatter outputDateTimeFormatter) {
    this.fileService = fileService;
    this.inputParserService = inputParserService;
    this.statisticAnalyserService = statisticAnalyserService;
    this.outputDateTimeFormatter = outputDateTimeFormatter;
  }

  public void printStatistics(String inputFileName) {
      List<CarTotal> carTotalList = fileService.readRawFile(inputFileName)
        .map(inputParserService::parseInputString)
        .collect(Collectors.toList());

      printTotalNumberOfCars(carTotalList);
      printTotalNumberOfCarsPerDay(carTotalList);
      printThreeBusiestPeriods(carTotalList);
      printNinetyMinutePeriodWithFewestCars(carTotalList);
  }

  private void printTotalNumberOfCars(List<CarTotal> carTotalList) {
    print(String.format(TOTAL_CARS_MESSAGE,statisticAnalyserService.getTotalNumberOfCars(carTotalList)));
  }

  private void printTotalNumberOfCarsPerDay(List<CarTotal> carTotalList) {
    print(TOTAL_CARS_PER_DAY_MESSAGE);
    statisticAnalyserService.getTotalsPerDay(carTotalList)
      .stream()
      .map(total -> String.format(CAR_TOTAL_FORMAT,
        TOTAL_CARS_PER_DAY_DATE_FORMAT.format(total.getTimePeriod()), total.getNumberOfCars()))
      .forEach(this::print);
  }

  private void printThreeBusiestPeriods(List<CarTotal> carTotalList) {
    print(THREE_BUSIEST_PERIODS_MESSAGE);

    statisticAnalyserService.getBusiestThreeTimePeriods(carTotalList)
      .stream()
      .map(total -> String.format(CAR_TOTAL_FORMAT,
        outputDateTimeFormatter.format(total.getTimePeriod()), total.getNumberOfCars()))
      .forEach(this::print);
  }

  private void printNinetyMinutePeriodWithFewestCars(List<CarTotal> carTotalList) {
    CarTotal total = statisticAnalyserService.getRollingPeriodWithFewestCars(carTotalList);
    print(String.format(LEAST_BUSY_NINETY_MINUTE_PERIOD_MESSAGE,
      outputDateTimeFormatter.format(total.getTimePeriod()), total.getNumberOfCars()));
  }

  private void print(String message) {
    System.out.println(message);
  }
}
