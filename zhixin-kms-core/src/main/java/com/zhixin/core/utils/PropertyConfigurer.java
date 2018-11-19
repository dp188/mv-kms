package com.zhixin.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * 属性配置工具
 * Created by user on 2015/8/18.
 */
public class PropertyConfigurer extends PropertyPlaceholderConfigurer {

    private Properties props;

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        this.props = props;

        /*for(Object key: props.keySet()) {
            System.out.println(key + "=" + props.get(key));
        }*/
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public Object setProperty(String key, String value) {
        return props.setProperty(key, value);
    }

}
