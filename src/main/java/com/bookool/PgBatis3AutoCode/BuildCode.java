package com.bookool.PgBatis3AutoCode;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

public class BuildCode
{

	/**
	 * 生成 model 类
	 * 
	 * @param zt
	 */
	public static void BuildModel(mytable zt) throws IOException
	{
		StringBuilder prostrb;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		prostrb = new StringBuilder("package ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model;\r\n\r\nimport java.util.Date;\r\nimport java.math.BigDecimal;\r\n\r\n/**\r\n * ");
		prostrb.append(zt.getTableComment());
		prostrb.append("\r\n * @自动生成 https://github.com/bookool/PgBatis3AutoCode \r\n * @自动生成 ");
		prostrb.append(df.format(new Date()));
		prostrb.append("\r\n */\r\npublic class ");
		prostrb.append(zt.getTableName());
		prostrb.append("\r\n{\r\n\r\n");
		for (myfield mf : zt.getFields())
		{
			prostrb.append("	/**\r\n	 * ");
			prostrb.append(mf.getFieldComment());
			prostrb.append("\r\n	 */\r\n	private ");
			prostrb.append(mf.getFModleType());
			prostrb.append(" ");
			prostrb.append(mf.getFieldName());
			prostrb.append(";\r\n\r\n");
		}
		for (myfield mf : zt.getFields())
		{
			prostrb.append("	/**\r\n	 * 获取 ");
			prostrb.append(mf.getFieldComment());
			prostrb.append("\r\n	 */\r\n	public ");
			prostrb.append(mf.getFModleType());
			prostrb.append(" get");
			prostrb.append(mf.getFieldName());
			prostrb.append("()\r\n	{\r\n		return ");
			prostrb.append(mf.getFieldName());
			prostrb.append(";\r\n	}\r\n\r\n	/**\r\n	 * 设置 ");
			prostrb.append(mf.getFieldComment());
			prostrb.append("\r\n	 */\r\n	public void set");
			prostrb.append(mf.getFieldName());
			prostrb.append("(");
			prostrb.append(mf.getFModleType());
			prostrb.append(" ");
			prostrb.append(mf.getFieldName());
			prostrb.append(")\r\n	{\r\n		this.");
			prostrb.append(mf.getFieldName());
			prostrb.append(" = ");
			prostrb.append(mf.getFieldName());
			if (mf.getFModleType().equals("String"))
			{
				prostrb.append(" == null ? null : ");
				prostrb.append(mf.getFieldName());
				prostrb.append(".trim()");
			}
			prostrb.append(";\r\n	}\r\n\r\n");
		}
		prostrb.append("}\r\n");
		try
		{
			// FileWriter fw = new FileWriter(zt.getModelDir() + "/" +
			// zt.getTableName() + ".java", true);
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(zt.getModelDir() + "/" + zt.getTableName() + ".java", true), "UTF-8");
			fw.write(prostrb.toString());
			fw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 生成 dao 类
	 * 
	 * @param zt
	 */
	public static void BuildDao(mytable zt) throws IOException
	{
		StringBuilder prostrb;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
		myfield prif; // 找到一个字段作为主键
		if (zt.getPriFields().isEmpty())
		{
			prif = zt.getFields().get(0);
		}
		else
		{
			prif = zt.getPriFields().get(0);
		}
		prostrb = new StringBuilder("package ");
		prostrb.append(zt.getPackageName());
		prostrb.append(
				".dao;\r\n\r\nimport java.util.Date;\r\nimport java.util.List;\r\nimport java.util.Map;\r\n\r\nimport ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append(";\r\n\r\n/**\r\n * ");
		prostrb.append(zt.getTableComment());
		prostrb.append("\r\n * @自动生成 https://github.com/bookool/PgBatis3AutoCode \r\n * @自动生成 ");
		prostrb.append(df.format(new Date()));
		prostrb.append("\r\n */\r\npublic interface ");
		prostrb.append(zt.getTableName());
		prostrb.append("Mapper\r\n{\r\n\r\n	/**\r\n	 * 基础模板 取得 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 分页列表\r\n	 */\r\n	List<");
		prostrb.append(zt.getTableName());
		prostrb.append("> baseselectListPage(Map<String, Object> map);\r\n\r\n	/**\r\n	 * 基础模板 取得一个 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 对象\r\n	 */\r\n	");
		prostrb.append(zt.getTableName());
		prostrb.append(" baseselectTopOneByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n\r\n	/**\r\n	 * 基础模板 删除 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 中的数据\r\n	 */\r\n	int basedeleteByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n\r\n	/**\r\n	 * 基础模板 添加一条完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	int baseinsert(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 添加一条 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	int baseinsertSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 更新完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	int baseupdate(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 更新 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	int baseupdateSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n}\r\n");
		try
		{
			// FileWriter fw = new FileWriter(zt.getDaoDir() + "/" +
			// zt.getTableName() + "Mapper.java", true);
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(zt.getDaoDir() + "/" + zt.getTableName() + "Mapper.java", true), "UTF-8");
			fw.write(prostrb.toString());
			fw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 生成 dao 的 XML
	 * 
	 * @param zt
	 */
	public static void BuildDaoXML(mytable zt) throws IOException
	{
		StringBuilder prostrb;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
		myfield prif; // 找到一个字段作为主键
		if (zt.getPriFields().isEmpty())
		{
			prif = zt.getFields().get(0);
		}
		else
		{
			prif = zt.getPriFields().get(0);
		}
		prostrb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n");
		prostrb.append(
				"<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\r\n\r\n");
		prostrb.append("<!-- ");
		prostrb.append(zt.getTableComment());
		prostrb.append(" -->\r\n");
		prostrb.append("<mapper namespace=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".dao.");
		prostrb.append(zt.getTableName());
		prostrb.append("Mapper\" >\r\n");
		prostrb.append("	<resultMap id=\"BaseResultMap\" type=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append("\" >\r\n");
		for (myfield mf : zt.getPriFields())
		{
			prostrb.append("		<!-- ");
			prostrb.append(mf.getFieldComment());
			prostrb.append(" -->\r\n");
			prostrb.append("		<id column=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" property=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" jdbcType=\"");
			prostrb.append(mf.getFJDBCType());
			prostrb.append("\" />\r\n");
		}
		for (myfield mf : zt.getCommFields())
		{
			prostrb.append("		<!-- ");
			prostrb.append(mf.getFieldComment());
			prostrb.append(" -->\r\n");
			prostrb.append("		<result column=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" property=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" jdbcType=\"");
			prostrb.append(mf.getFJDBCType());
			prostrb.append("\" />\r\n");
		}
		prostrb.append("	</resultMap>\r\n\r\n");
		prostrb.append("	<sql id=\"Base_Column_List\" >\r\n		");
		for (int i = 0; i < zt.getFields().size(); i++)
		{
			prostrb.append('"');
			prostrb.append(zt.getFields().get(i).getFieldName());
			if (i < zt.getFields().size() - 1)
			{
				prostrb.append("\", ");
			}
			if (i == zt.getFields().size() - 1)
			{
				prostrb.append("\"\r\n");
			}
			else if ((i + 1) % 6 == 0)
			{
				prostrb.append("\r\n		");
			}
		}
		prostrb.append("	</sql>\r\n\r\n");
		prostrb.append("	<!-- 分页 -->\r\n");
		prostrb.append("	<sql id=\"Page\">\r\n");
		prostrb.append("		<if test=\"Offset!=null and Rows!=null\">\r\n");
		prostrb.append("			OFFSET #{Offset,jdbcType=INTEGER} LIMIT #{Rows,jdbcType=INTEGER}\r\n");
		prostrb.append("		</if>\r\n");
		prostrb.append("	</sql>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 取得 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 分页列表 -->\r\n");
		prostrb.append("	<select id=\"baseselectListPage\" resultMap=\"BaseResultMap\" parameterType=\"java.util.Map\" >\r\n");
		prostrb.append("		SELECT\r\n");
		prostrb.append("		<include refid=\"Base_Column_List\" />\r\n");
		prostrb.append("		FROM \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		<trim prefix=\"WHERE\" prefixOverrides=\"AND|OR\">\r\n");
		for (myfield mf : zt.getFields())
		{
			if (mf.getFModleType().equals("String"))
			{
				prostrb.append("			<if test=\"");
				prostrb.append(mf.getFieldName());
				prostrb.append("!=null and ");
				prostrb.append(mf.getFieldName());
				prostrb.append("!=''\">\r\n");
				prostrb.append("				AND \"");
				prostrb.append(mf.getFieldName());
				prostrb.append("\" LIKE CONCAT('%', #{");
				prostrb.append(mf.getFieldName());
				prostrb.append(", jdbcType=");
				prostrb.append(mf.getFJDBCType());
				prostrb.append("}, '%')\r\n");
				prostrb.append("			</if>\r\n");
			}
			else
			{
				prostrb.append("			<if test=\"");
				prostrb.append(mf.getFieldName());
				prostrb.append("!=null\">\r\n");
				prostrb.append("				AND \"");
				prostrb.append(mf.getFieldName());
				prostrb.append("\" = #{");
				prostrb.append(mf.getFieldName());
				prostrb.append(", jdbcType=");
				prostrb.append(mf.getFJDBCType());
				prostrb.append("}\r\n");
				prostrb.append("			</if>\r\n");
			}
		}
		prostrb.append("		</trim>\r\n");
		prostrb.append("		<include refid=\"Page\"></include>\r\n");
		prostrb.append("	</select>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 取得一个 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 对象 -->\r\n");
		prostrb.append("	<select id=\"baseselectTopOneByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"");
		prostrb.append(prif.getFModleTypeFull());
		prostrb.append("\" >\r\n");
		prostrb.append("		SELECT\r\n");
		prostrb.append("		<include refid=\"Base_Column_List\" />\r\n");
		prostrb.append("		FROM \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		WHERE \"");
		prostrb.append(prif.getFieldName());
		prostrb.append("\" = #{");
		prostrb.append(prif.getFieldName());
		prostrb.append(",jdbcType=");
		prostrb.append(prif.getFJDBCType());
		prostrb.append("}\r\n");
		prostrb.append("		LIMIT 0,1\r\n");
		prostrb.append("	</select>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 删除 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 中的数据 -->\r\n");
		prostrb.append("	<delete id=\"basedeleteByPrimaryKey\" parameterType=\"");
		prostrb.append(prif.getFModleTypeFull());
		prostrb.append("\" >\r\n");
		prostrb.append("		DELETE FROM \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		WHERE \"");
		prostrb.append(prif.getFieldName());
		prostrb.append("\" = #{");
		prostrb.append(prif.getFieldName());
		prostrb.append(",jdbcType=");
		prostrb.append(prif.getFJDBCType());
		prostrb.append("}\r\n");
		prostrb.append("	</delete>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 添加一条完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录 -->\r\n");
		prostrb.append("	<insert id=\"baseinsert\" parameterType=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append("\" >\r\n");
		prostrb.append("		INSERT INTO \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\" (\r\n");
		prostrb.append("			");
		for (int i = 0; i < zt.getFields().size(); i++)
		{
			prostrb.append('"');
			prostrb.append(zt.getFields().get(i).getFieldName());
			if (i < zt.getFields().size() - 1)
			{
				prostrb.append("\", ");
			}
			if (i == zt.getFields().size() - 1)
			{
				prostrb.append("\")\r\n");
			}
			else if ((i + 1) % 6 == 0)
			{
				prostrb.append("\r\n			");
			}
		}
		prostrb.append("		VALUES (\r\n");
		prostrb.append("			");
		for (int i = 0; i < zt.getFields().size(); i++)
		{
			prostrb.append("#{");
			prostrb.append(zt.getFields().get(i).getFieldName());
			prostrb.append(", jdbcType=");
			prostrb.append(zt.getFields().get(i).getFJDBCType());
			prostrb.append("}");
			if (i < zt.getFields().size() - 1)
			{
				prostrb.append(", ");
			}
			if (i == zt.getFields().size() - 1)
			{
				prostrb.append(")\r\n");
			}
			else if ((i + 1) % 2 == 0)
			{
				prostrb.append("\r\n			");
			}
		}
		prostrb.append("	</insert>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 添加一条 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录 -->\r\n");
		prostrb.append("	<insert id=\"baseinsertSelective\" parameterType=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append("\" >\r\n");
		prostrb.append("		INSERT INTO \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >\r\n");
		for (myfield mf : zt.getFields())
		{
			prostrb.append("			<if test=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("!=null\" >\r\n");
			prostrb.append("				\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\",\r\n");
			prostrb.append("			</if>\r\n");
		}
		prostrb.append("		</trim>\r\n");
		prostrb.append("		<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\" >\r\n");
		for (myfield mf : zt.getFields())
		{
			prostrb.append("			<if test=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("!=null\" >\r\n");
			prostrb.append("				#{");
			prostrb.append(mf.getFieldName());
			prostrb.append(", jdbcType=");
			prostrb.append(mf.getFJDBCType());
			prostrb.append("},\r\n");
			prostrb.append("			</if>\r\n");
		}
		prostrb.append("		</trim>\r\n");
		prostrb.append("	</insert>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 更新完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录 -->\r\n");
		prostrb.append("	<update id=\"baseupdate\" parameterType=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append("\" >\r\n");
		prostrb.append("		UPDATE \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		<set>\r\n");
		for (myfield mf : zt.getFields())
		{
			// update中，作为条件的字段不更新
			if (mf == prif)
			{
				continue;
			}
			prostrb.append("			\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" = #{");
			prostrb.append(mf.getFieldName());
			prostrb.append(",jdbcType=");
			prostrb.append(mf.getFJDBCType());
			prostrb.append("},\r\n");
		}
		prostrb.append("		</set>\r\n");
		prostrb.append("		WHERE\r\n");
		prostrb.append("			");
		prostrb.append(prif.getFieldName());
		prostrb.append(" = #{");
		prostrb.append(prif.getFieldName());
		prostrb.append(",jdbcType=");
		prostrb.append(prif.getFJDBCType());
		prostrb.append("}\r\n");
		prostrb.append("	</update>\r\n\r\n");
		prostrb.append("	<!-- 基础模板 更新 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录 -->\r\n");
		prostrb.append("	<update id=\"baseupdateSelective\" parameterType=\"");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append("\" >\r\n");
		prostrb.append("		UPDATE \"");
		prostrb.append(zt.getTableTBName());
		prostrb.append("\"\r\n");
		prostrb.append("		<set>\r\n");
		for (myfield mf : zt.getFields())
		{
			// update中，作为条件的字段不更新
			if (mf == prif)
			{
				continue;
			}
			prostrb.append("			<if test=\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("!=null\" >\r\n");
			prostrb.append("				\"");
			prostrb.append(mf.getFieldName());
			prostrb.append("\" = #{");
			prostrb.append(mf.getFieldName());
			prostrb.append(",jdbcType=");
			prostrb.append(mf.getFJDBCType());
			prostrb.append("},\r\n");
			prostrb.append("			</if>\r\n");
		}
		prostrb.append("		</set>\r\n");
		prostrb.append("		WHERE\r\n");
		prostrb.append("			\"");
		prostrb.append(prif.getFieldName());
		prostrb.append("\" = #{");
		prostrb.append(prif.getFieldName());
		prostrb.append(",jdbcType=");
		prostrb.append(prif.getFJDBCType());
		prostrb.append("}\r\n");
		prostrb.append("	</update>\r\n\r\n");
		prostrb.append("</mapper>");
		try
		{
			// FileWriter fw = new FileWriter(zt.getDaoDir() + "/" +
			// zt.getTableName() + "Mapper.xml", true);
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(zt.getDaoDir() + "/" + zt.getTableName() + "Mapper.xml", true), "UTF-8");
			fw.write(prostrb.toString());
			fw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 生成 service 类
	 * 
	 * @param zt
	 */
	public static void BuildService(mytable zt) throws IOException
	{
		StringBuilder prostrb;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
		myfield prif; // 找到一个字段作为主键
		if (zt.getPriFields().isEmpty())
		{
			prif = zt.getFields().get(0);
		}
		else
		{
			prif = zt.getPriFields().get(0);
		}
		prostrb = new StringBuilder("package ");
		prostrb.append(zt.getPackageName());
		prostrb.append(
				".service;\r\n\r\nimport java.util.Date;\r\nimport java.util.List;\r\nimport java.util.Map;\r\n\r\nimport ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append(";\r\n\r\n/**\r\n * ");
		prostrb.append(zt.getTableComment());
		prostrb.append("\r\n * @自动生成 https://github.com/bookool/PgBatis3AutoCode \r\n * @自动生成 ");
		prostrb.append(df.format(new Date()));
		prostrb.append("\r\n */\r\npublic interface ");
		prostrb.append(zt.getTableName());
		prostrb.append("Service\r\n{\r\n\r\n	/**\r\n	 * 基础模板 取得 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 分页列表\r\n	 */\r\n	public List<");
		prostrb.append(zt.getTableName());
		prostrb.append("> baseselectListPage(Map<String, Object> map);\r\n\r\n	/**\r\n	 * 基础模板 取得一个 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 对象\r\n	 */\r\n	public ");
		prostrb.append(zt.getTableName());
		prostrb.append(" baseselectTopOneByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n\r\n	/**\r\n	 * 基础模板 删除 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 中的数据\r\n	 */\r\n	public int basedeleteByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n\r\n	/**\r\n	 * 基础模板 添加一条完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	public int baseinsert(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 添加一条 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	public int baseinsertSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 更新完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	public int baseupdate(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n	/**\r\n	 * 基础模板 更新 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	public int baseupdateSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record);\r\n\r\n}\r\n");
		try
		{
			// FileWriter fw = new FileWriter(zt.getServiceDir() + "/" +
			// zt.getTableName() + "Service.java", true);
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(zt.getServiceDir() + "/" + zt.getTableName() + "Service.java", true), "UTF-8");
			fw.write(prostrb.toString());
			fw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 生成 service.impl 类
	 * 
	 * @param zt
	 */
	public static void BuildServiceImpl(mytable zt) throws IOException
	{
		StringBuilder prostrb;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置日期格式
		myfield prif; // 找到一个字段作为主键
		String mappername = zt.getTableName().toLowerCase() + "Mapper";
		if (mappername.equals(zt.getTableName().toLowerCase() + "Mapper"))
		{
			mappername = "_" + mappername;
		}
		if (zt.getPriFields().isEmpty())
		{
			prif = zt.getFields().get(0);
		}
		else
		{
			prif = zt.getPriFields().get(0);
		}
		prostrb = new StringBuilder("package ");
		prostrb.append(zt.getPackageName());
		prostrb.append(
				".service.impl;\r\n\r\nimport java.util.Date;\r\nimport java.util.List;\r\nimport java.util.Map;\r\n\r\n");
		prostrb.append(
				"import javax.annotation.Resource;\r\n\r\nimport org.springframework.beans.factory.annotation.Autowired;\r\n");
		prostrb.append("import org.springframework.stereotype.Service;\r\n\r\nimport ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".dao.");
		prostrb.append(zt.getTableName());
		prostrb.append("Mapper;\r\nimport ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".model.");
		prostrb.append(zt.getTableName());
		prostrb.append(";\r\nimport ");
		prostrb.append(zt.getPackageName());
		prostrb.append(".service.");
		prostrb.append(zt.getTableName());
		prostrb.append("Service;\r\n\r\n/**\r\n * ");
		prostrb.append(zt.getTableComment());
		prostrb.append("\r\n * @自动生成 https://github.com/bookool/PgBatis3AutoCode \r\n * @自动生成 ");
		prostrb.append(df.format(new Date()));
		prostrb.append("\r\n */\r\n@Service(\"");
		prostrb.append(zt.getTableName());
		prostrb.append("Service\")\r\npublic class ");
		prostrb.append(zt.getTableName());
		prostrb.append("ServiceImpl implements ");
		prostrb.append(zt.getTableName());
		prostrb.append("Service\r\n{\r\n\r\n	@Resource\r\n	private ");
		prostrb.append(zt.getTableName());
		prostrb.append("Mapper ");
		prostrb.append(mappername);
		prostrb.append(";\r\n\r\n	/**\r\n	 * 基础模板 取得 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 分页列表\r\n	 */\r\n	@Override\r\n	public List<");
		prostrb.append(zt.getTableName());
		prostrb.append("> baseselectListPage(Map<String, Object> map)\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseselectListPage(map);\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 取得一个 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 对象\r\n	 */\r\n	@Override\r\n	public ");
		prostrb.append(zt.getTableName());
		prostrb.append(" baseselectTopOneByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(")\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseselectTopOneByPrimaryKey(");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 删除 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 中的数据\r\n	 */\r\n	@Override\r\n	public int basedeleteByPrimaryKey(");
		prostrb.append(prif.getFModleType());
		prostrb.append(" ");
		prostrb.append(prif.getFieldName());
		prostrb.append(")\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".basedeleteByPrimaryKey(");
		prostrb.append(prif.getFieldName());
		prostrb.append(");\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 添加一条完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	@Override\r\n	public int baseinsert(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record)\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseinsert(record);\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 添加一条 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	@Override\r\n	public int baseinsertSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record)\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseinsertSelective(record);\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 更新完整的 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	@Override\r\n	public int baseupdate(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record)\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseupdate(record);\r\n	}\r\n\r\n	/**\r\n	 * 基础模板 更新 ");
		prostrb.append(zt.getTableName());
		prostrb.append(" 记录\r\n	 */\r\n	@Override\r\n	public int baseupdateSelective(");
		prostrb.append(zt.getTableName());
		prostrb.append(" record)\r\n	{\r\n		return ");
		prostrb.append(mappername);
		prostrb.append(".baseupdateSelective(record);\r\n	}\r\n\r\n}\r\n");
		try
		{
			// FileWriter fw = new FileWriter(zt.getServiceImplDir() + "/" +
			// zt.getTableName() + "ServiceImpl.java", true);
			OutputStreamWriter fw = new OutputStreamWriter(
					new FileOutputStream(zt.getServiceImplDir() + "/" + zt.getTableName() + "ServiceImpl.java", true),
					"UTF-8");
			fw.write(prostrb.toString());
			fw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

}
