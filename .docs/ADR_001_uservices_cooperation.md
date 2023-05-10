Context: We are going to have multiple uservices, tested locally and deployed later on to k8s

Decision: We decided to go with Entity DAPR.

Solutions:
- DAPR: allows easily run the whole stack on developer machine, no language specyfic. pros: proto serialization, shifting complexity from codebase to configuration (e.g. event brokers), code language independence
- Spring Cloud stack: widely used, java-based

Reason: Subjective decision

Consequences: New tools to learn, as DAPR is not widely used by organizations
