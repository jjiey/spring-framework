package org.thinking.in.spring.xl;

public class Cat implements Animal {

	@Override
	public void eat() {
		System.out.println("cat eat！");
	}

	@Override
	public void run() {
		System.out.println("cat run！");
	}
}
