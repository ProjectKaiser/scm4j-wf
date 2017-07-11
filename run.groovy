package org.scm4j.wf;

@GrabResolver(name = 'jitpack', root = 'https://jitpack.io')
@Grab('com.github.scm4j:scm4j-wf:dev-SNAPSHOT')

import org.scm4j.wf.SCMWorkflow;
import org.scm4j.wf.actions.IAction;
import org.scm4j.wf.actions.PrintAction;

class CLI {

	String server
	boolean debug

	static void main(args) {

		def cli = new CliBuilder(usage: 'groovy run.groovy -show|-build|-tag productCoords')

		cli.show('show actions will be made with product specified by productCoords', required: false, args: 1, type: String)
		cli.build('execute production release action on product specified by productCoords', required: false, args: 1, type: String)
		cli.tag('execute tag action on product specified by productCoords', required: false, args: 1, type: String)

		OptionAccessor opt = cli.parse(args)
		if(!opt) {
			return
		}
		
		if (opt.show) {
			SCMWorkflow wf = new SCMWorkflow(opt.show)
			IAction action = wf.getProductionReleaseAction(null);
			PrintAction pa = new PrintAction();
			pa.print(System.out, action);
		}
	}
}
