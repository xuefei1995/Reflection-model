package com.xuefei.daoImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;


import com.xuefei.util.JdbcUtil;

public class AnimalDaoImpl<T> {
	
	@SuppressWarnings("rawtypes")
	private Class targetclass;
	private String classname;
	
	@SuppressWarnings("rawtypes")
	public AnimalDaoImpl(){
		@SuppressWarnings("unchecked")
		Class<T> clazz=(Class<T>) this.getClass();//获取对应的实现类对象
		Type type=clazz.getGenericSuperclass();//AnimalDaoImpl<Cat>类型
		ParameterizedType para=(ParameterizedType)type;//获得具体的参数化类型
		Type[] types=para.getActualTypeArguments();//获取参数化类型上面的泛型列表
		Type target=types[0];//获取泛型类型
		targetclass=(Class)target;//强转
		classname=targetclass.getSimpleName();		
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		QueryRunner qr=new QueryRunner(JdbcUtil.getDataSource());
		String sql="select * from "+classname;
		List<T> list=null;
		try {
			list=(List<T>)qr.query(sql, new BeanListHandler(targetclass));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	public T findById(int id) {
		QueryRunner qr=new QueryRunner(JdbcUtil.getDataSource());
		String sql="select * from "+classname+" where id=?";
		T t=null;
		try {
			t= (T)qr.query(sql, new BeanHandler(targetclass), new Object[]{id});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}
}
