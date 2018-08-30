package commands;

import org.omg.PortableInterceptor.DISCARDING;
import simulation.Command;
import simulation.Robot;
import util.Direction;

public class TurnToAngle extends Command {

    private final Direction direction;
    private final double angle;
    private final double speed;

    public TurnToAngle(Direction direction, double angle, double speed) {
        requires(Robot.driveTrain);
        this.direction = direction;
        this.angle = angle;
        this.speed = normalizeSpeed(speed);

        if (!Robot.driveTrain.hasGyro()) {
            throw new IllegalStateException("The current drivetrain doesn't have a gyro...");
        }
    }


    @Override
    protected void execute() {

        System.out.println("Angle: " + Robot.driveTrain.getGyro().getAngle());
        if (direction == Direction.LEFT) {
            Robot.driveTrain.set(-speed, speed);
        } else if (direction == Direction.RIGHT) {
            Robot.driveTrain.set(speed, -speed);
        }

    }

    @Override
    protected void initialize() {
        Robot.driveTrain.getGyro().reset();
    }

    @Override
    protected boolean isFinished() {
        System.out.println("Angle is " + Robot.gyro.getAngle());
        if (direction == Direction.RIGHT) {
            return Robot.driveTrain.getGyro().getAngle() >= angle;
        } else if (direction == Direction.LEFT) {
            return Robot.driveTrain.getGyro().getAngle() <= -angle;
        }
        // weird edge case
        return true;
    }

    @Override
    protected void end() {
        Robot.driveTrain.stop();
    }

    private double normalizeSpeed(double rawSpeed) {
        if (speed > 1.0 || speed < 0.0) {
            throw new IllegalArgumentException("Cannot have a speed that is less than 0 or greater than 1");
        } else {
            return rawSpeed;
        }
    }
}
