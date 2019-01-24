package com.prakass.products.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ConfigValuesTest {

    @Autowired
    private ConfigValues configValues;

    @Test
    public void shouldGetProductServiceBaseUrl() throws Exception {
        assertThat(configValues.getProductServiceBaseUrl(), is("http://api.example.com/vx"));
    }

    @Test
    public void shouldGetProductServiceKey() throws Exception {
        assertThat(configValues.getProductServiceKey(), is("keyValue"));
    }

    @Test
    public void shouldGetProductCategoryIncluded() throws Exception {
        assertThat(configValues.getProductCategoryIncluded(), is("categoryId"));
    }
}
