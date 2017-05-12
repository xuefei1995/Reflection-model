package com.xuefei.daoImpl;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.xuefei.annotation.column;
import com.xuefei.annotation.table;
import com.xuefei.util.JdbcUtil;

public class AnimalDaoImpl<T> {
	
	@SuppressWarnings("rawtypes")
	private Class targetclass;
	private String classname;
	private String idname;
	
	@SuppressWarnings("rawtypes")
	public AnimalDaoImpl(){
		@SuppressWarnings("unchecked")
		Class<T> clazz=(Class<T>) this.getClass();//获取对应的实现类对象
		Type type=clazz.getGenericSuperclass();//AnimalDaoImpl<Cat>类型
		ParameterizedType para=(ParameterizedType)type;//获得具体的参数化类型
		Type[] types=para.getActualTypeArguments();//获取参数化类型上面的泛型列表
		Type target=types[0];//获取泛型类型
		targetclass=(Class)target;//强转
		//获取注解的表名
		@SuppressWarnings("unchecked")
		table t=(table) targetclass.getAnnotation(table.class);
		classname=t.name();
		//获取注解的id名字
		Field f=null;
		try {
			f=targetclass.getDeclaredField("id");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		column c=f.getAnnotation(column.class);//获取id属性上的注解
		idname=c.name();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		QueryRunner qr=new QueryRunner(JdbcUtil.getDataSource());
		String sql="select * from "+classname;
		List<T> list=null;
		try {
			list=(List<T>)qr.query(sql, new MyBeanListHandler());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public T findById(int id) {
		QueryRunner qr=new QueryRunner(JdbcUtil.getDataSource());
		String sql="select * from "+classname+" where "+idname+"=?";
		T t=null;
		try {
			t= (T)qr.query(sql, new MyBeanHandler(), new Object[]{id});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
	
	//封装自己的BeanListHandler类
	class MyBeanListHandler implements ResultSetHandler{

		@SuppressWarnings("unchecked")
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			List<T> list=new ArrayList<T>();
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();//获得表的列的数量
			while(rs.next()){
				//遍历每一列
				T obj=null;
				try {
					obj=(T)targetclass.newInstance();//创建对应的对象
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				for (int i = 1; i <= count; i++) {		
					Object value=rs.getObject(i);//获得第i列的值
					String name=metaData.getColumnName(i);//获得第i列的名称
					Field[] field=targetclass.getDeclaredFields();//获取类上的所有属性
					for (Field field2 : field) {
						column c=field2.getAnnotation(column.class);//获取属性上的注解
						String myname=c.name();//获取注解的name值
						if(name.equals(myname)){
							field2.setAccessible(true);
							try {
								field2.set(obj, value);//给obj对应的属性赋值
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
				list.add(obj);
			}
			return list;
		}
	}
	
	//封装自己的BeanHandler类
	class MyBeanHandler implements ResultSetHandler{

		@SuppressWarnings("unchecked")
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			T obj=null;
			try {
				obj=(T)targetclass.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}//创建对应的对象
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();//获得表的列的数量
			if (rs.next()) {
				for (int i = 1; i <= count; i++) {
					Object value=rs.getObject(i);//获得第i列的值
					String name=metaData.getColumnName(i);//获得第i列的名称
					Field[] field=targetclass.getDeclaredFields();//获取类上的所有属性
					for (Field field2 : field) {
						column c=field2.getAnnotation(column.class);//获取属性上的注解
						String myname=c.name();//获取注解的name值
						if(name.equals(myname)){
							field2.setAccessible(true);
							try {
								field2.set(obj, value);//给obj对应的属性赋值
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							}
							break;
						}
					}
				}
			}
			return obj;
		}
	}
	
}
