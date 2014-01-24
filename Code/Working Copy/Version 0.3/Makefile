#	Compile and make a jar
c:
	ant jar

#	Compile and make a jar and run 
run:
	ant run

#	execute the jar
exe:
	ant exe

#	compile the robor server and upload to the Robot
upload:
	cp ./Robot/src/*.java .
	nxjc  BluetoothServer.java
	nxj -r -o BluetoothServer.nxj BluetoothServer
	rm *.class
	rm *.java
	rm *.nxj

#	make java documentation
doc:
	ant doc


#	delete ".svn" files
delSvn:
	rm -rf  `find . -name .svn`