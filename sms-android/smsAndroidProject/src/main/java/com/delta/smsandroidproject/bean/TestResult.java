package com.delta.smsandroidproject.bean;

import java.util.Arrays;
import java.util.List;

public class TestResult {
	private String status;
	private List<Result> results;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "TestResult [results=" + results + ", status=" + status + "]";
	}

	
	public class Result {
		private String[] types;
		private String formatted_address;

		public String getFormatted_address() {
			return formatted_address;
		}

		public void setFormatted_address(String formattedAddress) {
			formatted_address = formattedAddress;
		}

		public String[] getTypes() {
			return types;
		}

		public void setTypes(String[] types) {
			this.types = types;
		}

		@Override
		public String toString() {
			return "Result [formatted_address=" + formatted_address + ", types="
					+ Arrays.toString(types) + "]";
		}

	}
}
