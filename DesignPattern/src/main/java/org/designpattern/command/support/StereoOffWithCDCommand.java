package org.designpattern.command.support;

import org.designpattern.command.Command;

public class StereoOffWithCDCommand implements Command {
	Stereo stereo;

	public StereoOffWithCDCommand(Stereo stereo) {
		this.stereo = stereo;
	}

	@Override
	public void execute() {
		System.out.print("[execute]");
		stereo.moveCD();
		System.out.print("[execute]");
		stereo.off();
	}

	@Override
	public void undo() {
		System.out.print("[undo]");
		stereo.on();
		System.out.print("[undo]");
		stereo.setCD();
		System.out.print("[undo]");
		stereo.setVolume(11);
	}

}
