# consul-playground
Playing around with [Consul](https://www.consul.io/) and [Ecwid's Java API for Consul](https://github.com/Ecwid/consul-api).

To successfully run the tests, first start the Consul agent (according to the [Getting started section at consul.io](https://www.consul.io/intro/getting-started/install.html)) by running:

```
consul agent -dev
```

You can then run the tests themselves by using:

```
mvn test
```