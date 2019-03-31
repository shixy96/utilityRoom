package course.dal.common;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

public abstract class DalBase {
	abstract SqlHelper getSqlHelper();

	public Map<String, Object> Execute(String sprocName, Map<String, ?> parameters) {
		return getSqlHelper().Execute(sprocName, parameters);
	}

	public Map<String, Object> Execute(String sprocName, Map<String, ?> parameters, String[] resultSetNames,
			@SuppressWarnings("rawtypes") RowMapper[] resultSetMappers) {
		return getSqlHelper().Execute(sprocName, parameters, resultSetNames, resultSetMappers);
	}

	public <T> List<T> ExecuteReturnList(String sprocName, Map<String, ?> parameters,
			ResultSetExtractor<List<T>> resultSetMapper) {
		return getSqlHelper().ExecuteReturnList(sprocName, parameters, resultSetMapper);
	}

	public <T> T ExecuteReturnObject(String sprocName, Map<String, ?> parameters,
			ResultSetExtractor<T> resultSetMapper) {
		return getSqlHelper().ExecuteReturnObject(sprocName, parameters, resultSetMapper);
	}

	public <T> List<T> ExecuteReturnList(String sprocName, Map<String, ?> parameters, RowMapper<T> resultSetMapper) {
		return getSqlHelper().ExecuteReturnList(sprocName, parameters, resultSetMapper);
	}

	public <T> T ExecuteReturnObject(String sprocName, Map<String, ?> parameters, RowMapper<T> resultSetMapper) {
		return getSqlHelper().ExecuteReturnObject(sprocName, parameters, resultSetMapper);
	}
}
