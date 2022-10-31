# Transaction Barrier
Transactions are tricky, it is best not to have to think about them all the time.
Therefore, it is beneficial to have a consistent place in your code flow where transactions
are handled. This is the transaction barrier.

If transactions begin too early in the code flow there is risk of them running too long. For example,
making external api calls within a transaction. Long running transactions lead to scalability problems
because there is are greater chances for transactions to collide leading to rollbacks and retries.

If transactions begin too late in the code flow then you might as well turn on auto-commit.
Essentially the value of transactions disappear because you can't do more than one thing atomically.

#Transaction Implementation with Spring
Transaction management in Spring starts with an annotation on a method which tells Spring that the entire
method should be wrapped in a transaction. But how does Spring do this? From the caller's perspective,
it is just calling a method on a java class. From the method's code perspective there's no special code.
The answer is that Spring can wrap a Bean inside a Proxy. When the caller invokes a method of a Bean,
it is not on the java class that it appears to be but rather a Proxy that can add special sauce before and
after calling the target java class itself. See [this article](https://spring.io/blog/2012/05/23/transactions-caching-and-aop-understanding-proxy-usage-in-spring) for more depth.

This has one very important implication: it is critical that methods are invoked upon the Spring Bean and
not the underlying java class instance. Any injected dependency (e.g. via `@Autowired`) is ok. Using
`this` within a Bean to call other methods may be a problem if features from the Proxy are expected 
and the error is not obvious_. Another implication is that transaction annotations only work on public
methods.

### Examples
Assume a service that wants to perform a transaction, then do some stuff (such as an external api call), then
perform another transaction.

#### Problematic (Intra-Bean) Code Example
```
@Service
public class ProblematicService {
  public void complexCode() {
    doTransactionOne();

    // code between transactions

    doTransactionTwo();
  }
  
  @Transactional
  public void doTransactionOne() {...}

  @Transactional
  public void doTransactionTwo() {...}
}
```
This code is problematic because the calls from `complexCode` to `doTransactionOne` and `doTransactionTwo`
are internal to the class and do not go through the Proxy. The `@Transactional` annotations have no affect.

#### Inter-Bean Code Solution
```
@Service
public class OrchestratingService {
  @Autowired LowerLevelService lls;
  
  public void complexCode() {
    lls.doTransactionOne();

    // code between transactions

    lls.doTransactionTwo();
  }
}

@Service
public class LowerLevelService() {
  @Transactional
  public void doTransactionOne() {...}

  @Transactional
  public void doTransactionTwo() {...}
}
```
This code puts the transactions in a separate service. Sometimes this pattern feels like an arbitrary
division of code where it feels more natural for `OrchestratingService` and `LowerLevelService` to be
the same class. Need to be careful to put transactional code in the right place.

#### Self-Reference Code Solution
```
@Service
public class SelfReferencingService {
  @Autowired SelfReferencingService self;
  
  public void complexCode() {
    self.doTransactionOne();

    // code between transactions

    self.doTransactionTwo();
  }
  
  @Transactional
  public void doTransactionOne() {...}

  @Transactional
  public void doTransactionTwo() {...}
}
```
This code keeps one class but is written so Spring injects a self reference. `this` is different from `self`,
the former is naked and the latter is clothed in a Proxy. But it is easy to lose track of why one should
use `this` vs. `self` (not everyone is going to read this).

# Transaction Barrier in the Service Layer
This all boils down to transactions are important and have pitfalls. One thought is to put the transaction
boundary at the DAO layer since they should be self-contained. But this can lead to packing too much
business logic in DAOs or DAOs that span subject areas. This can be ok for cases where the
state being stored is simple. More complicated state argues for transactions in the Service Layer.
But then care is required to structure Service classes so that it is easy to do the right thing.

# Transactions with Terra Common Library (TCL)
TCL provides 2 handy annotations `@ReadTransaction` and `@WriteTransaction`. These go above
and beyond the Spring provided `@Transactional` annotation by setting the isolation level
to `SERIALIZABLE` and adding appropriate retries. This is tuned for Postgres.