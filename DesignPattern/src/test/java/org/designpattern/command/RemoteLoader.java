package org.designpattern.command;

import org.designpattern.command.support.CeilingFan;
import org.designpattern.command.support.CeilingFanHighCommand;
import org.designpattern.command.support.CeilingFanMediumCommand;
import org.designpattern.command.support.CeilingFanOffCommand;
import org.designpattern.command.support.Light;
import org.designpattern.command.support.LightOffCommand;
import org.designpattern.command.support.LightOnCommand;
import org.designpattern.command.support.MacroCommand;
import org.designpattern.command.support.RemoteControlWithUndo;
import org.designpattern.command.support.Stereo;
import org.designpattern.command.support.StereoOffWithCDCommand;
import org.designpattern.command.support.StereoOnWithCDCommand;

public class RemoteLoader {
	public static void main(String[] args) {
		RemoteControlWithUndo remoteControl = new RemoteControlWithUndo();

		Light livingLight = new Light("Living Room");
		Light kitchenLight = new Light("Kitchen Room");
		Stereo stereo = new Stereo("Living Room");
		CeilingFan ceilingFan = new CeilingFan("Living Room");

		LightOnCommand livingRoomLightOn = new LightOnCommand(livingLight);
		LightOffCommand livingRoomLightOff = new LightOffCommand(livingLight);
		LightOnCommand kitchenRoomLightOn = new LightOnCommand(kitchenLight);
		LightOffCommand kitchenRoomLightOff = new LightOffCommand(kitchenLight);
		StereoOnWithCDCommand stereoOnWithCDCommand = new StereoOnWithCDCommand(stereo);
		StereoOffWithCDCommand stereoOffWithCDCommand = new StereoOffWithCDCommand(stereo);
		CeilingFanHighCommand ceilingFanHighCommand = new CeilingFanHighCommand(ceilingFan);
		CeilingFanMediumCommand ceilingFanMediumCommand = new CeilingFanMediumCommand(ceilingFan);
		CeilingFanOffCommand ceilingFanOffCommand = new CeilingFanOffCommand(ceilingFan);

		Command[] partyOn = { livingRoomLightOn, kitchenRoomLightOn, stereoOnWithCDCommand };
		Command[] partyOff = { livingRoomLightOff, kitchenRoomLightOff, stereoOffWithCDCommand };
		MacroCommand partyOnMacro = new MacroCommand(partyOn);
		MacroCommand partyOffMacro = new MacroCommand(partyOff);

		remoteControl.setCommand(0, partyOnMacro, partyOffMacro);
		remoteControl.setCommand(1, ceilingFanHighCommand, ceilingFanOffCommand);
		remoteControl.setCommand(2, ceilingFanMediumCommand, ceilingFanOffCommand);

		System.out.println(remoteControl);

		remoteControl.onButtonWasPushed(0);
		remoteControl.offButtonWasPushed(0);
		remoteControl.undoButtonPushed();
		System.out.println();
		remoteControl.onButtonWasPushed(1);
		remoteControl.undoButtonPushed();
		remoteControl.offButtonWasPushed(1);
		System.out.println();
		remoteControl.onButtonWasPushed(2);
		remoteControl.offButtonWasPushed(2);
		remoteControl.undoButtonPushed();
	}
}
