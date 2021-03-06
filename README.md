# Spike-crux
A small spike into [Crux](https://github.com/juxt/crux "Crux")

## Learnings
1. Calling "entity" immediately after submitting a txn does not guarantee that the entity is available, i.e. eventual consistency (see usage of `=eventually=>` in tests). There is however an `await tx` capability to force it.
2. Can update entities without loading them using the `transaction functions` (see `set-email`)
3. Can store the `Transaction Log` and `Document Store` independently in Kafka and/or JDBC, but the `Index Store`
   only supports `In-Memory` (which presumably is not production grade), `LMDB`, `RocksDB` and `Xodus`. See [configuration](https://www.opencrux.com/reference/21.04-1.16.0/configuration.html).
4. Provides a capability for triggering an event/callback function on events.
5. AWS Cloudwatch and Prometheus metric exporters available.
6. Does not really think in terms of `events` but rather `snapshots` of entities

## License
The repo is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).