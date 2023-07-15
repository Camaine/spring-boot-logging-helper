package org.springlogginghelper;

import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springlogginghelper.config.ConfigDetails;
import org.springlogginghelper.config.LoggingConfiguration;

public class LoggingRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // Register the JSONLogging bean
        GenericBeanDefinition jsonLoggingBeanDefinition = new GenericBeanDefinition();
        jsonLoggingBeanDefinition.setBeanClass(JSONLogging.class);
        registry.registerBeanDefinition("jsonLogging", jsonLoggingBeanDefinition);

        // Register the ConfigDetails bean
        GenericBeanDefinition configDetailsBeanDefinition = new GenericBeanDefinition();
        configDetailsBeanDefinition.setBeanClass(ConfigDetails.class);
        registry.registerBeanDefinition("configDetails", configDetailsBeanDefinition);

        // Register the LoggingConfiguration bean
        GenericBeanDefinition loggingConfigurationBeanDefinition = new GenericBeanDefinition();
        loggingConfigurationBeanDefinition.setBeanClass(LoggingConfiguration.class);
        loggingConfigurationBeanDefinition.getConstructorArgumentValues()
                .addGenericArgumentValue(new RuntimeBeanReference("configDetails"));
        registry.registerBeanDefinition("loggingConfiguration", loggingConfigurationBeanDefinition);

        // Register the LogDetailExtractor bean
        GenericBeanDefinition logDetailExtractorBeanDefinition = new GenericBeanDefinition();
        logDetailExtractorBeanDefinition.setBeanClass(LogDetailExtractor.class);
        logDetailExtractorBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("environment"));
        logDetailExtractorBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("configDetails"));
        registry.registerBeanDefinition("logDetailExtractor", logDetailExtractorBeanDefinition);

        // Register the LoggingAnnotationAdvisor bean
        GenericBeanDefinition loggingAnnotationAdvisorBeanDefinition = new GenericBeanDefinition();
        loggingAnnotationAdvisorBeanDefinition.setBeanClass(LoggingAnnotationAdvisor.class);
        loggingAnnotationAdvisorBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("jsonLogging"));
        loggingAnnotationAdvisorBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(new RuntimeBeanReference("logDetailExtractor"));
        registry.registerBeanDefinition("loggingAnnotationAdvisor", loggingAnnotationAdvisorBeanDefinition);
    }
}

