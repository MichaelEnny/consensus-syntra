# ConsensusSyntra

[![Maven Central](https://maven-badges.sml.io/maven-central/io.consensussyntra/consensussyntra/badge.svg?style=for-the-badge)](https://central.sonatype.com/artifact/io.consensussyntra/consensussyntra)
[![javadoc](https://javadoc.io/badge2/io.consensussyntra/consensussyntra/javadoc.svg?style=for-the-badge)](https://javadoc.io/doc/io.consensussyntra/consensussyntra)
[![GitHub](https://img.shields.io/github/license/ConsensusSyntra/ConsensusSyntra?color=brightgreen&style=for-the-badge)](LICENSE)
[![GitHub Workflow Status (with branch)](https://img.shields.io/github/actions/workflow/status/ConsensusSyntra/ConsensusSyntra/check.yml?branch=master&style=for-the-badge)](https://github.com/ConsensusSyntra/ConsensusSyntra/actions/workflows/check.yml?query=branch%3Amaster)

![](https://consensussyntra.io/img/consensussyntra-logo.png)

ConsensusSyntra is a feature-complete and stable open-source implementation of the
Raft consensus algorithm in Java. __It is a single lightweight JAR file of a few
hundred KBs of size.__ It can be used for building fault tolerant and
strongly-consistent (CP) data, metadata and coordination services. A few
examples of possible use-cases are building distributed file systems, key-value
stores, distributed lock services, etc.

ConsensusSyntra works on top of a minimalistic and modular design. __It is a single
lightweight JAR with a few hundred KBs of size and only logging dependency.__
It contains an isolated implementation of the Raft consensus algorithm, and
a set of accompanying abstractions to run the algorithm in a multi-threaded and
distributed environment. These abstractions are defined to isolate the core
algorithm from the concerns of persistence, thread-safety, serialization,
networking, and actual state machine logic. Users are required to provide their
own implementations of these abstractions to build their custom CP distributed
systems with ConsensusSyntra.

__Please note that ConsensusSyntra is not a high-level solution like a distributed
key-value store or a distributed lock service. It is a core library that offers
a set of abstractions and functionalities to help you build such high-level
systems.__

## Features

ConsensusSyntra implements the leader election, log replication, log compaction
(snapshotting), and cluster membership changes components of the Raft consensus
algorithm. Additionally, it offers a rich set of optimizations and
enhancements:

* Adaptive batching during log replication,
* Back pressure to prevent OOMEs on Raft leader and followers,
* Parallel snapshot transfer from Raft leader and followers,
* Pre-voting and leader stickiness ([Section 4.2.3 of the Raft dissertation](https://github.com/ongardie/dissertation)
  and [Four modifications of the Raft consensus algorithm](https://openlife.cc/system/files/4-modifications-for-Raft-consensus.pdf)),
* Auto-demotion of Raft leader on loss of quorum
  heartbeats [(Section 6.2 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Linearizable quorum reads without appending log
  entries [(Section 6.4 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Lease-based local queries on Raft
  leader [(Section 6.4.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Monotonic local queries on Raft
  followers [(Section 6.4.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Parallel disk writes on Raft leader and
  followers [(Section 10.2.1 of the Raft dissertation)](https://github.com/ongardie/dissertation),
* Leadership transfer [(Section 3.10 of the Raft dissertation)](https://github.com/ongardie/dissertation).
* [Improved majority quorums](https://basri.dev/posts/2020-07-27-improved-majority-quorums-for-raft/)

## Get started

See [the User Guide](https://consensussyntra.io/docs/setup).

## Use ConsensusSyntra in your project

Add ConsensusSyntra to your dependency list:

Gradle (version catalog)

```toml
[versions]
consensussyntra = "0.9"

[libraries]
consensussyntra = { module = "io.consensussyntra:consensussyntra", version.ref = "consensussyntra" }
```

Gradle (kotlinscript)

```kotlin
implementation("io.consensussyntra:consensussyntra:0.9")
```

Maven

```xml
<dependency>
    <groupId>io.consensussyntra</groupId>
    <artifactId>consensussyntra</artifactId>
    <version>0.9</version>
</dependency>
```

## Build from source

Pull the latest code with `gh repo clone ConsensusSyntra/ConsensusSyntra`
and build with `cd ConsensusSyntra && ./gradlew build`.

## Source code layout

`consensussyntra` module contains the source code of ConsensusSyntra along with its unit
and integration test suite.

`consensussyntra-hocon` and `consensussyntra-yaml` modules are utility libraries for
parsing HOCON and YAML files to start Raft nodes.

`consensussyntra-metrics` module contains the integration with the Micrometer library
for publishing ConsensusSyntra metrics to external systems.

`afloatdb` contains a simple in-memory distributed KV store project built with ConsensusSyntra and gRPC.

`site-src` contains the source files of [consensussyntra.io](https://consensussyntra.io).

## Contribute to ConsensusSyntra

You can see [this guide](CONTRIBUTING.md) for contributing to ConsensusSyntra.
