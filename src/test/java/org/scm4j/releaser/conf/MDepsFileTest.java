package org.scm4j.releaser.conf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.scm4j.releaser.TestEnvironment;

public class MDepsFileTest {
	
	@Test
	public void test() {
		assertFalse(new MDepsFile("").hasMDeps());
		assertFalse(new MDepsFile((String) null).hasMDeps());
		assertFalse(new MDepsFile((List<Component>) null).hasMDeps());
		
		assertTrue(new MDepsFile("").getMDeps().isEmpty());
		assertTrue(new MDepsFile((String) null).getMDeps().isEmpty());
		assertTrue(new MDepsFile((List<Component>) null).getMDeps().isEmpty());
		
		assertTrue(new MDepsFile("").toFileContent().isEmpty());
		assertTrue(new MDepsFile((String) null).toFileContent().isEmpty());
		assertTrue(new MDepsFile((List<Component>) null).toFileContent().isEmpty());
		
		String content = "\r\n"
				+ "        # my cool comment \r\n\r\n"
				+ "    " + TestEnvironment.PRODUCT_UNTILL + " # product comment \r\n"
				+ "  \r\n";
		MDepsFile mdf = new MDepsFile(content);
		assertTrue(mdf.hasMDeps());
		List<Component> mDeps = mdf.getMDeps();
		assertTrue(mDeps.size() == 1);
		assertEquals(new Component(TestEnvironment.PRODUCT_UNTILL), mDeps.get(0));
		
		Component initialComp = new Component("    " + TestEnvironment.PRODUCT_UNTILL + " # product comment ");
		mdf.replaceMDep(initialComp.clone("12.13.14"));
		
		assertEquals(content.replace(TestEnvironment.PRODUCT_UNTILL, TestEnvironment.PRODUCT_UNTILL + ":12.13.14"), mdf.toFileContent());
		
		
		
		
	}

}