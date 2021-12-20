package com.endava.internship.cryptomarket.confservice.integration;

import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

import lombok.SneakyThrows;
import oracle.jdbc.pool.OracleDataSource;

public abstract class IntegrationTest extends DataSourceBasedDBTestCase {

    private final static String FILENAME = "/testData.xml";

    protected final String URL;

    protected final String USERNAME;

    protected final String PASSWORD;

    public IntegrationTest() throws Exception {
        Properties properties = new Properties();
        try (InputStream resource = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(resource);
            URL = properties.getProperty("datasource.url");
            USERNAME = properties.getProperty("datasource.username");
            PASSWORD = properties.getProperty("datasource.password");
        }
        super.setUp();
    }

    protected FlatXmlDataSet getDataSet() throws DataSetException {
        return new FlatXmlDataSetBuilder().build(this.getClass()
                .getResourceAsStream(FILENAME));
    }

    @SneakyThrows
    @Override
    protected DataSource getDataSource() {
        final OracleDataSource oracleDataSource = new OracleDataSource();
        oracleDataSource.setDriverType("oracle.jdbc.driver.OracleDriver");
        oracleDataSource.setURL(URL);
        oracleDataSource.setUser(USERNAME);
        oracleDataSource.setPassword(PASSWORD);
        return oracleDataSource;
    }
}
