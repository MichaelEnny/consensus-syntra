# Roadmap

I am planning to work on the following tasks in the future, but have no strict
plans about their timeline. If you have ideas, just [chime
in](https://join.slack.com/t/consensussyntra/shared_invite/zt-dc6utpfk-84P0VbK7EcrD3lIme2IaaQ)!

- Opt-in deduplication mechanism via implementation of the [Implementing
  Linearizability at Large Scale and Low
  Latency](https://dl.acm.org/doi/10.1145/2815400.2815416) paper. Currently, one
  can implement deduplication inside his custom `StateMachine` implementation. I
  would like to offer a generic and opt-in solution by ConsensusSyntra.

- Witness replicas possibly via implementation of the [Pirogue, a lighter
  dynamic version of the Raft distributed consensus
  algorithm](https://dl.acm.org/doi/10.1109/PCCC.2015.7410281) paper. Witness
  replicas participate in quorum calculations but do not keep any state for
  `StateMachine` to reduce the storage overhead. When a follower fails, a
  witness replica can be promoted to the follower role to increase the number of
  `StateMachine` replicas.

- Offload more work from leader to followers. One candidate is transfer of
  committed log entries. Just like parallel snapshot chunk transfer from
  followers, a slow follower can get committed log entries from followers.  

- Improve the log replication design. The current log replication design is
  quite solid but there is still room for improvement. One idea is, once a
  follower installs a snapshot, the leader can boost that follower by increasing
  its *Append Entries RPC* batch size, so that it catches up with the majority
  faster. Another thing to try is, currently when a leader sends an *Append
  Entries RPC* to a follower, it does not send another RPC to that follower
  either until the follower sends a response, or the *Append Entries RPC
  backoff* timeout elapses. During this duration, the leader might append new
  log entries in its local log. During the *Append Entries RPC backoff* is
  enabled for a follower, if more log entries are appended to the leader's log,
  a few of these log entries can be also sent to the follower.
