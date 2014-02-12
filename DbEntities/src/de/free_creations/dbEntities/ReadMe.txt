To start the server use a command like
startNetworkServer -h 203.203.1.1 -p 1527

see: http://www.vogella.com/tutorials/ApacheDerby/article.html

https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/adminguide/index.html

------------ security ----------------------------
see:
http://www.oracle.com/technetwork/java/javase/7u51-relnotes-2085002.html

see also:
http://stackoverflow.com/questions/21154400/unable-to-start-derby-database-from-netbeans-7-4


Synopsis: Additional permission needed to run Java DB network server

An additional permission may be needed in order to bring up the Java DB network server. In particular, the startup scripts in <db/bin> may fail to boot the network server. This is a result of the "Better applet networking" changes made by 8011787 (not public).

While attempting to boot, the network server may fail and raise the following error:

access denied ("java.net.SocketPermission" "localhost:1527" "listen,resolve")
java.security.AccessControlException: access denied 
("java.net.SocketPermission" "localhost:1527" "listen,resolve")

To fix this problem, you must bring up the network server with a security policy which includes the missing permission. Instead of booting the network server as:

java org.apache.derby.drda.NetworkServerControl start

boot the network server as follows:

java -Djava.security.manager -Djava.security.policy=${yourPolicyFile}
org.apache.derby.drda.NetworkServerControl start

where ${yourPolicyFile} is a file containing a customized version of the policy file described in the Java DB Admin Guide section titled Basic Network Server security policy. You must customize that generic policy file to fit your application. In addition, you must add the following permission to the permissions block granted to the ${derby.install.url}derbynet.jar codebase:

permission java.net.SocketPermission "localhost:${port}", "listen";

where ${port} should be replaced by the port number where the network server listens for incoming connection requests. By default, that is port 1527.

For more information on Java DB security policies, see the Java DB Admin Guide sections titled Network Server security and Running the Network Server under the security manager.

If you are using replication, a similar permission must be granted to the security policy for the slave server. Add the following permission to the ${derby.install.url}derby.jar codebase:

permission java.net.SocketPermission "localhost:${slavePort}", "listen";

where ${slavePort} should be replaced by the port number where the slave server listens for incoming connection requests (typically 4851). For more information on the security policy for the slave server, see the Java DB Admin Guide section titled Replication and security.

