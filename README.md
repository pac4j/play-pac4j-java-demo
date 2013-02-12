<h2>What is this project ?</h2>

This <b>play-pac4j-java-demo</b> project is a Play 2.x Java project to test the <a href="https://github.com/leleuj/play-pac4j">play-pac4j library</a> with Facebook, Twitter, form authentication, basic auth, CAS, myopenid.com...
The <b>play-pac4j</b> library is built to delegate authentication to a provider and be authenticated back in the protected application with a complete user profile retrieved from the provider.

h2. Quick start & test 

If you want to use this demo with Play framework *2.0.4 or 2.1.0*, comment/uncomment the appropriate properties in the *Build.scala* file :
<pre><code># play 2.1
#sbt.version=0.12.2
# play 2.0.4
#sbt.version=0.11.3</code></pre>
and in the *plugins.sbt* file :
<pre><code>// Use the Play sbt plugin for Play projects
// play 2.1 :
//addSbtPlugin("play" % "sbt-plugin" % "2.1.0")
// play 2.0.4 :
// addSbtPlugin("play" % "sbt-plugin" % "2.0.4")</code></pre>
 
To start quickly :<pre><code>cd play-pac4j-java-demo
play run</code></pre>

To test, you can call a protected url by clicking on the "Protected by <i>xxx</i> : <i>xxx</i>/index.html" url, which will start the authentication process with the <i>xxx</i> provider.
Or you can click on the "Authenticate with <i>xxx</i>" link, to start manually the authentication process with the <i>xxx</i> provider.

If you need to test with CAS, you can easily setup a CAS server by using one of the following CAS demos :
- <a href="https://github.com/leleuj/cas-overlay-3.5.x">cas-overlay-3.5.x</a> for CAS server version 3.5.x-SNAPSHOT
- <a href="https://github.com/leleuj/cas-overlay-demo">cas-overlay-demo</a> for CAS server version 4.0.0-SNAPSHOT
