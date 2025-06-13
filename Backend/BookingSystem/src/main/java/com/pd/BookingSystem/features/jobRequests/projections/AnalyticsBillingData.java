package com.pd.BookingSystem.features.jobRequests.projections;


public interface AnalyticsBillingData {
    Long getCount();
    Long getPlannedDurationInMinutes();
    Long getActualDurationInMinutes();
    Double getTotalPrice();
}
