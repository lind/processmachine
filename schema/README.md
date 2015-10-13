# Schemafiles
Uses [gradle-avro-plugin](https://github.com/commercehub-oss/gradle-avro-plugin)

See: [Apache Avro](http://avro.apache.org/)

## Links
* [Schema evolution](http://martin.kleppmann.com/2012/12/05/schema-evolution-in-avro-protocol-buffers-thrift.html)
* [Hadoop: The Definitive Guide (kap 12)](http://shop.oreilly.com/product/0636920033448.do)
* [Schema repo](https://github.com/schema-repo/schema-repo)
* [Schema registry](https://github.com/confluentinc/schema-registry)
* [Documentation tool for Avro schemas](https://github.com/ept/avrodoc)
* Binary/JSON requests example in avro with embedded Jetty: [avro-mobile](https://github.com/flurry/avro-mobile)
    * See: [AvroServer.java](https://github.com/flurry/avro-mobile/blob/master/avro-java-server/src/com/flurry/avroserver/AvroServer.java)

Avro tools for reading binary encoded files:

```
java -jar $AVRO_HOME/avro-tools-*.jar tojson <avrofil>.avro
```
