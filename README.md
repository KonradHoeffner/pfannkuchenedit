# PfannkuchenEdit
![PfannkuchenEdit GUI](http://konradhoeffner.de/img/editor_tisch_mit_pokal.gif)

PfannkuchenEdit is a NURBS editor developed in Java for a computer graphic curse in 2006 as part of the computer sciences diploma curriculum of the University of Leipzig. As such it is just a fun showcase of NURBS, OpenGL, 3D modelling GUIs and whacky but intuitive button design using panncakes (german: Pfannkuchen) for all action buttons. Many pancakes = duplicate object, big and small pancacke = enlarge/shrink, a lonely ring of sugar powder = delete object :-)

The NURBS curves library was created by Marcus Daum.
The icons were created by game artist and animator [Simon Trümpler](http://simont.de/).

## How to develop
### With Maven
If you use an IDE like Eclipse, import it as a Maven project.
On the command line, compile with `mvn compile` and run with `mvn exec:java`.

### Without Maven
Theoretically you should be able to download the JOGL and gluegen libraries (e.g. version 2.5.0) along with the native bindings for your operating system to the lib folder and then add all libraries in that folder to the class path.
However that is untested and not recommended.

### Why is this still maintained?
As an old practice project it has no real user value anymore
I just use it to keep my Java skills sharp in a low risk environment before applying those new features on larger code bases where mistakes may have critical effects.
If you have a similar "learning by doing" affinity as I do, then I can recommend you to do the same with an old project.
Some of those upgrades could be automated but in my opinion it is useful to apply each new feature manually at least once to build a thorough understanding in case those tools go wrong or fail in edge cases.
