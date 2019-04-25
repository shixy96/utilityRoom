package course.dal.datas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import course.bll.HowNetWordManager;
import course.dal.bean.HowNetData;

public class HowNetNegativeInit {
	private static ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
	private static HowNetWordManager howNetWordManager = (HowNetWordManager) context.getBean("howNetWordManager");
	private static final String fileName = "src/test/resources/HowNet.txt";
	private static Logger logger = LoggerFactory.getLogger(HowNetNegativeInit.class);

	public static void main(String args[]) {
		Expand();
	}

	@SuppressWarnings("resource")
	private static void Expand() {
		File fp = new File(fileName);
		if (!fp.isFile()) {
			logger.error("文件" + fileName + "不存在！");
			return;
		}
		try {
			FileReader fr = new FileReader(fp);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			String[] arg = null;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				HowNetData howNetData = new HowNetData();
				arg = line.split("=");
				if (arg.length >= 2) {
					howNetData.setNO(Integer.parseInt(arg[1]));
				}

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					howNetData.setW_C(arg[1]);
				}

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					String[] arg1 = arg[1].split(" ");
					howNetData.setG_C(arg1[0]);
				}

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					String[] arg1 = arg[1].split("\\|");
					howNetData.setS_C(arg1[0]);
				}

				br.readLine();// EC

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					howNetData.setW_E(arg[1]);
				}

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					String[] arg1 = arg[1].split(" ");
					howNetData.setG_E(arg1[0]);
				}

				line = br.readLine();
				arg = line.split("=");
				if (arg.length >= 2) {
					String[] arg1 = arg[1].split("\\|");
					howNetData.setS_E(arg1[0]);
				}

				br.readLine();// EE

				line = br.readLine();
				int index = line.indexOf("=");
				if (index + 1 < line.length() - 1) {
					howNetData.setDEF(getDEF(line.substring(index + 1)));
				}

				line = br.readLine();// RMK

				if (howNetData.getS_C() == null || howNetData.getS_E() == null
						|| howNetData.getS_C().indexOf("Minus") == -1 && howNetData.getS_E().indexOf("Minus") == -1) {
					continue;
				}
				howNetWordManager.insert(howNetData.getW_C(), howNetData.getG_C(), howNetData.getS_C(),
						howNetData.getW_E(), howNetData.getG_E(), howNetData.getS_E(), howNetData.getDEFStr());
				System.out.println(howNetData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getDEF(String line) {
		// , = : |
		if (StringUtils.isEmpty(line)) {
			return null;
		}
		String[] splitComma = line.split(",");
		if (splitComma.length == 0) {
			return null;
		}
		List<String> splitByequle = new ArrayList<String>();
		for (String arg : splitComma) {
			String[] arg1s = arg.split("=");
			for (String arg1 : arg1s) {
				splitByequle.add(arg1);
			}
		}
		List<String> splitByColon = new ArrayList<String>();
		for (String arg : splitByequle) {
			String[] arg1s = arg.split(":");
			for (String arg1 : arg1s) {
				splitByColon.add(arg1);
			}
		}
		String result = "";
		for (String arg1 : splitByColon) {
			String[] arg2s = arg1.split("\\|");
			for (String arg2 : arg2s) {
				result += trimTrans(arg2) + ",";
			}
		}

		return result.substring(0, result.length() - 1);
	}

	private static String trimTrans(String res) {
		String result = "";
		if (res == null || res.length() == 0) {
			return result;
		}
		StringBuffer sb = new StringBuffer(res);
		while (sb.length() > 0 && sb.charAt(0) == "{".charAt(0)) {
			sb.deleteCharAt(0);
		}
		while (sb.length() > 0 && sb.charAt(sb.length() - 1) == "}".charAt(0)) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

}
