package com.pd.BookingSystem.features.jobRequests.enums;

public enum RequestStatusEnum {
    PENDING("Pending"), //No employee
    IN_PROGRESS("In Progress"), // Employee assigned, requires approval
    APPROVED("Approved"),   // Employee assigned, approved by Admin
    COMPLETED("Completed"),
    CANCELLED("Cancelled"), // Client cancelled the request
    REJECTED("Rejected"),
    EXPIRED("Expired"),

//    ON_HOLD("On Hold"),
//    RESCHEDULED("Rescheduled"),
//    INVOICED("Invoice"),
//    PAID("Paid"),
//    REFUNDED("Refunded"),
//    DISPUTED("Disputed"),
//    CLOSED("Closed"),

    ;

    private final String status;

    RequestStatusEnum(String status) {
        this.status = status;
    }
}
