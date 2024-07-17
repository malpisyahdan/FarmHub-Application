package com.project.app.farmhub.common.type;



public enum StatusPayment {
    PENDING("PENDING"),
    PAID("PAID");
   
    private final String statusString;

    StatusPayment(String statusString) {
        this.statusString = statusString;
    }

    public String getStatusString() {
        return statusString;
    }

    public static StatusOrder fromStatusString(String statusString) {
        for (StatusOrder status : StatusOrder.values()) {
            if (status.getStatusString().equalsIgnoreCase(statusString)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status string: " + statusString);
    }
}