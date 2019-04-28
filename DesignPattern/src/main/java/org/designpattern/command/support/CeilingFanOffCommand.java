package org.designpattern.command.support;

import org.designpattern.command.Command;

public class CeilingFanOffCommand extends CeilingFanCommand implements Command {

	public CeilingFanOffCommand(CeilingFan ceilingFan) {
		this.ceilingFan = ceilingFan;
	}

	@Override
	public void execute() {
		preSpead = ceilingFan.getSpead();
		ceilingFan.off();
		System.out.println("[execute] " + ceilingFan.getLocation() + " ceilingFan is OFF");
	}

}
