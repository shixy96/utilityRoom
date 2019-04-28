package org.designpattern.command.support;

import org.designpattern.command.Command;

public class LightOffCommand implements Command {
	Light light;

	public LightOffCommand(Light light) {
		this.light = light;
	}

	@Override
	public void execute() {
		System.out.print("[execute]");
		light.off();
	}

	@Override
	public void undo() {
		System.out.print("[undo]");
		light.on();
	}

}
