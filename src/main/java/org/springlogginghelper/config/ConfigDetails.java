package org.springlogginghelper.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ConfigDetails {
    private List<String> excludeRequestAttributes = new ArrayList<>();
    private List<String> excludeResponseAttributes = new ArrayList<>();
    private List<String> excludeExceptionAttributes = new ArrayList<>();
    private List<String> excludeRequestRegex = new ArrayList<>();
    private List<String> includeHeaderAttributes = new ArrayList<>();
}
