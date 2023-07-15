package org.springlogginghelper;

import org.springframework.context.annotation.Import;
import org.springlogginghelper.config.FilterConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({LoggingRegister.class, FilterConfiguration.class})
public @interface EnableLogging {
}
