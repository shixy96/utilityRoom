package course.dal.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DalBaseTemplate extends DalBase {
	
	@Autowired
	private SqlHelperTemplate sqlHelper;

	public SqlHelper getSqlHelper() {
		return sqlHelper;
	}

}
