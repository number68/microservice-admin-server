/**
 * Copyright (C), 2015-2019, 辰森世纪
 * FileName: WebConfig
 * Author:   syc
 * Date:     2019/7/4 18:49
 * Description: WebConfig
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.choice.cloud.monitor;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 〈WebConfig〉<br> 
 * 需要被监控服务设置此选项，解决httptrace由于时间戳带小数导致的浏览器内存crash问题
 * @author syc
 * @date 2019/7/4
 * @since 0.1
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
                // 阻止 jackson converter 将Instant 转换为时间戳
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                break;
            }
        }
    }

}
