package com.basic.myspringboot;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

@Builder
@Getter
@ToString
public class CustomVO {
    private String mode;
    private double rate;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
