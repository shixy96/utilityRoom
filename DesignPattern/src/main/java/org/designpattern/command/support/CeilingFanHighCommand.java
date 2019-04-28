package org.designpattern.command.support;

import org.designpattern.command.Command;

public class CeilingFanHighCommand extends CeilingFanCommand implements Command {

	public CeilingFanHighCommand(CeilingFan ceilingFan) {
		this.ceilingFan = ceilingFan;
	}

	@Override
	public void execute() {
		preSpead = ceilingFan.getSpead();
		ceilingFan.high();
		System.out.println("[execute] " + ceilingFan.getLocation() + " ceilingFan's spead is HIGH");

	}

}
