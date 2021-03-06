package org.scm4j.releaser.scmactions;

import java.util.ArrayList;
import java.util.List;

import org.scm4j.commons.Version;
import org.scm4j.commons.progress.IProgress;
import org.scm4j.releaser.BuildStatus;
import org.scm4j.releaser.CachedStatuses;
import org.scm4j.releaser.ExtendedStatus;
import org.scm4j.releaser.Utils;
import org.scm4j.releaser.actions.ActionAbstract;
import org.scm4j.releaser.actions.ActionSet;
import org.scm4j.releaser.actions.IAction;
import org.scm4j.releaser.conf.Component;
import org.scm4j.releaser.conf.VCSRepository;
import org.scm4j.releaser.conf.VCSRepositoryFactory;
import org.scm4j.releaser.scmactions.procs.ISCMProc;
import org.scm4j.releaser.scmactions.procs.SCMProcActualizePatches;
import org.scm4j.releaser.scmactions.procs.SCMProcBuild;
import org.scm4j.releaser.scmactions.procs.SCMProcForkBranch;
import org.scm4j.releaser.scmactions.procs.SCMProcLockMDeps;
import org.scm4j.vcs.api.VCSChangeListNode;

public class SCMActionRelease extends ActionAbstract {

	private final List<ISCMProc> procs = new ArrayList<>();
	private final BuildStatus bsFrom;
	private final BuildStatus bsTo;
	private final Version targetVersion;
	private final boolean delayedTag;

	public SCMActionRelease(Component comp, List<IAction> childActions, CachedStatuses cache, VCSRepositoryFactory repoFactory,
							ActionSet actionSet, boolean delayedTag, VCSRepository repo) {
		super(comp, childActions, repo);
		ExtendedStatus status = cache.get(repo.getUrl());
		bsFrom = status.getStatus();
		targetVersion = status.getNextVersion();
		List<VCSChangeListNode> vcsChangeList = new ArrayList<>();
		BuildStatus bsTo = null;
		this.delayedTag = delayedTag;
		switch(bsFrom) {
			case FORK:
				procs.add(new SCMProcForkBranch(comp, cache, repo, vcsChangeList));
			case LOCK:
				procs.add(new SCMProcLockMDeps(cache, repoFactory, repo, vcsChangeList));
				bsTo = BuildStatus.LOCK;
				if (actionSet == ActionSet.FORK_ONLY) {
					break;
				}
			case BUILD_MDEPS:
			case ACTUALIZE_PATCHES:
				if (bsFrom.ordinal() > BuildStatus.LOCK.ordinal() && actionSet == ActionSet.FULL) {
					procs.add(new SCMProcActualizePatches(cache, repoFactory, repo));
				}
			case BUILD:
				if (actionSet == ActionSet.FULL) {
					procs.add(new SCMProcBuild(comp, cache, delayedTag, repo));
					bsTo = BuildStatus.BUILD;
				}
			case DONE:
		}
		this.bsTo = bsTo;
	}

	@Override
	protected void executeAction(IProgress progress) {
		for (ISCMProc proc : procs) {
			proc.execute(progress);
		}
	}

	@Override
	public String toStringAction() {
		return getDescription(getDetailedStatus());
	}

	private String getDescription(String status) {
		return String.format("%s %s, target version: %s, target branch: %s", status, comp.getCoords(), targetVersion,
				Utils.getReleaseBranchName(repo, targetVersion));
	}

	private String getDetailedStatus() {
		String skipStr = procs.isEmpty() && bsFrom != BuildStatus.DONE ? "skip " : "";
		String bsToStr = bsTo != null && bsTo != bsFrom ? " -> " + bsTo : "";
		return skipStr + getSimpleStatus() + bsToStr;
	}

	@Override
	public String toString() {
		return getDescription(getSimpleStatus());
	}

	private String getSimpleStatus() {
		return bsFrom.toString();
	}

	public BuildStatus getBsFrom() {
		return bsFrom;
	}

	public BuildStatus getBsTo() {
		return bsTo;
	}

	public List<ISCMProc> getProcs() {
		return procs;
	}

	@Override
	public boolean isExecutable() {
		return bsFrom != BuildStatus.DONE;
	}
	
	public boolean isDelayedTag() {
		return delayedTag;
	}
}
