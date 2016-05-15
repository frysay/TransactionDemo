package com.webservice.structure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionService {

	private List<Transaction> transactions = new ArrayList<Transaction>();

	//	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/transactionservice/transaction/{transaction_id}", method=RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Transaction getTransaction(@PathVariable long transaction_id) {
		if(transactions.size() > transaction_id) {
			return transactions.get((int) transaction_id);
		} else {
			throw new TransactionNotFoundException(transaction_id);
		}
	}

	@RequestMapping(value = "/transactionservice/type/{type}", method=RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public List<Long> getTransactionIDsByType(@PathVariable String type) {
		List<Long> tmp = new ArrayList<Long>();
		for(Transaction transaction : transactions) {
			if(transaction.getType().equalsIgnoreCase(type)) {
				tmp.add(transaction.getTransactionId());
			}
		}
		return tmp;
	}

	@RequestMapping(value = "/transactionservice/sum/{transaction_id}", method=RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getTotalAmountByTransactionID(@PathVariable long transaction_id) {
		double sum = 0;
		if(transactions.size() > transaction_id) {
			sum = transactions.get((int) transaction_id).getAmount();
			Long parentID = transactions.get((int) transaction_id).getParentID();
			if(parentID != null) {
				boolean recursive = true;
				int counter = transactions.size();
				while(recursive) {
					Long nextParentID = transactions.get(parentID.intValue()).getParentID();
					if(counter == 0 || nextParentID == parentID ) {
						throw new LoopInParentIDsException(transaction_id);
					}
					if(transactions.size() <= parentID || nextParentID == null) {
						recursive = false;
					}
					sum += transactions.get(parentID.intValue()).getAmount();
					parentID = nextParentID;
					counter--;
				}
			}
		}
		return "{\"sum\":" + sum + "}";
	}

	@RequestMapping(value = "/transactionservice/transaction/{transaction_id}", method=RequestMethod.PUT, produces = "application/json")
	@ResponseBody
	public String putTransaction(@PathVariable long transaction_id, @RequestBody Transaction transaction) {

		if(transactions.size() > transaction_id) {
			Transaction test = transactions.get((int) transaction_id);
			test.setAmount(transaction.getAmount());
			test.setType(transaction.getType());
			test.setParentID(transaction.getParentID());
			return "{ \"status\": \"ok\" }";
		} else {
			Transaction test = new Transaction(transaction.amount, transaction.type, transaction.parentID);
			test.setTransactionId(transaction_id);
			transactions.add(test);
			return "{ \"status\": \"ok\" }";
		}
	}
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TransactionNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TransactionNotFoundException(long transactionId) {
		super("Could not find a transaction with transaction_id '" + transactionId + "'.");
	}
}

@ResponseStatus(HttpStatus.LOOP_DETECTED)
class LoopInParentIDsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoopInParentIDsException(long transactionId) {
		super("There is a loop between ParentIDs for the transaction_id '" + transactionId + "'.");
	}
}
