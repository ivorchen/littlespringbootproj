package com.genesys.gir.qa.testcopystream.Models;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Configuration
@EnableConfigurationProperties(WebDavConfigComponent.class)
public class WebDavSetup {

    private Logger logger=LogManager.getLogger(WebDavSetup.class);

    @Autowired
    private WebDavConfigComponent webDavConfigComponent;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WebDAVClient webDAVClient() {
        int maxPerRouteConnection=webDavConfigComponent.getMaxPerRouteConnection();
        int maxTotalConnection=webDavConfigComponent.getMaxTotalConnection();
        return new WebDAVClient(
                newPoolingHttpClientConnectionManager(
                        maxPerRouteConnection,
                        maxTotalConnection),
                        webDavConfigComponent.getSocketTimeout());
    }

    private static PoolingHttpClientConnectionManager newPoolingHttpClientConnectionManager( final int maxPerRouteConnection,
            final int maxTotalConnection)
    {
        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .build();

        final PoolingHttpClientConnectionManager poolHttpConnManager = new PoolingHttpClientConnectionManager(registry);

        if (maxPerRouteConnection > 0)
        {
            poolHttpConnManager.setDefaultMaxPerRoute(maxPerRouteConnection);
        }
        if (maxTotalConnection > 0)
        {
            poolHttpConnManager.setMaxTotal(maxTotalConnection);
        }

        return poolHttpConnManager;
    }
}
