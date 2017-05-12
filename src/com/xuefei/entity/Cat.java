package com.xuefei.entity;

import com.xuefei.annotation.column;
import com.xuefei.annotation.table;

@table(name = "cat_list")
public class Cat {
	@column(name = "id_list")
	private int id;
	@column(name = "name_list")
	private String name;
	@column(name = "age_list")
	private int age;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	@Override
	public String toString() {
		return "Cat [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
	public void setAge(int age) {
		this.age = age;
	}
}
