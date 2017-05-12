package com.xuefei.entity;

import com.xuefei.annotation.column;
import com.xuefei.annotation.table;

@table(name = "dog_list")
public class Dog {
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
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Dog [id=" + id + ", name=" + name + ", age=" + age + "]";
	}
}
