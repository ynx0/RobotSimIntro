package simulation;

import commands.DriveForward;
import commands.DriveForwardAntiDrift;
import org.usfirst.frc.team1683.driveTrain.TankDrive;


public class Robot extends SimIterativeRobot {
	public static TankDrive driveTrain;
	private double prevEnc;
	private Command command;
	private CommandGroup action;
	
	@Override
	public void robotInit() {
		driveTrain = new TankDrive(super.getLeftSimGroup(), super.getRightSimGroup(), Robot.gyro);
	}
	
	public void autonomousInit() {
//		command = new TurnToAngle(Direction.RIGHT, 20, 0.3); /*new DriveForward(4000, 0.2);*/
		command = new DriveForwardAntiDrift(100000, 1);
		command.start();
//		action = new Square(10000, 0.2);
//		action.start();
	}
	
	@Override
	public void autonomousPeriodic() {
		Main.debug.put("Angle", Robot.gyro.getAngle());
		Command.runAllCommands();
//		CommandGroup.runAllCommands();
	}
}
