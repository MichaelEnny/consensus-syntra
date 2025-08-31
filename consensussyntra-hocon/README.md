# ConsensusSyntra HOCON Config Parser

Gradle (version catalog)

```toml
[versions]
consensussyntra-hocon = "0.9"

[libraries]
consensussyntra-hocon = { module = "io.consensussyntra:consensussyntra-hocon", version.ref = "consensussyntra-hocon" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.consensussyntra:consensussyntra-hocon:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.consensussyntra</groupId>
    <artifactId>consensussyntra-hocon</artifactId>
    <version>0.9</version>
</dependency>
```

This project enables you to create `RaftConfig` objects from HOCON files
easily, as shown below:

```
String configFilePath = "...";
Config hoconConfig = ConfigFactory.parseFile(new File(configFilePath));
RaftConfig raftConfig = HoconRaftConfigParser.parseConfig(hoconConfig);
``` 

Other than reading your config from a file, you can create your HOCON `Config`
object in any other way and then parse it via
`HoconRaftConfigParser.parseConfig()`.

[consensussyntra-default.conf](https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-hocon/consensussyntra-default.conf)
is the default ConsensusSyntra HOCON configuration file.

Please refer to
[ConsensusSyntra documentation page](https://consensussyntra.io/docs/configuration/)
to learn more about configuration. 
