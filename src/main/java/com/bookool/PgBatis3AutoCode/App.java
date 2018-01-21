package com.bookool.PgBatis3AutoCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.*;

import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonActionListener;

import java.text.SimpleDateFormat;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;

import org.apache.commons.lang3.StringUtils;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello world!");
		String LogFileName = System.getProperty("user.dir") + "/autocode.log";
		File logfile = new File(LogFileName);
		if (!logfile.exists()) {
			try {
				logfile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println(e.getMessage());
				return;
			}
		}
		logfile = null;
		System.out.println("日志文件：" + LogFileName);
		AppLog.WriteLog("Hello world!");

		if (StartPro(args)) {
			AppLog.WriteLog("结束！全部成功!\r\n");
		} else {
			AppLog.WriteLog("结束！有错误发生!\r\n", true);
		}
	}

	/**
	 * 开始自动生成
	 * 
	 * @param args
	 *            命令参数
	 * @return 是否全部成功
	 */
	private static boolean StartPro(String[] args) {
		String CusTableName = null; // 只生成针对一个表生成代码，为空表示生成所有代码
		AppLog.WriteLog("检查参数1：XML配置文件");
		if (args.length < 1 || args.length > 2) {
			AppLog.WriteLog("参数错误！", true);
			return false;
		}
		if (args.length == 2) {
			CusTableName = args[1];
		}
		AppLog.WriteLog("解析文件：" + args[0]);
		String PackageName;
		String TableNamePrefixion;
		String TableScriptDir;
		String ModelDir;
		String DaoDir;
		String ServiceDir;
		String ServiceImplDir;
		try {
			SAXReader saxReader = new SAXReader();
			Document doc = saxReader.read(new File(args[0]));
			Element zroot = doc.getRootElement();
			PackageName = VNode(zroot, "PackageName");
			TableNamePrefixion = VNode(zroot, "TableNamePrefixion", false); // TableNamePrefixion可以为空
			TableScriptDir = VNode(zroot, "TableScriptDir");
			ModelDir = VNode(zroot, "ModelDir");
			DaoDir = VNode(zroot, "DaoDir");
			ServiceDir = VNode(zroot, "ServiceDir");
			ServiceImplDir = VNode(zroot, "ServiceImplDir");
		} catch (Exception e) {
			AppLog.WriteLog("发生错误：" + e.getMessage(), true);
			return false;
		}
		File[] fs = new File(TableScriptDir).listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].getName().matches(".+(?i)\\.sql$")) {
				AppLog.WriteLog("分析数据库脚本：" + fs[i].getName());
				String tablestr = "";
				try {
					// FileReader fr = new FileReader(fs[i]);
					InputStreamReader fr = new InputStreamReader(new FileInputStream(fs[i]), "UTF-8");
					BufferedReader br = new BufferedReader(fr);
					try {
						StringBuilder rsb = new StringBuilder();
						String rline = br.readLine();
						while (rline != null) {
							rsb.append(rline.replaceAll("--\\s+.*$", ""));
							rsb.append(' ');
							rline = br.readLine();
						}
						rline = rsb.toString();
						Map<String, String> tbcomm = new HashMap<>();
						Map<String, Map<String, String>> tbcommobj = new HashMap<>();
						Matcher tczm = Pattern.compile(
								"COMMENT\\s+ON\\s+TABLE\\s+(?:\"?[^\\.\"']+\"?\\.|)\"?([^\\.\"']+)\"?\\s+IS\\s+'([^']+)'\\s*;", Pattern.CASE_INSENSITIVE)
								.matcher(rline);
						while (tczm.find()) {
							tbcomm.put(tczm.group(1), tczm.group(2));
							tbcommobj.put(tczm.group(1), new HashMap<String, String>());
						}
						tczm = null;
						tczm = Pattern.compile(
								"COMMENT\\s+ON\\s+COLUMN\\s+(?:\"?[^\\.\"']+\"?\\.|)\"?([^\\.\"']+)\"?\\.\"?([^\\.\"']+)\"?\\s+IS\\s+'([^']+)'\\s*;", Pattern.CASE_INSENSITIVE)
								.matcher(rline);
						while (tczm.find()) {
								Map<String, String> zmap;
								if (tbcommobj.containsKey(tczm.group(1))) {
									zmap = tbcommobj.get(tczm.group(1));
								} else {
									zmap = new HashMap<>();
									tbcommobj.put(tczm.group(1), zmap);
								}
								zmap.put(tczm.group(2), tczm.group(3));
						}
						tczm = null;
						tczm = Pattern.compile(
								"CREATE\\s+TABLE\\s+(?:\"?[^\"]+\"?\\.|)\"?[^\"\\(\\)]+\"?\\([^;]+;", Pattern.CASE_INSENSITIVE)
								.matcher(rline);
						while (tczm.find()) {
							tablestr = tczm.group();
							mytable zt = GetTable(tablestr, CusTableName, PackageName, TableNamePrefixion,
									TableScriptDir, ModelDir, DaoDir, ServiceDir, ServiceImplDir,
									tbcomm, tbcommobj);
							if (zt != null) {
								if (zt.getFields().isEmpty() && zt.getPriFields().isEmpty()) {
									AppLog.WriteLog("发生错误：[" + zt.getTableName() + "] 表中没有字段！", true);
								} else {
									BuildCode.BuildModel(zt);
									AppLog.WriteLog("成功生成了 model/" + zt.getTableName() + ".java 文件。");
									BuildCode.BuildDao(zt);
									AppLog.WriteLog("成功生成了 dao/" + zt.getTableName() + "Mapper.java 文件。");
									BuildCode.BuildDaoXML(zt);
									AppLog.WriteLog("成功生成了 dao/" + zt.getTableName() + "Mapper.xml 文件。");
									BuildCode.BuildService(zt);
									AppLog.WriteLog("成功生成了 service/" + zt.getTableName() + "Service.java 文件。");
									BuildCode.BuildServiceImpl(zt);
									AppLog.WriteLog(
											"成功生成了 service/impl/" + zt.getTableName() + "ServiceImpl.java 文件。");
								}
							}
						}
						br.close();
						fr.close();
					} catch (IOException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
						AppLog.WriteLog("发生错误：" + e.getMessage(), true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						AppLog.WriteLog("发生错误：" + e.getMessage(), true);
					}
				} catch (FileNotFoundException | UnsupportedEncodingException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					AppLog.WriteLog("发生错误：" + e.getMessage(), true);
				}

			}
		}
		return true;

	}
	
	private final static String TypeReg = "(smallint|integer|bigint|bit|real|double precision|numeric|character|character varying|date|time with time zone|timestamp with time zone|text|bytea)";

	/**
	 * 将脚本字符串转为table对象
	 */
	private static mytable GetTable(String TabStr, String CusTableName, String PackageName, String TableNamePrefixion,
			String TableScriptDir, String ModelDir, String DaoDir, String ServiceDir, String ServiceImplDir,
			Map<String, String> tbcomm, Map<String, Map<String, String>> tbcommobj)
			throws Exception {
		Matcher zm = Pattern
				.compile("CREATE\\s+TABLE\\s+(?:\"?[^\"]+\"?\\.|)\"?" + TableNamePrefixion
						+ "([^\"\\(\\)]+)\"?(\\([^;]+)\\s*;", Pattern.CASE_INSENSITIVE)
				.matcher(TabStr);
		if (zm.find()) {
			String TableName = zm.group(1).trim();
			String TableComment = tbcomm.get(TableNamePrefixion + TableName);
			if (CusTableName != null) {
				if (!CusTableName.equals(TableName)) {
					AppLog.WriteLog("找到数据表：" + TableName + " ，并非指定，不生成。");
					return null;
				}
			}
			AppLog.WriteLog("找到数据表：" + TableName);
			boolean fe = false;
			File tfile = new File(ModelDir + "/" + TableName + ".java");
			if (tfile.exists()) {
				AppLog.WriteLog(tfile.getAbsolutePath() + " 已经存在。", true);
				fe = true;
			}
			tfile = new File(DaoDir + "/" + TableName + "Mapper.java");
			if (tfile.exists()) {
				AppLog.WriteLog(tfile.getAbsolutePath() + " 已经存在。", true);
				fe = true;
			}
			tfile = new File(DaoDir + "/" + TableName + "Mapper.xml");
			if (tfile.exists()) {
				AppLog.WriteLog(tfile.getAbsolutePath() + " 已经存在。", true);
				fe = true;
			}
			tfile = new File(ServiceDir + "/" + TableName + "Service.java");
			if (tfile.exists()) {
				AppLog.WriteLog(tfile.getAbsolutePath() + " 已经存在。", true);
				fe = true;
			}
			tfile = new File(ServiceImplDir + "/" + TableName + "ServiceImpl.java");
			if (tfile.exists()) {
				AppLog.WriteLog(tfile.getAbsolutePath() + " 已经存在。", true);
				fe = true;
			}
			if (fe) {
				AppLog.WriteLog("代码已经存在，" + TableName + " 不再生成。", true);
				return null;
			} else {
				String TableCon = zm.group(2).trim();
				List<String> prilist = new ArrayList<String>();
				zm = Pattern.compile(".*PRIMARY\\s+KEY\\s*\\(([^\\(\\)]+)\\).*").matcher(TableCon);
				if (zm.find()) {
					String[] pk = zm.group(1).split(",");
					for (String zpk : pk) {
						String zz = zpk.replace("\"", "").trim();
						prilist.add(zz);
					}
				}
				mytable zt = new mytable();
				zt.setPackageName(PackageName);
				zt.setTableNamePrefixion(TableNamePrefixion);
				zt.setTableScriptDir(TableScriptDir);
				zt.setModelDir(ModelDir);
				zt.setDaoDir(DaoDir);
				zt.setServiceDir(ServiceDir);
				zt.setServiceImplDir(ServiceImplDir);
				zt.setTableName(TableName);
				zt.setTableComment(TableComment);
				zt.setFields(new ArrayList<myfield>());
				zt.setPriFields(new ArrayList<myfield>());
				zt.setCommFields(new ArrayList<myfield>());
				zm = Pattern
						.compile(
								"\\s*\"?([^\"]*)\"?\\s+" + TypeReg + "(\\(\\d?\\)|)\\s*([^,\\)]*)(?:,|\\))")
						.matcher(TableCon);
				while (zm.find()) {
					TableCon = zm.group(0).trim();
					myfield zf = new myfield();
					zf.setFieldName(zm.group(1).trim());
					zf.setFDBType(zm.group(2).trim().toLowerCase());
					zf.setFieldComment(tbcommobj.get(TableNamePrefixion + TableName).get(zm.group(1).trim()));
					if (zm.group(3).matches(".*NOT\\s*NULL.*")) {
						zf.setFieldNotNull(true);
					} else {
						zf.setFieldNotNull(false);
					}
					boolean ispri = false;
					for (String pstr : prilist) {
						if (pstr.equals(zf.getFieldName())) {
							ispri = true;
							break;
						}
					}
					if (ispri) {
						zt.getPriFields().add(zf);
					} else {
						zt.getCommFields().add(zf);
					}
					zt.getFields().add(zf);
				}
				return zt;
			}
		} else {
			AppLog.WriteLog("发生错误：没有找到表数据！", true);
			return null;
		}
	}

	/**
	 * 检查配置节点是否为空，配置节点为目录的检查目录是否存在
	 */
	private static String VNode(Element zroot, String NodeName) throws Exception {
		return VNode(zroot, NodeName, true);
	}

	/**
	 * 检查配置节点是否为空，配置节点为目录的检查目录是否存在
	 */
	private static String VNode(Element zroot, String NodeName, Boolean NotBlank) throws Exception {
		Element nownode = zroot.element(NodeName);
		String StPr = nownode.getText().trim();
		if (NotBlank && StringUtils.isBlank(StPr)) {
			throw new Exception(NodeName + " 为空！");
		} else {
			if (nownode.attribute("ConType") != null) {
				if ("dir".equals(nownode.attribute("ConType").getValue())) {
					File prodir = new File(StPr);
					if (!prodir.exists()) {
						throw new Exception(NodeName + "：" + StPr + " 目录不存在！");
					}
				}
			}
			AppLog.WriteLog(NodeName + "： " + StPr);
			return StPr;
		}
	}

}
