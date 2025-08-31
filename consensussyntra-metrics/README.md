# ConsensusSyntra metrics with Micrometer integration

Add the following dependency to the classpath for publishing ConsensusSyntra metrics
to external monitoring systems via
<a href="https://micrometer.io/" target="_blank">Micrometer</a>.

Gradle (version catalog)

```toml
[versions]
consensussyntra-metrics = "0.9"

[libraries]
consensussyntra-metrics = { module = "io.consensussyntra:consensussyntra-metrics", version.ref = "consensussyntra-metrics" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.consensussyntra:consensussyntra-metrics:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.consensussyntra</groupId>
    <artifactId>consensussyntra-metrics</artifactId>
    <version>0.9</version>
</dependency>
```

<a href="https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-metrics/src/main/java/io/consensussyntra/metrics/RaftNodeMetrics.java" target="_blank">
`RaftNodeMetrics`</a>
implements the
<a href="https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra/src/main/java/io/consensussyntra/report/RaftNodeReportListener.java" target="_blank">
`RaftNodeReportListener`</a>
interface and can be injected into created `RaftNode` instances via
`RaftNodeBuilder.setRaftNodeReportListener()`. Then, several metrics extracted
from published `RaftNodeReport` objects are passed to meter registries. 
