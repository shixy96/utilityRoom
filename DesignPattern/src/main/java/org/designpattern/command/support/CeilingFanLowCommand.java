package org.designpattern.command.support;

import org.designpattern.command.Command;

public class CeilingFanLowCommand extends CeilingFanCommand implements Command {

	public CeilingFanLowCommand(CeilingFan ceilingFan) {
		this.ceilingFan = ceilingFan;
	}

	@Override
	public void execute() {
		preSpead = ceilingFan.getSpead();
		ceilingFan.low();
		System.out.println("[execute] " + ceilingFan.getLocation() + " ceilingFan's spead is LOW");
	}

}
