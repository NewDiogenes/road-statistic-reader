package com.amcodeprojects.seek.aips.service.parse;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class InputParserServiceTest {

  private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
  private final String testDateTime = "2016-12-01T05:00:00";
  private final Integer testNumberOfCars = 5;
  private final String testString = String.format("%s %d", testDateTime, testNumberOfCars);
  private InputParserService inputParserService;

  @Before
  public void setup() {
    inputParserService = new InputParserService(dateTimeFormatter);
  }

  @Test
  public void givenAStringWIthAValidISODate_parseInputString_shouldReturnACarTotalWIthTheParsedLocalDateTime() {
    assertEquals(LocalDateTime.parse(testDateTime, dateTimeFormatter),
      inputParserService.parseInputString(testString).getTimePeriod());
  }

  @Test
  public void givenAStringWIthAIntergerCount_parseInputString_shouldReturnACarTotalWIthTheParsedNumberOfCars() {
    assertEquals(testNumberOfCars,
      inputParserService.parseInputString(testString).getNumberOfCars());
  }

}