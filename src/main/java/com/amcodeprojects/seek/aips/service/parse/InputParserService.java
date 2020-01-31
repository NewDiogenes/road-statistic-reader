package com.amcodeprojects.seek.aips.service.parse;

import com.amcodeprojects.seek.aips.model.CarTotal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InputParserService {

  private final DateTimeFormatter dateFormatter;

  public InputParserService(DateTimeFormatter dateFormatter){
    this.dateFormatter = dateFormatter;
  }

  public CarTotal parseInputString(String inputString) {

    String[] inputValues = inputString.split(" ");

    CarTotal carTotal = new CarTotal();

    carTotal.setTimePeriod(LocalDateTime.parse(inputValues[0], dateFormatter));
    carTotal.setNumberOfCars(Integer.parseInt(inputValues[1]));
    return carTotal;
  }
}
