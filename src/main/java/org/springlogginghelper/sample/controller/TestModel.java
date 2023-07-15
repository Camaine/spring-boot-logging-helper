package org.springlogginghelper.sample.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TestModel {
    private String id;
    private String name;
    private String description;
}
