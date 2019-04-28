package org.designpattern.command.support;

import org.designpattern.command.Command;

public class RemoteControlWithUndo {
	private static final int controlNum = 7;
	Command[] onCommands;
	Command[] offCommands;
	Command undoCommand;

	public RemoteControlWithUndo() {
		onCommands = new Command[controlNum];
		offCommands = new Command[controlNum];

		Command noCommand = new NoCommand();
		for (int i = 0; i < controlNum; i++) {
			onCommands[i] = noCommand;
			offCommands[i] = noCommand;
		}
		undoCommand = noCommand;
	}

	public void setCommand(int slot, Command onCommand, Command offCommand) {
		onCommands[slot] = onCommand;
		offCommands[slot] = offCommand;
	}

	public void onButtonWasPushed(int slot) {
		onCommands[slot].execute();
		undoCommand = onCommands[slot];
	}

	public void offButtonWasPushed(int slot) {
		offCommands[slot].execute();
		undoCommand = offCommands[slot];
	}

	public void undoButtonPushed() {
		undoCommand.undo();
	}

	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\n------ Remote Control ------\n");
		for (int i = 0, l = onCommands.length; i < l; i++) {
			stringBuffer.append("[slot " + i + "] " + onCommands[i].getClass().getName() + "    "
					+ offCommands[i].getClass().getName() + "\n");
		}
		stringBuffer.append("[undo] " + undoCommand.getClass().getName() + "\n");
		return stringBuffer.toString();
	}
}
