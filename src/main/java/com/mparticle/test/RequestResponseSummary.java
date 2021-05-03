package com.mparticle.test;

public class RequestResponseSummary {
    public long start;
    public long duration;
    public int status;
    public boolean isSuccessful;

    public static RequestResponseSummary create(long start, long duration, int status) {
        RequestResponseSummary summary = new RequestResponseSummary();

        summary.start = start;
        summary.duration = duration;
        summary.status = status;
        summary.isSuccessful = status < 400;

        return summary;
    }
}
