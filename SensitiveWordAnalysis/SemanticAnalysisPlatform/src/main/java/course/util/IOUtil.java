package course.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class IOUtil {
	public static void output_to_file(String content, String filename, boolean append) {
		if (content != null) {
			BufferedWriter fp_save = null;
			try {
				fp_save = new BufferedWriter(new FileWriter(filename, append));
				fp_save.write(content.toString());
				fp_save.close();
			} catch (IOException e) {
				System.err.println("can't open file " + filename + "\n" + e);
				System.exit(1);
			} finally {
				if (fp_save != null) {
					try {
						fp_save.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
