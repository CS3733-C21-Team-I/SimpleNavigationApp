package edu.wpi.cs3733.c21.teamI.parking.reservations;

import edu.wpi.cs3733.c21.teamI.database.ParkingPeripheralServerManager;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ParkingSlip {

  public static Map<Integer, Double> pricingMap;

  private static final double OVERCHARGE_PER_5_MIN = .50;

  private static final double PENALTY_UNCALCULATED = -1;

  static {
    pricingMap = new HashMap<>();
    pricingMap.put(1, 3.00);
    pricingMap.put(60, 5.00);
    pricingMap.put(120, 5.50);
    pricingMap.put(180, 6.00);
    pricingMap.put(300, 7.00);
    pricingMap.put(540, 8.00);
  }

  private int id;
  private ParkingReservation reservation;
  private Timestamp entryTimestamp;
  private int durationMin;
  private double baseCost;
  private double penalty;

  public ParkingSlip(
      int id,
      ParkingReservation reservation,
      Timestamp entryTimestamp,
      int durationMin,
      double baseCost) {
    this.id = id;
    this.reservation = reservation;
    this.entryTimestamp = entryTimestamp;
    this.durationMin = durationMin;
    this.baseCost = baseCost;
    this.penalty = PENALTY_UNCALCULATED;
  }

  public void calculatePenalty(Timestamp currentTime) {
    int minOver =
        (int)
            ((currentTime.getTime()
                    - (entryTimestamp.getTime() + TimeUnit.MINUTES.toMillis(durationMin)))
                / 1000L
                / 60L);
    if (minOver <= 0) penalty = 0;
    else {
      penalty = ((minOver / 5) + 1) * OVERCHARGE_PER_5_MIN;
      ParkingPeripheralServerManager.getInstance().updateSlipPenalty(id, penalty);
    }
  }

  public int getId() {
    return id;
  }

  public ParkingReservation getReservation() {
    return reservation;
  }

  public Timestamp getEntryTimestamp() {
    return entryTimestamp;
  }

  public Timestamp getEndTimestamp() {
    return new Timestamp(entryTimestamp.getTime() + TimeUnit.MINUTES.toMillis(durationMin));
  }

  public int getDurationMin() {
    return durationMin;
  }

  public double getBaseCost() {
    return baseCost;
  }

  public double getPenalty() {
    return penalty;
  }
}
