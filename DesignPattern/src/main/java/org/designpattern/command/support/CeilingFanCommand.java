package org.designpattern.command.support;

import org.designpattern.command.Command;

public abstract class CeilingFanCommand implements Command {
	protected CeilingFan ceilingFan;
	protected int preSpead;

	@Override
	public void undo() {
		String speadstr = "";
		switch (preSpead) {
		case CeilingFan.HIGH:
			ceilingFan.high();
			speadstr = "HIGH";
			break;
		case CeilingFan.MEDIUM:
			ceilingFan.medium();
			speadstr = "MEDIUM";
			break;
		case CeilingFan.LOW:
			ceilingFan.low();
			speadstr = "LOW";
			break;
		case CeilingFan.OFF:
			ceilingFan.off();
			speadstr = "OFF";
			break;
		default:
			break;
		}
		System.out.println("[undo] " + ceilingFan.getLocation() + " ceilingFan's spead is " + speadstr);
	}

}
