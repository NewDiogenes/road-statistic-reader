package com.amcodeprojects.seek.aips.service.analyse;

import com.amcodeprojects.seek.aips.model.CarTotal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticAnalyserService {

  private final Integer timePeriodMinutesForLeastCars = 90;

  public Integer getTotalNumberOfCars(List<CarTotal> carTotalList) {
    return carTotalList
      .stream()
      .mapToInt(CarTotal::getNumberOfCars)
      .sum();
  }

  public List<CarTotal> getTotalsPerDay(List<CarTotal> carTotalList) {
    return carTotalList.stream()
      .collect(Collectors.groupingBy(ct -> ct.getTimePeriod().toLocalDate()))
      .entrySet()
      .stream()
      .map(this::getCarTotalPerDay)
      .sorted(Comparator.comparing(CarTotal::getTimePeriod))
      .collect(Collectors.toList());
  }

  public List<CarTotal> getBusiestThreeTimePeriods(List<CarTotal> carTotals) {
    carTotals.sort(Comparator.comparingInt(CarTotal::getNumberOfCars)
    .reversed());
    return carTotals.subList(0, 3);
  }

  public CarTotal getRollingPeriodWithFewestCars(List<CarTotal> carTotals) {
    return carTotals.stream()
      .map(CarTotal::getTimePeriod)
      .map(mapToNinetyMinuteTimePeriod(carTotals))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .sorted(Comparator.comparing(CarTotal::getNumberOfCars))
      .findFirst()
      .orElseThrow(() -> new RuntimeException("Unknown Error"));
  }

  private Function<LocalDateTime, Optional<CarTotal>> mapToNinetyMinuteTimePeriod(List<CarTotal> carTotalList) {
    return startTime -> {
      List<CarTotal> cars = carTotalList.stream()
        .filter(carTotal -> carTotal.getTimePeriod().compareTo(startTime) >= 0)
        .filter(carTotal -> carTotal.getTimePeriod()
          .compareTo(startTime.plusMinutes(timePeriodMinutesForLeastCars)) <= 0)
        .collect(Collectors.toList());

      return getNinetyMinuteCarTotalIfExists(startTime, cars);
    };
  }

  private Optional<CarTotal> getNinetyMinuteCarTotalIfExists(LocalDateTime startTime, List<CarTotal> cars) {
    if (cars.size() == 3) {
      CarTotal carTotal = new CarTotal();
      carTotal.setTimePeriod(startTime);
      carTotal.setNumberOfCars(cars.stream().mapToInt(CarTotal::getNumberOfCars).sum());
      return Optional.of(carTotal);
    } else {
      return Optional.empty();
    }
  }

  private CarTotal getCarTotalPerDay(Map.Entry<LocalDate, List<CarTotal>> entry) {
    CarTotal carTotal = new CarTotal();
    carTotal.setTimePeriod(entry.getKey().atStartOfDay());
    carTotal.setNumberOfCars(entry.getValue().stream().mapToInt(CarTotal::getNumberOfCars).sum());
    return carTotal;
  }
}
