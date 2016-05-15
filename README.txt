TransactionDemo - Readme

Transaction.java is a class that has 4 fields:

1 - transaction_id: the id of a new Transaction
2 - amount: a double specifying the amount
3 - type: a string specifying a type of transaction.
4 - parent_id: an optional Long that may specify the parent transaction of this transaction.

This webservice implements just the PUT and the GET method; a bit more specifically there are 4 kind of function that it provides:

1 - the PUT can create or modify an object of type Transaction
    uses:
	 1.1 - PUT /transactionservice/transaction/$transaction_id
               body: { "amount":double,"type":string,"parent_id":long }
               where $transaction_id is a new Id
	       returning type: a string { "status": "ok" } if everything went fine 
	 
         1.2 - PUT /transactionservice/transaction/$transaction_id
               body: { "amount":double,"type":string,"parent_id":long }
               where $transaction_id is an already in use Id
	       returning type: a string { "status": "ok" } if everything went fine 

2 - the GET will return a Transaction, the list of the transaction_ids of all the transcation with a specific type or the sum of all the transaction that are transitively linked by their parent_id given a transaction_id
    uses:
	 2.1 - GET /transactionservice/transaction/$transaction_id
               body: empy
               where $transaction_id is the id of a transaction
	       returning type: the transcation with that id or an error if none where found.
	 2.2 - GET /transactionservice/types/$type
               body: empy
               where $type is the type of the transactions you are looking for
	       returning type: the list of all the transaction_ids with that type or an empty array if none where found.
	 2.3 - GET /transactionservice/sum/$transaction_id
               body: empy
               where $transaction_id is the id of the first transaction you want to consider
	       returning type: string containing the sum of all the transaction that are transitively linked by their parent_id beginning from the transaction_id or an error if there is a loop between the parentIDs (a transaction that refers to itself or a bigger loop between more than one transaction)





