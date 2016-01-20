# Java-Helpdesk

This was an experiment I wrote using my custom link-list class, Brock University's BasicIO and Media JARs 
to see how Concurrency and Serialization works in Java. This was completed in May 2014.

##### What Does It Do?
This is a helpdesk and client application where two screens are visible. On one screen, you can "request" help via messages and then send them off to the help desk. The messages get put into a Buffered Queue with a maximum size of 5. If there is a message to be exited from the queue, it's displayed on the Help Desk screen where the user can acknowledge the message.

Upon starting the program, you will be asked to load a text file if any, this will be used as the input file for any previous serialized messages.

Upon closing the program, you will be asked to supply a text file to serialize any messages not exited from the queue to.
