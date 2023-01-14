/**
 * Copyright 2020-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.binghe.rpc.consumer.spring;

import io.binghe.rpc.annotation.RpcReference;
import io.binghe.rpc.constants.RpcConstants;
import io.binghe.rpc.consumer.spring.context.RpcConsumerSpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author binghe(公众号：冰河技术)
 * @version 1.0.0
 * @description RpcConsumerPostProcessor
 */
@Component
public class RpcConsumerPostProcessor implements ApplicationContextAware, BeanClassLoaderAware, BeanFactoryPostProcessor {
    private final Logger logger = LoggerFactory.getLogger(RpcConsumerPostProcessor.class);
    private ApplicationContext context;
    private ClassLoader classLoader;

    private final Map<String, BeanDefinition> rpcRefBeanDefinitions = new LinkedHashMap<>();

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
        RpcConsumerSpringContext.getInstance().setContext(applicationContext);
    }
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (beanClassName != null) {
                Class<?> clazz = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                ReflectionUtils.doWithFields(clazz, this::parseRpcReference);
            }
        }

        BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
        this.rpcRefBeanDefinitions.forEach((beanName, beanDefinition) -> {
            if (context.containsBean(beanName)) {
                throw new IllegalArgumentException("spring context already has a bean named " + beanName);
            }
            registry.registerBeanDefinition(beanName, rpcRefBeanDefinitions.get(beanName));
            logger.info("registered RpcReferenceBean {} success.", beanName);
        });
    }

    private void parseRpcReference(Field field) {
        RpcReference annotation = AnnotationUtils.getAnnotation(field, RpcReference.class);
        if (annotation != null) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(RpcReferenceBean.class);
            builder.setInitMethodName(RpcConstants.INIT_METHOD_NAME);
            builder.addPropertyValue("interfaceClass", field.getType());
            builder.addPropertyValue("version", annotation.version());
            builder.addPropertyValue("registryType", annotation.registryType());
            builder.addPropertyValue("registryAddress", annotation.registryAddress());
            builder.addPropertyValue("loadBalanceType", annotation.loadBalanceType());
            builder.addPropertyValue("serializationType", annotation.serializationType());
            builder.addPropertyValue("timeout", annotation.timeout());
            builder.addPropertyValue("async", annotation.async());
            builder.addPropertyValue("oneway", annotation.oneway());
            builder.addPropertyValue("proxy", annotation.proxy());
            builder.addPropertyValue("group", annotation.group());
            builder.addPropertyValue("scanNotActiveChannelInterval", annotation.scanNotActiveChannelInterval());
            builder.addPropertyValue("heartbeatInterval", annotation.heartbeatInterval());
            builder.addPropertyValue("retryInterval", annotation.retryInterval());
            builder.addPropertyValue("retryTimes", annotation.retryTimes());
            builder.addPropertyValue("enableResultCache", annotation.enableResultCache());
            builder.addPropertyValue("resultCacheExpire", annotation.resultCacheExpire());
            builder.addPropertyValue("enableDirectServer", annotation.enableDirectServer());
            builder.addPropertyValue("directServerUrl", annotation.directServerUrl());
            builder.addPropertyValue("enableDelayConnection", annotation.enableDelayConnection());
            builder.addPropertyValue("corePoolSize", annotation.corePoolSize());
            builder.addPropertyValue("maximumPoolSize", annotation.maximumPoolSize());

            BeanDefinition beanDefinition = builder.getBeanDefinition();
            rpcRefBeanDefinitions.put(field.getName(), beanDefinition);
        }
    }
}
