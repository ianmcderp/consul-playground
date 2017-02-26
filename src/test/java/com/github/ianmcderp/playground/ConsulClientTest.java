package com.github.ianmcderp.playground;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Matthias Riedl (ianmcderp) on 26.02.2017.
 */
public class ConsulClientTest {
    private ConsulClient consulClient;

    @Before
    public void before() {
        consulClient = new ConsulClient("localhost");
    }

    @Test
    public void testKV() {
        // Given
        consulClient.setKVValue("com.my.app.foo", "foo");
        consulClient.setKVValue("com.my.app.bar", "bar");
        consulClient.setKVValue("com.your.app.foo", "hello");
        consulClient.setKVValue("com.your.app.bar", "world");

        // When
        Response<GetValue> keyValueResponse = consulClient.getKVValue("com.my.app.foo");
        System.out.println("single KV: " + keyValueResponse.getValue().getKey() + ": " + keyValueResponse.getValue().getDecodedValue());

        List<GetValue> myValues = consulClient.getKVValues("com.my").getValue();
        List<GetValue> yourValues = consulClient.getKVValues("com.your").getValue();

        // Then
        myValues.forEach(value -> System.out.println("mine: " + value.getKey() + " " + value.getDecodedValue()));
        yourValues.forEach(value -> System.out.println("yours: " + value.getKey() + " " + value.getDecodedValue()));
    }

    @Test
    public void testServices() {
        // Given
        NewService pizzaService1 = new NewService();
        pizzaService1.setId("com.github.ianmcderp.pizza-order-service.1");
        pizzaService1.setName("PizzaService");
        pizzaService1.setPort(8080);
        pizzaService1.setTags(Collections.singletonList("N-EU"));
        consulClient.agentServiceRegister(pizzaService1);

        NewService pizzaService2 = new NewService();
        pizzaService2.setId("com.github.ianmcderp.pizza-order-service.2");
        pizzaService2.setName("PizzaService");
        pizzaService2.setPort(8080);
        pizzaService2.setTags(Arrays.asList("US", "ASIA"));
        consulClient.agentServiceRegister(pizzaService2);

        // When
        Response<List<HealthService>> queriedServices = consulClient.getHealthServices("PizzaService", true, QueryParams.DEFAULT);

        // Then
        queriedServices.getValue()
                .stream()
                .map(HealthService::getService)
                .forEach(service -> System.out.println(service.toString()));
    }
}
