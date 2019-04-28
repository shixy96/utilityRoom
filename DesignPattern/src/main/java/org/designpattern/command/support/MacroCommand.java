package org.designpattern.command.support;

import org.designpattern.command.Command;

public class MacroCommand implements Command {
	Command[] commands;

	public MacroCommand(Command[] commands) {
		this.commands = commands;
	}

	@Override
	public void execute() {
		System.out.println("\n[macroCommand execute]");
		for (int i = 0; i < commands.length; i++) {
			commands[i].execute();
		}
	}

	@Override
	public void undo() {
		System.out.println("\n[macroCommand undo]");
		for (int i = 0; i < commands.length; i++) {
			commands[i].undo();
		}
	}

}
