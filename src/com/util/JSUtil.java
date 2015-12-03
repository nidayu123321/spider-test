package com.util;


import org.apache.commons.io.IOUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JSUtil {
	
	private static final Map<String, String> cacheJsFile = new ConcurrentHashMap<String, String>();

	public static String executeJsFunc(String jsFile, String jsFuncName,  Object... args) {
	    return executeJsFunc1(jsFuncName, false, jsFile, args);
	}
	public static String executeResourceJsFunc(String jsFile,String jsFuncName,  Object... args) {
	    return executeJsFunc1(jsFuncName, true, jsFile, args);
	}
	private static Map<String, ScriptEngine> engineMap = new ConcurrentHashMap<>();
	private static String executeJsFunc1(String jsFuncName, boolean classPathRes, String jsFiles, Object... args) {
	    ScriptEngineManager manager = new ScriptEngineManager();
	    ScriptEngine engine = null;
		manager.getEngineByExtension("js");
	    FileReader reader = null;
		InputStream is = null;
	    try {
			engine = engineMap.get(jsFiles);
			if (engine == null) {
				engine = manager.getEngineByExtension("js");
				String[] jsFileArr = jsFiles.split(",");
				for (String jsFile : jsFileArr) {
					String js = cacheJsFile.get(jsFile);
					if (js == null) {
						if (classPathRes) {
							is = JSUtil.class.getResourceAsStream(jsFile);
							js = IOUtils.toString(is, "utf-8");
						} else {
							reader = new FileReader(jsFile);
							js = IOUtils.toString(reader);
						}
						cacheJsFile.put(jsFile, js);
					}
					engine.eval(js);
				}
				engineMap.put(jsFiles, engine);
			}
	        if(engine instanceof Invocable) {
	            Invocable invoke = (Invocable)engine;
	            Object c = invoke.invokeFunction(jsFuncName, args);
	            return c.toString();
	        }
	    } catch (Exception e1) {
			e1.printStackTrace();
	    } finally {
	        IOUtils.closeQuietly(is);
	        IOUtils.closeQuietly(reader);
	    }
	    return null;
	}
	private static String evalJs(String eval, String... js) {
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByExtension("js");   
		FileReader reader = null;
		try {
			// 执行指定脚本
			for(String j : js) {
				engine.eval(j);
			}
			Object obj = engine.eval(eval);
			//System.out.println(engine.get("Two4TwoSeven"));
			return obj.toString();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return null;
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(executeJsFunc("C:/D/myself/spider-test/WebContent/WEB-INF/js/renren.js",
				"base64_encode", "Chenxu001"));
	}

}
