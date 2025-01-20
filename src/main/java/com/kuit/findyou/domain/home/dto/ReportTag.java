package com.kuit.findyou.domain.home.dto;


public enum ReportTag {
    PROTECTING("보호중"), MISSING("실종신고"), WITNESSED("목격신고");

    private final String value;

    ReportTag(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
