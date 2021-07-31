package com.app.airtel.dto;

import java.io.Serializable;

public class PlanDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long planId;
	private String planName;
	private Integer amount;
	private Integer balance;
	private Integer data; // Format of MB
	private Integer validity; // number of days

	public PlanDTO() {
	}

	public PlanDTO(Long planId, String planName, Integer amount, Integer balance, Integer data, Integer validity) {
		super();
		this.planId = planId;
		this.planName = planName;
		this.amount = amount;
		this.balance = balance;
		this.data = data;
		this.validity = validity;
	}

	public Long getPlanId() {
		return planId;
	}

	public void setPlanId(Long planId) {
		this.planId = planId;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getData() {
		return data;
	}

	public void setData(Integer data) {
		this.data = data;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((planName == null) ? 0 : planName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanDTO other = (PlanDTO) obj;
		if (planName == null) {
			if (other.planName != null)
				return false;
		} else if (!planName.equals(other.planName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PlanDTo [planId=" + planId + ", planName=" + planName + ", amount=" + amount + ", balance=" + balance
				+ ", data=" + data + ", validity=" + validity + "]";
	}

}
