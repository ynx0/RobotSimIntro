package org.usfirst.frc.team1683.path;

import org.usfirst.frc.team1683.driveTrain.DriveTrain;

/**
 * 
 * Moves robot in a path based on coordinates
 * @author Pran
 *
 */
public class Path {
	private PathPoint[] path;
	private double currentHeading;
	private DriveTrainMover mover;
	private DriveTrainTurner turner;
	private DriveTrain driveTrain;
	private boolean isTurning = true;
	private int pathIndex = 0;
	private double speed;
	private double turnSpeed;
	private boolean stopCondition = false;
	private boolean canMoveBackward = false;
	private boolean isMovingBackward = false;
	private SpeedEasing easing;
	

	/**
	 * Creates a new path object with a default initial heading of 90 degrees
	 * @param driveTrain The driveTrain that controls the motors
	 * @param path An array of PathPoint objects indicating where to go
	 * @param speed The speed with which to move and turn
	 */
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed, double turnSpeed) {
		this(driveTrain, path, speed, turnSpeed, 90);
	}
	
	/**
	 * Creates a new path object
	 * @param driveTrain The driveTrain that controls the motors
	 * @param path An array of PathPoint objects indicating where to go
	 * @param speed The speed with which to move and turn
	 * @param currentHeading The current heading in degrees, where 0 degrees is horizontal, and 90 degrees is pointing up (towards positive y coordinates)
	 * 							The current heading is used to determine how much to initially turn.
	 */
	public Path(DriveTrain driveTrain, PathPoint[] path, double speed, double turnSpeed, double currentHeading) {
		this.driveTrain = driveTrain;
		this.path = path;
		this.currentHeading = currentHeading;
		this.speed = speed;
		this.turnSpeed = turnSpeed;
		PathPoint.convertAbsoluteToRelative(path);
	}
	
	/**
	 * Sometimes the path has to move straight ahead
	 * In this case, this method sets whether the stop condition should be all motors are done or any
	 * In some cases one side might finish before the other side, so what should it do?
	 * If all, then it will call driveTrain.areAllFinished()
	 * If any, it will call driveTrain.areAnyFinished()
	 * The default is any
	 * @param allFinished true if the robot should wait until *all* motors are done before moving to the next point, false otherwise
	 */
	public void setStopCondition(boolean allFinished) {
		stopCondition = allFinished;
	}
	
	private boolean isMoverDone() {
		if (stopCondition) {
			return mover.areAllFinished();
		} else {
			return mover.areAnyFinished();
		}
	}
	
	public boolean isDone() {
		return pathIndex >= path.length;
	}
	
	public void run() {
		if (isDone()) {
			driveTrain.stop();
			return;
		}
		if (isTurning) {
			if (turner == null) {
				turner = new DriveTrainTurner(driveTrain, calculateTurnAngle(), speed);
			}
			if (turner.isDone()) {
				double b = isMovingBackward ? -1 : 1;
				mover = new DriveTrainMover(driveTrain, b * path[pathIndex].getDistance(), speed, easing);
				isTurning = false;
				currentHeading += turner.getAngle();
				if (isMovingBackward) {
					
				}
				driveTrain.stop();
			} else {
				turner.run();
			}
		} else {
			mover.runIteration();
			if (isMoverDone()) {
				pathIndex++;
				if (!isDone()) {
					turner = new DriveTrainTurner(driveTrain, calculateTurnAngle(), Math.abs(turnSpeed));
					isTurning = true;
				}
			}
		}
	}
	
	/**
	 * Gives the turn angle necessary for movement
	 * Normally, this is simply path[pathIndex].getAngle() - currentHeading
	 * However, if canMoveBackward is set to true, then we force the angle to be between -90 and 90.
	 * @return
	 */
	private double calculateTurnAngle() {
		double angle = path[pathIndex].getAngle() - currentHeading;
		if (!canMoveBackward) {
			isMovingBackward = false;
			return angle;
		} else {
			// Ensure that -90 <= angle <= 90
			isMovingBackward = angle < -90 || angle > 90;
			if (angle > 90)
				return angle - 180;
			else if (angle < -90)
				return angle + 180;
			else
				return angle;
		}
	}
	
	/**
	 * @return The speed when moving straight
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @return The turning speed
	 */
	public double getTurnSpeed() {
		return turnSpeed;
	}

	/**
	 * @return Whether the robot can move backwards when executing the path, defaults to false
	 */
	public boolean getCanMoveBackward() {
		return canMoveBackward;
	}

	/**
	 * @param canMoveBackward If true, the robot will be able
	 * to move backwards when executing the path, possibly making it quicker
	 */
	public void setCanMoveBackward(boolean canMoveBackward) {
		this.canMoveBackward = canMoveBackward;
	}

	/**
	 * @param easing the easing to set
	 */
	public void setEasing(SpeedEasing easing) {
		this.easing = easing;
	}
}
