package com.security.jdbc.pojos;

public class Authorities {


    private String authority;

    public Authorities(){}

    public Authorities(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "Authorities{" +
                "authority='" + authority + '\'' +
                '}';
    }
}
