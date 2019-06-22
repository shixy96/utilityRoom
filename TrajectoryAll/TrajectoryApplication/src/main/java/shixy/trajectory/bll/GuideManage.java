package shixy.trajectory.bll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import shixy.trajectory.dal.GlobalDal;

@Component
public class GuideManage {
	@Autowired
	private GlobalDal globalDal;

	public Integer getAllTableNum() {
		Integer result = null;
		try {
			result = globalDal.getAllTableNum();
		} catch (Exception e) {
			System.err.println("GuideManage getTableNum error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public List<Integer> getTablelistMember() {
		List<Integer> result = null;
		try {
			result = globalDal.listMember();
		} catch (Exception e) {
			System.err.println("GuideManage getTablelistMember error");
			System.err.println(e.getMessage());
		}
		return result;
	}

	public void insert(Integer num, String tableName) {
		try {
			globalDal.insert(num, tableName);
		} catch (Exception e) {
			System.err.println("GuideManage insert error");
			System.err.println(e.getMessage());
		}
	}

	public void update(Integer num, String tableName) {
		try {
			globalDal.update(num, tableName);
		} catch (Exception e) {
			System.err.println("GuideManage update error");
			System.err.println(e.getMessage());
		}
	}

	public void delete(Integer num) {
		try {
			globalDal.delete(num);
		} catch (Exception e) {
			System.err.println("GuideManage delete error");
			System.err.println(e.getMessage());
		}
	}

}
