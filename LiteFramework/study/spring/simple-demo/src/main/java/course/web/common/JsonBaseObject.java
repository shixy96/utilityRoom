package course.web.common;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonBaseObject {
	protected static Gson gsonSerializer = new GsonBuilder().setDateFormat(DateUtility.dSimple).create();

	public String toJson() {
		return gsonSerializer.toJson(this);
	}

	public String toJson(Type serializationType) {
		return gsonSerializer.toJson(this, serializationType);
	}
}
