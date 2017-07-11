package org.scm4j.wf

@GrabResolver(name = 'jitpack', root = 'https://jitpack.io')
@Grab('com.github.scm4j:scm4j-wf:master-SNAPSHOT')
import org.scm4j.wf.*
import org.scm4j.wf.actions.*

class CLI {

	String server
	boolean debug

	static void main(args) {

		def cli = new CliBuilder(usage: 'groovy run.groovy show|build|tag productName')

		cli.show('show actions will be made with product productName', required: false, args: 1, type: String)
		cli.build('execute production release action on product productName', required: false, args: 1, type: String)
		cli.tag('execute tag action on product productName', required: false, args: 1, type: String)

		OptionAccessor opt = cli.parse(args)
		if(!opt) {
			return
		}
		// print usage if -h, --help, or no argument is given
		if (opt.show) {
			SCMWorkflow wf = new SCMWorkflow(opt.show)
			IAction action = wf.getProductionReleaseAction(null);
			PrintAction pa = new PrintAction();
			pa.print(System.out, action);
		}
	}
}
