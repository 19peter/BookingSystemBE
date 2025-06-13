package com.pd.BookingSystem.features.jobRequests.projections;

import java.time.LocalDate;
import java.time.LocalTime;

public interface RequestTimeData {

    LocalDate getServiceDate();

    LocalTime getServiceTime();

    Integer getPlannedDurationInMinutes();
}
