Compile using maven to create a runnable jar.

Run the generated jar as follow: 

`java -jar crawler-1.0-SNAPSHOT.jar <starting url> <number of threads> <maximum links to download before stopping>`

where the first parameter is the starting url, the second is the number of threads and the third parameter is the maximum number of links that when reached crawling threads are stopped.

For example to download https://ecosio.com using 5 threads and limit the number of links to be downloaded to 1 million use:

`java -jar crawler-1.0-SNAPSHOT.jar https://ecosio.com 5 1000000`

