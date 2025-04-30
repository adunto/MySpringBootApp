package com.basic.myspringboot.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter @Setter
@Component
@ConfigurationProperties("myboot")
public class MyBootProperties {
    private String name;
    private String age;
    private String fullName;
}
