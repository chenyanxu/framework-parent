package com.kalix.framework.core.util;

public final class SystemProperties {

	public static final String USER_DIR = System.getProperty("user.dir");

	public static final String USER_HOME = System.getProperty("user.home");

	public static final String USER_NAME = System.getProperty("user.name");

	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	public static final String PATH_SEPARATOR = System
			.getProperty("path.separator");

	public static final String JAVA_HOME = System.getProperty("java.home");

	public static final String JAVA_VENDOR = System.getProperty("java.vendor");

	public static final String JAVA_VENDOR_URL = System
			.getProperty("java.vendor.url");

	public static final String JAVA_VERSION = System
			.getProperty("java.version");

	public static final String JAVA_CLASS_PATH = System
			.getProperty("java.class.path");

	public static final String JAVA_CLASS_VERSION = System
			.getProperty("java.class.version");

	public static final String OS_NAME = System.getProperty("os.name");

	public static final String OS_ARCH = System.getProperty("os.arch");

	public static final String OS_VERSION = System.getProperty("os.version");

	public SystemProperties() {
	}
	// java.util.Properties pp = System.getProperties();
	// // out.println(pp.toString());
	// java.util.Enumeration en = pp.propertyNames();
	// while (en.hasMoreElements()) {
	// String na=(String) en.nextElement();
	// System.out.print(na);
	// System.out.println(" value :"+pp.getProperty(na));
	// }

}
