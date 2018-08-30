package commands;

import simulation.CommandGroup;
import util.Direction;

public class Square extends CommandGroup {

    public Square(double distanceToTravel, double speed) {
        addSequential(new TurnToAngle(Direction.RIGHT, 45, speed));
        addSequential(new DriveForward(distanceToTravel, speed));
        addSequential(new TurnToAngle(Direction.LEFT, 90, speed));
        addSequential(new DriveForward(distanceToTravel, speed));
        addSequential(new TurnToAngle(Direction.LEFT, 90, speed));
        addSequential(new DriveForward(distanceToTravel, speed));
        addSequential(new TurnToAngle(Direction.LEFT, 90, speed));
        addSequential(new DriveForward(distanceToTravel, speed));
    }

}
