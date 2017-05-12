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
		Class<T> clazz=(Class<T>) this.getClass();//��ȡ��Ӧ��ʵ�������
		Type type=clazz.getGenericSuperclass();//AnimalDaoImpl<Cat>����
		ParameterizedType para=(ParameterizedType)type;//��þ���Ĳ���������
		Type[] types=para.getActualTypeArguments();//��ȡ��������������ķ����б�
		Type target=types[0];//��ȡ��������
		targetclass=(Class)target;//ǿת
		//��ȡע��ı���
		@SuppressWarnings("unchecked")
		table t=(table) targetclass.getAnnotation(table.class);
		classname=t.name();
		//��ȡע���id����
		Field f=null;
		try {
			f=targetclass.getDeclaredField("id");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		column c=f.getAnnotation(column.class);//��ȡid�����ϵ�ע��
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
	
	//��װ�Լ���BeanListHandler��
	class MyBeanListHandler implements ResultSetHandler{

		@SuppressWarnings("unchecked")
		@Override
		public Object handle(ResultSet rs) throws SQLException {
			List<T> list=new ArrayList<T>();
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();//��ñ���е�����
			while(rs.next()){
				//����ÿһ��
				T obj=null;
				try {
					obj=(T)targetclass.newInstance();//������Ӧ�Ķ���
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				for (int i = 1; i <= count; i++) {		
					Object value=rs.getObject(i);//��õ�i�е�ֵ
					String name=metaData.getColumnName(i);//��õ�i�е�����
					Field[] field=targetclass.getDeclaredFields();//��ȡ���ϵ���������
					for (Field field2 : field) {
						column c=field2.getAnnotation(column.class);//��ȡ�����ϵ�ע��
						String myname=c.name();//��ȡע���nameֵ
						if(name.equals(myname)){
							field2.setAccessible(true);
							try {
								field2.set(obj, value);//��obj��Ӧ�����Ը�ֵ
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
	
	//��װ�Լ���BeanHandler��
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
			}//������Ӧ�Ķ���
			ResultSetMetaData metaData = rs.getMetaData();
			int count = metaData.getColumnCount();//��ñ���е�����
			if (rs.next()) {
				for (int i = 1; i <= count; i++) {
					Object value=rs.getObject(i);//��õ�i�е�ֵ
					String name=metaData.getColumnName(i);//��õ�i�е�����
					Field[] field=targetclass.getDeclaredFields();//��ȡ���ϵ���������
					for (Field field2 : field) {
						column c=field2.getAnnotation(column.class);//��ȡ�����ϵ�ע��
						String myname=c.name();//��ȡע���nameֵ
						if(name.equals(myname)){
							field2.setAccessible(true);
							try {
								field2.set(obj, value);//��obj��Ӧ�����Ը�ֵ
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
