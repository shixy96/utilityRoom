package org.designpattern.command.support;

import org.designpattern.command.Command;

public class LightOnCommand implements Command {
	Light light;

	public LightOnCommand(Light light) {
		this.light = light;
	}

	@Override
	public void execute() {
		System.out.print("[execute]");
		light.on();
	}

	@Override
	public void undo() {
		System.out.print("[undo]");
		light.off();
	}

}
