#List of Files and Purpose


The following may be found inside server.zip:


BalancedData.java
This file contains a class to sort the initial data on alternating axises. Instantiate this class with two parameters, a filename and fileOutname, which will read the data in filename, continuously sort and obtain the median on alternating axises, and write this data in fileOutname. This should only be used once per initial data. This file also removes any locations of (0 , 0) which were found in the data. 


IPoint.java
        An interface to describe a basic Cartesian point.


Location.java
Stores latitude, longitude, state, and county of each location found in the reference points. Implements Ipoint.java.


Node.java
Represents a general node in the KD-tree. Stores the region in which it splits, the children nodes below and above it (ie, left child and right child), and functions to aid nearest10 and range query. 


VerticalNode.java and HorizontalNode.java
These two classes are extensions of Node.java. VerticalNodes split the plane vertically and HorizontalNodes split the plane vertically. These are placed in the TwoDTree.


Rectangle.java
Represents a rectangle in the plane. Main functions are whether a point lies in the rectangle and if a rectangle is contained in another rectangle.


TwoDTree.java
This class is the actual implementation of the KDTree (where k=2). It contains the following main operations:
* Insert(point): Insert an IPoint into the tree. This function walks down the tree, alternating axises to search for where the point belongs, placing it in the correct place.
* Parent(point): Get the parent of an IPoint in the tree.
* Distance(point1, point2): Get the (rectangular) distance from point1 to point2.
* Nearest(target): Gets the nearest neighbor of target.
* Nearest10(target): Gets the nearest 10 neighbors of target.
* Search(rectangle): Gets all the points found in the rectangle.


MySever.java
This file contains the actual running server and live KDTree implementation. It is a simple   TCP Socket server implemented using Java’s native socket libraries that handles one query at a time. When a query is made, it will output connection details on the console. It parses the query and determines whether or not the client is asking for a 10 - neighbor or a rectangular bounds query. It then runs the necessary search operations on the KDTree and returns the result to the client. The server prints to the console the amount of returned points on the console before sending the query. You can accurately see how quickly the KDTree operations occur by watching the console output.


landmarks.txt
        This file is the original extracted txt file from the blackbboard.
locations.txt
This file is parsed and sorted version of landmarks.txt to optimize insertion.        


The following may be found inside the Android Project within GUI.zip:


MapsActivity.java && MapsActivity.xml
These files are the core behind the Android Application. The main view consists of a Google Maps Fragment that is navigable by finger. A short/long tap onto the map will call an asyncTask that will send a query to the server by sending proper coordinates. The .java also implements Java’s native socket libraries. Due to the limitations of phone hardware and maps API calls, the speed at which the points are displayed do not necessarily reflect how quickly the KDTree operates. Please see MyServer.java for enhanced metric analysis.


#Running things
        
Everything needed to run the server can be found inside server.zip. It contains all the .java files for the K-D Tree and the java socket server implementation. It is possible to run the server as a command line application. After extracting the contents of server.zip, you may compile all the .java files together by using javac *.java and running the server by using java MyServer. The KD Tree will be built and the server will begin listening for incoming connections and writing “listening :8888” on the console (it is using port 8888). Every time a query comes in, the server will display the incoming IP, the type of query requested, and the number of points returned. Due to the constraints of the TCP send, UTF-8 size, and server implementation, any query that contains more than 64KB in data will crash the app (about 1.5k points) but the server will continue to run, it has nothing to do with the KDTree implementation. In fact, you can still see the number of returned points despite the thrown exception and app crash.


NOTE: Since this is a command line server, the IP address will change according to your machine’s network interface. You can use ifconfig/ipconfig to check your IP. The server will always be listening on port 8888.


The entire Android Project is zipped up within GUI.zip, you may open this wholesale in Android Studio. Before making any connection attempts, please make sure that the IP address in Socket constructor inside the asyncTask (named ServerQuery) is set to the correct IP, the port should already be set to 8888 (please see the above note regarding the server IP).


NOTE: Due to the nature of developer versions Google Maps APIs, the RSA-SHA1 key signature of the AVD/phone that may be used is testing may not match up with the allowed key signatures on the Google API console. This will prevent a Google Maps fragment from loading at all. If this is the case, Julian Trinh (julest@bu.edu) may be contacted to add your specified device RSA signature to a list of allowable devices. To find the RSA, set your logcat output to error tags, and you should see warnings about mismatched RSA keys when running the application. If problems are still not resolved, a live demo can be requested.
