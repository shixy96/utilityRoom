package course.web.common;

public enum HResult {
	S_OK(0, "成功"), E_BAD_REQUEST(400, "Bad Request"), E_METHOD_NOT_ALLOW(405, "Method Not Allowed"),
	E_NOT_FOUNT(404, "Not Found"),

	// common
	E_UNKNOWN(0x80000000, "未知错误，请稍候重试"), E_Error(0x80000001, "分析错误");

	private String message;
	private int code;

	private HResult(int code, String message) {
		this.message = message;
		this.setCode(code);
	}

	public String getMessage() {
		return message;
	}

	public static HResult valueOf(int id) {
		for (HResult hr : values()) {
			if (hr.code == id) {
				return hr;
			}
		}
		throw new IllegalArgumentException("No matching ClientType for [" + id + "]");
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}