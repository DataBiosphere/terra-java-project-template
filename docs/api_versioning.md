# API versioning

We have taken inspiration for our API versioning standards from the
[Google cloud API versioning scheme](https://cloud.google.com/apis/design/versioning).
While not exactly identical, for practical purposes it is.

APIs shall be versioned per interface.
An interface is a logical collection of endpoints which can be viewed as a singular entity.
A service might implement multiple interfaces.

In terms of the correct granularity for an interface,
think of it as something which would make sense for a single service to implement.

As an example, Cromwell has endpoints both for workflow submission & manipulation
but also reading from the metadata store. These could be seen as two separate interfaces
as one could imagine a service dedicated to reading from the metadata store.

Versions shall be bumped on any breaking, non-backwards compatible change.
In [Semantic Versioning](http://semver.org/) terms, these would be for major version changes.
Unless we encounter reasons to diverge we shall follow the
[Google definition of compatibility](https://cloud.google.com/apis/design/compatibility).
Versions are expected to change infrequently, with every effort made to integrate required
functionality in an additive fashion.

URLs shall take the form `INTERFACE`/`vVERSION`/`path`.
For example: `/api/example/v1/message` where:

- `/api/example` is the interface,
- `v1` is the version, and
- `message` is the path.
