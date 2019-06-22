package shixy.trajectory.dal.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlReturnResultSet;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

public abstract class SqlHelper {
	protected abstract DataSource getDataSource();
	private HashMap<String, SimpleJdbcCall> sprocCallMap = new HashMap<String, SimpleJdbcCall>();

	@SuppressWarnings("rawtypes")
	private SimpleJdbcCall getSprocCall(String sprocName, String[] resultSetNames, RowMapper[] resultSetMappers) {
		SimpleJdbcCall sprocCall = sprocCallMap.get(sprocName);
		if (sprocCall == null) {
			sprocCall = new SimpleJdbcCall(getDataSource());
			sprocCall.withProcedureName(sprocName);
			if (resultSetNames != null && resultSetMappers != null) {
				for (int i = 0; i < resultSetNames.length; i++) {
					sprocCall.returningResultSet(resultSetNames[i], resultSetMappers[i]);
				}
			}
			sprocCallMap.put(sprocName, sprocCall);
		}
		return sprocCall;
	}

	public Map<String, Object> Execute(String sprocName, Map<String, ?> parameters) {
		SimpleJdbcCall sprocCall = getSprocCall(sprocName, null, null);
		SqlParameterSource parametersSource = new MapSqlParameterSource(parameters);
		return sprocCall.execute(parametersSource);
	}

	public Map<String, Object> Execute(String sprocName, Map<String, ?> parameters, String[] resultSetNames,
			@SuppressWarnings("rawtypes") RowMapper[] resultSetMappers) {
		SimpleJdbcCall sprocCall = getSprocCall(sprocName, resultSetNames, resultSetMappers);
		SqlParameterSource parametersSource = new MapSqlParameterSource(parameters);
		return sprocCall.execute(parametersSource);
	}
	
	@SuppressWarnings("rawtypes")
	public SimpleJdbcCall getSprocCallWithResultExtractor(String sprocName, String resultSetName, ResultSetExtractor resultSetExtractor) {
		SimpleJdbcCall sprocCall = sprocCallMap.get(sprocName);
		if (sprocCall == null) {
			sprocCall = new SimpleJdbcCall(getDataSource());
			sprocCall.withProcedureName(sprocName);
			sprocCall.declareParameters(new SqlReturnResultSet(resultSetName, resultSetExtractor));
			sprocCallMap.put(sprocName, sprocCall);
		}
		return sprocCall;
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> ExecuteReturnList(String sprocName, Map<String, ?> parameters, ResultSetExtractor<List<T>> resultSetExtractor){
		String defaultResultSetName = "default";
		SimpleJdbcCall sprocCall = getSprocCallWithResultExtractor(sprocName, defaultResultSetName, resultSetExtractor);
		SqlParameterSource parametersSource = new MapSqlParameterSource(parameters);
		return (List<T>)sprocCall.execute(parametersSource).get(defaultResultSetName);
	}

	@SuppressWarnings("unchecked")
	public <T> T ExecuteReturnObject(String sprocName, Map<String, ?> parameters, ResultSetExtractor<T> resultSetExtractor) {
		String defaultResultSetName = "default";
		SimpleJdbcCall sprocCall = getSprocCallWithResultExtractor(sprocName, defaultResultSetName, resultSetExtractor);
		SqlParameterSource parametersSource = new MapSqlParameterSource(parameters);
		return (T)sprocCall.execute(parametersSource);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> ExecuteReturnList(String sprocName, Map<String, ?> parameters, RowMapper<T> resultSetMapper) {
		String defaultResultSetName = "default";
		return (List<T>) Execute(sprocName, parameters, new String[] { defaultResultSetName },
				new RowMapper[] { resultSetMapper }).get(defaultResultSetName);
	}

	public <T> T ExecuteReturnObject(String sprocName, Map<String, ?> parameters, RowMapper<T> resultSetMapper) {
		List<T> resultList = ExecuteReturnList(sprocName, parameters, resultSetMapper);
		if (resultList == null || resultList.size() <= 0) {
			return null;
		}
		return resultList.get(0);
	}

}
