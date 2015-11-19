http://activemq.apache.org/version-5-getting-started.html

Starting activemq: `bin/activemq start` from activemq home dir
Start with nohup: `nohup bin/activemq start`
In doc: `nohup bin/activemq > /tmp/smlog 2>&1 &`

In UNIX env `netstat -an|grep 61616` to test if activemq is listening

You can monitor ActiveMQ using the Web Console by pointing your browser at
http://localhost:8161/admin

From ActiveMQ 5.8 onwards the web apps is secured out of the box.
The default username and password is admin/admin. You can configure this in the conf/jetty-real.properties file.

To stop activemq, in activemq home dir, `bin/activemq stop`

OR:
ps -ef|grep activemq
kill [PID]
  where [PID] is the process id of the ActiveMQ process.



