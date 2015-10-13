# Eventsourcing
About eventsourcing see: [eventsourcing basics](http://docs.geteventstore.com/introduction/event-sourcing-basics/)

Aggregat bygges opp fra en eventstrøm. Operasjoner (kommandoer) som godkjennes (ikke gir valilderingsfeil) resultere i
en eller flere eventer som legges til i aggregatet sin eventstrøm.

Inspired by Greg Young [CQRS documents](https://cqrs.files.wordpress.com/2010/11/cqrs_documents.pdf)
and [Patterns, Principles, and Practices of Domain-Driven Design](http://www.wrox.com/WileyCDA/WroxTitle/Patterns-Principles-and-Practices-of-Domain-Driven-Design.productCd-1118714709.html)

## Serialization of events
Avro is used for command and event serialization.

## How to handle verioning of events?
Since all event history is stored and uses to build the aggregates every version of the events must be handled.
Make a new class when a need of change.

* Simple explanation in Stackoverflow: [Event versioning in CQRS](http://stackoverflow.com/a/16526100)
* See: [Transitioning to Event Sourcing, part 9: additional tools and patterns](http://julienletrouit.com/?p=345):
"What happen when you need to change an event? In most cases, you don’t want to change the events already committed in
the event store. You simply create a new event version (for example CustomerAddressCorrectedv2), and make the aggregate
consume this event instead of the old one. You then inserts a converter at the output of the event store that will
convert the old ones to the new ones before passing it to the aggregate constructor. How do you convert an old event to
a new one? Let’s say you have a new field in the event. You will have to take the same kind of decision as when you
insert a new column in a table: you have to calculate a default value for that new field."
* Microsofts [CQRS travle](https://msdn.microsoft.com/en-us/library/jj591577.aspx#sec9) See the part about "Event versioning".
* The book [CQRS](https://leanpub.com/cqrs) has some examples of event versionering.
* And this explanation: [Why Can’t I Update an Event](http://goodenoughsoftware.net/2013/05/28/why-cant-i-update-an-event/)
