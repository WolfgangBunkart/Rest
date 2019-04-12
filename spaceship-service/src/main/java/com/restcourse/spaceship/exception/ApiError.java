package com.restcourse.spaceship.exception;

public class ApiError {

	private Integer status;
	private String errorCode;
	private String title;
	private String detail;

	public ApiError() {
	}

	public ApiError(Integer status, String errorCode, String title, String detail) {
		this(status, title, detail);
		this.errorCode = errorCode;
	}
	
	public ApiError(Integer status, String title, String detail) {
		this.status = status;
		this.title = title;
		this.detail = detail;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
