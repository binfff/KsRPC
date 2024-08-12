package com.example.rpcclient;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;;
import org.springframework.util.ClassUtils;


public class KsClientsRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
        BeanDefinitionRegistry registry) {
        // 扫描指定包中的所有 @MyFeignClient 接口
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(
            false) {
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                //检查该类是否是独立的（即非内部类）。检查该类是否是注解类型。
                if (beanDefinition.getMetadata().isIndependent() && !beanDefinition.getMetadata()
                    .isAnnotation()) {
                    isCandidate = true;
                }

                return isCandidate;
            }
        };
        scanner.addIncludeFilter(new AnnotationTypeFilter(KsClient.class));

        Set<String> basePackage = getBasePackages(metadata);
        Set<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        for (String s : basePackage) {
            Set<BeanDefinition> components = scanner.findCandidateComponents(s);
            candidateComponents.addAll(components);
        }
        for (BeanDefinition bd : candidateComponents) {
            // 获取接口类名
            String className = bd.getBeanClassName();

            try {
                Class<?> clazz = Class.forName(className);

                // 检查类是否带有 @MyFeignClient 注解
                if (clazz.isInterface() && clazz.isAnnotationPresent(KsClient.class)) {
                    KsClient myFeignClient = clazz.getAnnotation(KsClient.class);

                    // 创建 BeanDefinition 使用 FactoryBean
                    BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
                        KsClientFactoryBean.class);
                    beanDefinitionBuilder.addConstructorArgValue(clazz);
//                    beanDefinitionBuilder.addConstructorArgValue(myFeignClient.serviceName());

                    // 注册 BeanDefinition
                    registry.registerBeanDefinition(clazz.getSimpleName(),
                        beanDefinitionBuilder.getBeanDefinition());
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Set<String> getBasePackages(AnnotationMetadata metadata) {
        Set<String> basePackages = new HashSet<>();

        // 获取 @ComponentScan 注解的包路径
        if (metadata.hasAnnotation(ComponentScan.class.getName())) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(ComponentScan.class.getName())
            );
            if (attributes != null) {
                basePackages.addAll(Arrays.asList(attributes.getStringArray("basePackages")));
            }
        }

        // 获取 @SpringBootApplication 注解的包路径
        if (metadata.hasAnnotation(SpringBootApplication.class.getName())) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                metadata.getAnnotationAttributes(SpringBootApplication.class.getName())
            );
            if (attributes != null) {
                basePackages.addAll(Arrays.asList(attributes.getStringArray("scanBasePackages")));
            }
        }

        // 如果没有配置注解，获取默认包路径
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(metadata.getClassName()));
        }
        return basePackages;
    }
}
