package commands;

import simulation.Command;
import simulation.Main;
import simulation.Robot;

public class DriveForward extends Command {
    private double speed;
    private double distanceToTravel;
    private double lossSum = 0;
    private double cycles = 0;
    private double averageError = 0;

    public DriveForward(double distanceToTravel, double speed) {
        requires(Robot.driveTrain);
        this.distanceToTravel = distanceToTravel;
        this.speed = normalizeSpeed(speed);
    }

    private DriveForward() {
    }


    @Override
    protected void initialize() {
        Robot.driveTrain.getRightEncoder().reset();
        Robot.driveTrain.getLeftEncoder().reset();
    }

    @Override
    protected void execute() {
        double error = Math.abs(Robot.gyro.getAngle());
        lossSum += error;
        averageError = lossSum/(++cycles);
        Main.debug.put("Average Err:", averageError);
        System.out.println("Distance is: " + Robot.driveTrain.getLeftEncoder().getDistance());
        Robot.driveTrain.set(speed, speed);
    }

    @Override
    protected boolean isFinished() {
        return Robot.driveTrain.getLeftEncoder().getDistance() >= distanceToTravel;
    }

    @Override
    protected void end() {
        Robot.driveTrain.stop();
    }

    protected double normalizeSpeed(double rawSpeed) {
        if (speed > 1.0 || speed < 0.0) {
            throw new IllegalArgumentException("Cannot have a speed that is less than 0 or greater than 1");
        } else {
            return rawSpeed;
        }
    }
}
