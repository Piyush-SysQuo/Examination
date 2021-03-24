package com.inspection.examination.model;

public class StudentAvailabilityBody {

    private int pages;
    private int limit;


    public StudentAvailabilityBody(int pages, int limit) {
        this.pages = pages;
        this.limit = limit;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
