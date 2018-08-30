package commands;

import org.usfirst.frc.team1683.sensors.Gyro;
import simulation.Main;
import simulation.Robot;

public class DriveForwardAntiDrift extends DriveForward {
    private static final double ANGLE_THRESHOLD = 0.5d; // in reality 0.5 degrees is much to harsh, it should be somewhere around 20 at least
    private static final double DAMPENING_FACTOR = 0.7;
    private final Gyro gyro;
    private double speed;
    private double lossSum = 0;
    private double cycles = 0;
    private double averageError = 0;


    public DriveForwardAntiDrift(double distanceToTravel, double speed) {
        super(distanceToTravel, speed);
        requires(Robot.driveTrain);
        this.speed = normalizeSpeed(speed);
        this.gyro = Robot.gyro;
    }


    @Override
    protected void execute() {
        double error = Math.abs(Robot.gyro.getAngle());
        lossSum += error;
        averageError = lossSum/(++cycles);
        Main.debug.put("Average Err:", averageError);
        System.out.println("Distance is: " + Robot.driveTrain.getLeftEncoder().getDistance());
        if (gyro.getAngle() >= ANGLE_THRESHOLD) {
            Robot.driveTrain.set(speed * -DAMPENING_FACTOR, speed * DAMPENING_FACTOR);
        } else if (gyro.getAngle() <= -ANGLE_THRESHOLD) {
            Robot.driveTrain.set(speed * DAMPENING_FACTOR, speed * -DAMPENING_FACTOR);
        } else {
            Robot.driveTrain.set(speed, speed);
        }

    }

}
