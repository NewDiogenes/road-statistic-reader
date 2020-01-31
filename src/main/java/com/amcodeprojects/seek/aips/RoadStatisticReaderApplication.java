package com.amcodeprojects.seek.aips;

import com.amcodeprojects.seek.aips.service.RoadStaticService;
import com.amcodeprojects.seek.aips.service.analyse.StatisticAnalyserService;
import com.amcodeprojects.seek.aips.service.file.FileService;
import com.amcodeprojects.seek.aips.service.parse.InputParserService;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class RoadStatisticReaderApplication {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  public static void main(String[] args) throws IOException {

    FileService fileService = new FileService();
    InputParserService inputParserService = new InputParserService(formatter);
    StatisticAnalyserService statisticAnalyserService = new StatisticAnalyserService();
    RoadStaticService roadStaticService =
      new RoadStaticService(fileService, inputParserService, statisticAnalyserService, formatter);

    roadStaticService.printStatistics(args[0]);
  }
}
