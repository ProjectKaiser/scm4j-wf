package org.scm4j.releaser;

import org.scm4j.commons.Version;
import org.scm4j.releaser.conf.Component;

import java.util.LinkedHashMap;

public class ExtendedStatusTreeNode {

	public static final ExtendedStatusTreeNode DUMMY = new ExtendedStatusTreeNode(null, null, null, null);
	
	private final Component comp;
	private final Version nextVersion;
	private final BuildStatus status;
	private final LinkedHashMap<Component, ExtendedStatusTreeNode> subComponents;

	public ExtendedStatusTreeNode(Version nextVersion, BuildStatus status,
			LinkedHashMap<Component, ExtendedStatusTreeNode> subComponents, Component comp) {
		this.nextVersion = nextVersion;
		this.status = status;
		this.subComponents = subComponents;
		this.comp = comp;
	}
	
	public Version getNextVersion() {
		return nextVersion;
	}

	public BuildStatus getStatus() {
		return status;
	}

	public LinkedHashMap<Component, ExtendedStatusTreeNode> getSubComponents() {
		return subComponents;
	}

	public Component getComp() {
		return comp;
	}
}
