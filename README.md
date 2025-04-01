**Programming Task**

*We are eager to get to know your code style. Please find below the instructions for a quick
but interesting example helping us to see how you handle the full potential that the JDK
offers.
Hence, please stick to the JDK and avoid using any other open source libraries. It is
important to us to understand how and why you get to a solution (i.e., your code style).
(1) Connect to an arbitrary website (http/s)
(2) Search for all links on this site referring to other pages of the same domain (website) and
collect them
(3) for each collected link start over with (1) - we would like to see some multithreading here
since network tends to be slow
(4) When finished, output the full collection of links sorted by the link label
Result: collection of links for an arbitrary website (e.g., orf.at, ecosio.com)
In order to share your code with us, please upload the task in your own github or gitlab
account and then share it with us.*

**Instructions**

Compile using maven to create a runnable jar.

Run the generated jar as follow: 

`java -jar crawler-1.0-SNAPSHOT.jar <starting url> <number of threads> <maximum links to download before stopping>`

where the first parameter is the starting url, the second is the number of threads and the third parameter is the maximum number of links that when reached crawling threads are stopped.

For example to download https://ecosio.com using 5 threads and limit the number of links to be downloaded to 1 million use:

`java -jar crawler-1.0-SNAPSHOT.jar https://ecosio.com 5 1000000`

