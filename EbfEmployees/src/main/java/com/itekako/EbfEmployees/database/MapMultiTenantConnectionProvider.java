package com.itekako.EbfEmployees.database;

import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class MapMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    private Map<String, ConnectionProvider> connectionProviderMap
            = new HashMap<>();

    public MapMultiTenantConnectionProvider() throws IOException {
        initConnectionProviderForTenant("user");
        initConnectionProviderForTenant("admin");
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProviderMap.values()
                .iterator()
                .next();
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String s) {
        return connectionProviderMap.get(s);
    }
    private void initConnectionProviderForTenant(String tenantId)
            throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(
                String.format("/hibernate-database-%s.properties", tenantId)));
        DriverManagerConnectionProviderImpl connectionProvider
                = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure(properties);
        this.connectionProviderMap.put(tenantId, connectionProvider);
    }
}
