# ConsensusSyntra YAML Config Parser

Gradle (version catalog)

```toml
[versions]
consensussyntra-yaml = "0.9"

[libraries]
consensussyntra-yaml = { module = "io.consensussyntra:consensussyntra-yaml", version.ref = "consensussyntra-yaml" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.consensussyntra:consensussyntra-yaml:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.consensussyntra</groupId>
    <artifactId>consensussyntra-yaml</artifactId>
    <version>0.9</version>
</dependency>
```

This project enables you to create `RaftConfig` objects from YAML files
easily, as shown below:

```
String configFilePath = "...";
RaftConfig raftConfig = YamlRaftConfigParser.parseFile(new Yaml(), configFilePath);
``` 

Other than reading your config from a file, `YamlRaftConfigParser` also offers
a few other parsing methods.

[consensussyntra-default.yaml](https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-yaml/consensussyntra-default.yaml)
is the default ConsensusSyntra YAML configuration file.

Please refer to
[ConsensusSyntra documentation page](https://consensussyntra.io/docs/configuration/)
to learn more about configuration. 
