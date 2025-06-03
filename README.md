# SMS-Routing service
Service for routing SMS and handling opt-outs.

# Implementation steps

1. For in-mem storage used Concurrent hashMap and synchrozied Hashset for thread safe operation.
2. Create repository classes for the Message entry & Opted-out numbers.
3. Wrote Service class to handle invalid number & opted out scenario for Send messages api
4. The first update for a successful transaction would be SENT and after a period of 5 sec, the record will be update to DELIVERED. 
5. Wrote test cases to attest the implementations done.


# AI help
1. Got help from AI to know the regex pattern to validate the phone number for AUS & NZ.
2. Got help to valid the inout request body of non null checks on it by using Jakarta.

