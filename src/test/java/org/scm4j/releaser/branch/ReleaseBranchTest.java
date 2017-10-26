package org.scm4j.releaser.branch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.scm4j.commons.Version;
import org.scm4j.releaser.SCMReleaser;
import org.scm4j.releaser.WorkflowTestBase;
import org.scm4j.releaser.actions.ActionKind;
import org.scm4j.releaser.actions.IAction;
import org.scm4j.releaser.conf.Component;

public class ReleaseBranchTest extends WorkflowTestBase {

	@Test
	public void testCreate() {
		ReleaseBranch rb = new ReleaseBranch(compUnTillDb);
		assertEquals(compUnTillDb, rb.getComponent());
		assertEquals(compUnTillDb.getVcsRepository().getReleaseBranchPrefix()
				+ env.getUnTillDbVer().toPreviousMinor().getReleaseNoPatchString(), rb.getName());
	}

	@Test
	public void testExists() throws Exception {
		ReleaseBranch rb = new ReleaseBranch(compUnTillDb);
		assertFalse(rb.exists());

		env.generateFeatureCommit(env.getUnTillDbVCS(), compUnTillDb.getVcsRepository().getDevelopBranch(),
				"feature added");
		SCMReleaser releaser = new SCMReleaser();
		IAction action = releaser.getActionTree(compUnTillDb);
		action.execute(getProgress(action));

		rb = new ReleaseBranch(compUnTillDb);
		assertTrue(rb.exists());
		assertEquals(compUnTillDb.getVcsRepository().getReleaseBranchPrefix()
				+ env.getUnTillDbVer().getReleaseNoPatchString(), rb.getName());
	}

	@Test
	public void testGetMDeps() throws Exception {
//		env.generateFeatureCommit(env.getUnTillDbVCS(), compUnTillDb.getVcsRepository().getDevelopBranch(), "feature added");
//		env.generateFeatureCommit(env.getUnTillVCS(), compUnTill.getVcsRepository().getDevelopBranch(), "feature added");
//		env.generateFeatureCommit(env.getUblVCS(), compUBL.getVcsRepository().getDevelopBranch(), "feature added");
		SCMReleaser releaser = new SCMReleaser();
		IAction action = releaser.getActionTree(compUnTill, ActionKind.FORK_ONLY);
		action.execute(getProgress(action));
		
		ReleaseBranch rb = new ReleaseBranch(compUnTill);
		List<Component> mDeps = rb.getMDeps();
		assertTrue(mDeps.size() == 2);
		assertTrue(mDeps.contains(compUBL.clone(env.getUblVer().toReleaseZeroPatch())));
		assertTrue(mDeps.contains(compUnTillDb.clone(env.getUnTillDbVer().toReleaseZeroPatch())));
	}

	@Test
	public void testVersionSelect() throws Exception {
		assertEquals(env.getUnTillDbVer().toPreviousMinor().toReleaseZeroPatch(), new ReleaseBranch(compUnTillDb).getVersion());
		Version testVer = new Version("11.12");
		assertEquals(testVer, new ReleaseBranch(compUnTillDb, testVer).getVersion());

		env.generateFeatureCommit(env.getUnTillDbVCS(), compUnTillDb.getVcsRepository().getDevelopBranch(), "feature added");
		SCMReleaser releaser = new SCMReleaser();
		IAction action = releaser.getActionTree(compUnTill, ActionKind.FORK_ONLY);
		assertIsGoingToForkAll(action);
		action.execute(getProgress(action));

		assertEquals(env.getUnTillDbVer().toReleaseZeroPatch(), new ReleaseBranch(compUnTillDb).getVersion());
	}
	
	@Test
	public void testToString() {
		assertNotNull(new ReleaseBranch(compUnTill).toString());
	}
}
