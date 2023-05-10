Context: Data serialization of events may be textual or binary

Decision: We decided to go with textual (JSON) representation

Solutions:
- JSON: use JSON serialization, as we may share such contracts using Avro schema
- PROTO: reuse protobuf experience in similar way as we already have gRpc/PROTO in DAPR

Reason: JSON is much more easier for debug when we would like to spy messages in brokers. Also we would like to be familiar with Avro as it is used in e.g. Kafka

Consequences: Changing serialization later on will no may be wasy as at some point it may provide to mixed serialization mechanisms

Notes:
- [Serialization in DAP should be customised](https://github.com/dapr/java-sdk/issues/496) so we have our ser/deser  avro-native approach
- [Avro bs Proto](https://dataforgeeks.com/data-serialisation-avro-vs-protocol-buffers/2015/)
