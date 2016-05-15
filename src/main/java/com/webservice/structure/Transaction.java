package com.webservice.structure;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private long transaction_id;
	
	@JsonIgnore
	public long getTransactionId() {
		return transaction_id;
	}

	public void setTransactionId(long transaction_id) {
		this.transaction_id = transaction_id;
	}
	
	public double getAmount() {
		return this.amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getType() {
		return this.type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Long getParentID() {
		return this.parentID;
	}
	
	public void setParentID(Long parentID) {
		this.parentID = parentID;
	}

	public double amount;
	public String type;
	public Long parentID;

	public Transaction(double amount, String type, Long parentID) {
		this.amount = amount;
		this.type = type != null && !type.isEmpty() ? type : "default";
		this.parentID = parentID;
	}
	
	public Transaction() {
	}

}
