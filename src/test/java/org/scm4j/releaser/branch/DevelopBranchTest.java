package org.scm4j.releaser.branch;

import org.junit.Test;
import org.scm4j.releaser.TestEnvironment;
import org.scm4j.releaser.Utils;
import org.scm4j.releaser.conf.Component;
import org.scm4j.releaser.exceptions.EComponentConfig;
import org.scm4j.vcs.api.IVCS;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class DevelopBranchTest {
	
	@Test
	public void testIsNotModifiedIfNoCommits() throws Exception {
		try (TestEnvironment env = new TestEnvironment()) {
			env.generateTestEnvironmentNoVCS();
			Component mockedComp = spy(new Component(TestEnvironment.PRODUCT_UNTILL, env.getRepoFactory()));
			IVCS mockedVCS = mock(IVCS.class);
			doReturn(mockedVCS).when(mockedComp).getVCS();
			doReturn(new ArrayList<>()).when(mockedVCS).log(anyString(), anyInt());
			DevelopBranch db = new DevelopBranch(mockedComp);
			assertFalse(db.isModified());
		}
	}
	
	@Test
	public void testGetVersionIfNoVersionFile() throws Exception {
		try (TestEnvironment env = new TestEnvironment()) {
			env.generateTestEnvironment();
			Component comp = new Component(TestEnvironment.PRODUCT_UNTILL, env.getRepoFactory());
			env.getUnTillVCS().removeFile(comp.getVcsRepository().getDevelopBranch(), Utils.VER_FILE_NAME, "version file removed");
			DevelopBranch db = new DevelopBranch(comp);
			try {
				db.getVersion();
				fail();
			} catch (EComponentConfig e) {
				
			}
		}
	}
}
