package com.amcodeprojects.seek.aips.model;

import java.time.LocalDateTime;

public class CarTotal {

  private LocalDateTime timePeriod;
  private Integer numberOfCars;

  public Integer getNumberOfCars() {
    return numberOfCars;
  }

  public LocalDateTime getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(LocalDateTime timePeriod) {
    this.timePeriod = timePeriod;
  }

  public void setNumberOfCars(Integer numberOfCars) {
    this.numberOfCars = numberOfCars;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CarTotal carTotal = (CarTotal) o;

    if (timePeriod != null ? !timePeriod.equals(carTotal.timePeriod) : carTotal.timePeriod != null) return false;
    return numberOfCars != null ? numberOfCars.equals(carTotal.numberOfCars) : carTotal.numberOfCars == null;
  }

  @Override
  public int hashCode() {
    int result = timePeriod != null ? timePeriod.hashCode() : 0;
    result = 31 * result + (numberOfCars != null ? numberOfCars.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "CarTotal{" +
      "timePeriod=" + timePeriod +
      ", numberOfCars=" + numberOfCars +
      '}';
  }
}
