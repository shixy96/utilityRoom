package org.designpattern.command.support;

import org.designpattern.command.Command;

public class StereoOnWithCDCommand implements Command {
	Stereo stereo;

	public StereoOnWithCDCommand(Stereo stereo) {
		this.stereo = stereo;
	}

	@Override
	public void execute() {
		System.out.print("[execute]");
		stereo.on();
		System.out.print("[execute]");
		stereo.setCD();
		System.out.print("[execute]");
		stereo.setVolume(11);
	}

	@Override
	public void undo() {
		System.out.print("[undo]");
		stereo.moveCD();
		System.out.print("[undo]");
		stereo.off();
	}

}
