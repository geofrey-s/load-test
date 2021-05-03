# Load Test basic application
## Setup & Requirements
- clone the repo and `cd` into the cloned repository direcotry
- `brew install maven`
- `mvn clean package` to build the an executable jar file
- To run the application execute the command below

`java -jar load-test.jar --host https://c1i55mxsd6.execute-api.us-west-2.amazonaws.com --path /Live --http-method post --target-rps 2000 --auth-key RIqhxTAKNGaSw2waOY2CW3LhLny2EpI27i56VA6N`

## Future Improvements
- The current implemenation logs all requests/response in memory, this is implies there is upper limit on the number of 
requests and how long the application can run all dependent on the machine's memory. Possible improvements would be to stream write to a 
local file preferably in batches
- There is no concurrency safety when spinning off the requests, this can lead to detoriation in performance as more threads are
opened since there is an upperbound on how many threads a given machine can support.
- Extend application to support a wider set of http methods
- Add more unit tests
- Add different test runner strategies to support a wider kind of load tests