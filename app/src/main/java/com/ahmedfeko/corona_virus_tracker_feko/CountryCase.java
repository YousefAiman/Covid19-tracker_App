package com.ahmedfeko.corona_virus_tracker_feko;

public class CountryCase {

    public CountryCase(String country, int cases) {
        this.setCountry(country);
        this.setCases(cases);
    }

    private String country;
    private int cases;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }
}
