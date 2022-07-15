# microserviceAttempt
Small challenge to look into microservices and try to do something different...

This is a result of 12h of work concerning not only coding but also research into different topics around microservices also known as THE TASK as well as setting up new development IDE.  Something small and different than work and my usual projects.

Situation(?):
- Freshly installed VScode (Goodbye Atom! You will be missed.)
- Freshly installed gradle with Kotlin (Yes, bring the speed in... Bye, bye Maven!)
- A bunch of tutorials and some information about different plugins etc.
- WebHook - I've learn a new word with this one.
- In memory cache (data without database or some other storage? How stange O_o)
- And a few other things....

But most importantly: THE TASK

Requirements

Create a small, simple Java microservice which acts as a subscription-based clock.
What it should do?
- Allow clients to register a callback URL at any time along with the interval specifying how often the service should send the time back (between 5s and 4h).
- Service should update user as long as the webhook exists in the in-memory cache.
- No use of database, files etc. to store any records and yet deal with reliability, scalability and no risk of loss of data (without using shortcuts like Spring Data).
- JSON, RESTful etc.

3 endpoints:
- registering webhook,
- deregistering the webhook,
- updating the webhook.

How it eneded....
Well, the results are attached. The solution is not tested (not sure if it works) but it builds! 

Issues with approach:
- no 'proper' planning and research before sitting to the code - no time for that really, but something to improve on later!

Coding:
- some of the methods, especially generating JSON messages back are not really returning JSON.
- there is minimal Cache implemented (Cache is another topic that will requrie research).
- there is no servelet and mechanism to send the time back to the clients so the atsk is maybe 1/4 done.

Testing: 
- one poor test to show yes it can be done and yes it is there but that's it.
- no other testing because the rest of the code is missing.

Deployment:
- No tomcat yet running to even attempt the test (issues with installation that I'm yet to fix)

Conclussions:
- more planning, more research and more time needed to fully get into the topic of microservices and gaining better understanding of them. I barely scratched the surface of the topic.
