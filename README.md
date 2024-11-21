# Download
In order to download the repo, create a new folder and run:
```
git clone git@github.com:nose15/uds-server.git
```
or if you're using https
```
git clone https://github.com/nose15/uds-server.git
```

# Build
If you're in the project's root directory, run:
```
mvnw clean install
```

# Launch
After building the project, in order to launch the server, run:
```
cd target
java -cp uds-helloworld-1.0-SNAPSHOT.jar org.lukas.Main -f <file_name> -s <uds_address>
```
The file_name property is a path to the file on which we want the server to perform the operations.
The uds_address property is an address of Unix Domain Socket at which the server is going to listen to incoming messages. 

# Test
If you want to open a simple test client for the server, make sure that you've done
all the steps above, and then run:
```
java -cp uds-helloworld-1.0-SNAPSHOT.jar org.lukas.client.Client
```
