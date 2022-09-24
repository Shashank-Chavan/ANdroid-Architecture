package com.example.arcitecture_components;

public class Countries {
    private String name;
    private String code;
    Countries(String name,String code){
        this.name = name;
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
