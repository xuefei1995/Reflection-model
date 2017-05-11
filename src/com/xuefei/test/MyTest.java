package com.xuefei.test;

import java.util.List;

import org.junit.Test;






import com.xuefei.daoImpl.CatDaoImpl;
import com.xuefei.daoImpl.DogDaoImpl;
import com.xuefei.entity.Cat;
import com.xuefei.entity.Dog;

public class MyTest {
	@Test
	public void testCat(){
		CatDaoImpl cat=new CatDaoImpl();
		List<Cat> find = cat.findAll();
		for (Cat cat2 : find) {
			System.out.println(cat2);
		}
	}
	
	@Test
	public void testDog(){
		DogDaoImpl dog=new DogDaoImpl();
		Dog findById = dog.findById(2);
		System.out.println(findById);
	}
	
}
