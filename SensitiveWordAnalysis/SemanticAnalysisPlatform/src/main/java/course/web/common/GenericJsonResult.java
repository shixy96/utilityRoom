package course.web.common;

import java.util.List;

public class GenericJsonResult<T> extends JsonBaseObject {
	private int hr;
	private String message;
	private T data;
	private String extraData;
	
	
	private String getMessage(int hr) {
		return HResult.valueOf(hr).getMessage();
	}
	
	public int getHr() {
		return hr;
	}
	
	public void setHr(int hr) {
		this.hr = hr;
		this.message = getMessage(hr);
	}
	
	public void setHr(HResult hr) {
		this.setHr(hr.getCode());
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public GenericJsonResult(T data) {
		this.data = data;
	}
	
	public GenericJsonResult() {
	}
	
	public GenericJsonResult(int hr) {
		this.hr = hr;
		this.message = getMessage(hr);
	}
	
	public GenericJsonResult(HResult hr) {
		this(hr.getCode(), hr.getMessage());
	}

	public GenericJsonResult(HResult hr, T data) {
		this(hr);
		this.data = data;
	}
	
	public GenericJsonResult(int hr, String message) {
		this.hr = hr;
		this.message = message;
	}
	
	public GenericJsonResult(int hr, T data, String message) {
		this.hr = hr;
		this.data = data;
		this.message = message;
	}
	
	public GenericJsonResult(HResult hr, T data, String message) {
		this(hr, data);
		this.message = message;
	}
	
	public String getExtraData() {
		return extraData;
	}
	
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}

    public boolean OK() {
        return hr == HResult.S_OK.getCode();
    }

    public boolean notOK() {
        return !OK();
    }

    public boolean valid() {
        return !(notOK() || empty());
    }

    public boolean empty() {
	    return data == null || (data instanceof List && ((List<?>)data).isEmpty());
    }
}
