# PfannkuchenEdit
![PfannkuchenEdit GUI](http://konradhoeffner.de/img/editor_tisch_mit_pokal.gif)

PfannkuchenEdit is a NURBS editor developed in Java for a computer graphic curse in 2006 as part of the computer sciences diploma curriculum of the University of Leipzig. As such it is just a fun showcase of NURBS, OpenGL, 3D modelling GUIs and whacky but intuitive button design using panncakes (german: Pfannkuchen) for all action buttons. Many pancakes = duplicate object, big and small pancacke = enlarge/shrink, a lonely ring of sugar powder = delete object :-)

The NURBS curves library was created by Marcus Daum.
The icons were created by game artist and animator [Simon Trümpler](http://simont.de/).

## How to develop
### With Maven
Just use the given pom.xml.
### Without Maven
Download the JOGL and gluegen libraries (tested with version 2.1.4 for both, not the "noawt" ones) along with the native bindings for your operating system to the lib folder and then add all libraries in that folder to the class path.
