package org.designpattern.command.support;

import org.designpattern.command.Command;

public class CeilingFanMediumCommand extends CeilingFanCommand implements Command {

	public CeilingFanMediumCommand(CeilingFan ceilingFan) {
		this.ceilingFan = ceilingFan;
	}

	@Override
	public void execute() {
		preSpead = ceilingFan.getSpead();
		ceilingFan.medium();
		System.out.println("[execute] " + ceilingFan.getLocation() + " ceilingFan's spead is MEDIUM");
	}

}
