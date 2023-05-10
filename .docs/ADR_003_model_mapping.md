Context: Minimalize number of manually created mappings

Decision: We decided to go with ModelMapper

Solutions:
- ModelMapper
- MapStruct

Reason: ModelMapper is widely used, it has integration with [Vavr](https://github.com/vavr-io/vavr-jackson) models and [Proto](https://github.com/HubSpot/jackson-datatype-protobuf)

Consequences: As we use also Jackson annotations, it will be only Jackson-compatible

Notes:
- Nothing
