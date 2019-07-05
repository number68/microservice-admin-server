package com.choice.cloud.monitor;

import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.bull.javamelody.CollectorServlet;
import net.bull.javamelody.internal.common.Parameters;

// Always run, if you depend on this module, you will get this autoconfiguration
@Configuration
@EnableConfigurationProperties(JavaMelodyConfigurationProperties.class)
@ConditionalOnProperty(prefix = JavaMelodyConfigurationProperties.PREFIX, name = "collectserver.enabled",
    matchIfMissing = true)
public class JavaMelodyAutoConfiguration {
    private static final String CONTEXT_ROOT = "/javamelody";

    @Bean
    public ServletRegistrationBean collectorServletBean(JavaMelodyConfigurationProperties properties,
        ServletContext servletContext) {
        // give parameters from application.properties/yml to javamelody
        for (final Map.Entry<String, String> entry : properties.getInitParameters().entrySet()) {
            servletContext.setAttribute(Parameters.PARAMETER_SYSTEM_PREFIX + entry.getKey(), entry.getValue());
        }

        final CollectorServlet servlet = new CollectorServlet();
        final ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet);
        // setLoadOnStartup otherwise CollectorServlet.init(ServletConfig) is not called
        // before the first request
        servletRegistrationBean.setLoadOnStartup(1);
        servletRegistrationBean.addUrlMappings(CONTEXT_ROOT);

        return servletRegistrationBean;
    }
}
