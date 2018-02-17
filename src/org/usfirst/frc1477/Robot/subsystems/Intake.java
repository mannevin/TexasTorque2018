// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc1477.Robot.subsystems;

import org.usfirst.frc1477.Robot.RobotMap;
import org.usfirst.frc1477.Robot.commands.BlockIntake;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Intake extends Subsystem implements Sendable {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final DoubleSolenoid intakeGrip = RobotMap.intakeIntakeGrip;
    private final DoubleSolenoid intakeExtender = RobotMap.intakeIntakeExtender;
    private final SpeedController leftIntake = RobotMap.intakeLeftIntake;
    private final SpeedController rightIntake = RobotMap.intakeRightIntake;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new BlockIntake(0));

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void periodic() {
    }
    public void intake() {
    	if (leftIntake.get()>0 && rightIntake.get() > 0) {
    		leftIntake.set(-.5);
    		rightIntake.set(-.5);
    	}
    	else{
    		leftIntake.set(.5);
    		rightIntake.set(.5);
    	}
    }
    public void setGrip() {
    	if(intakeGrip.get() == Value.kReverse)
    		intakeGrip.set(Value.kForward);
    	else
    		intakeGrip.set(Value.kReverse);
    }
	public void stopIntake() {
    	leftIntake.set(0);
    	rightIntake.set(0);
    }
    public void intakeExtend() {
    	if (intakeExtender.get() == Value.kForward)
    		intakeExtender.set(Value.kReverse);
    	else
    		intakeExtender.set(Value.kForward);	
    }
    public void stop() {
    	stopIntake();
    	intakeExtender.set(Value.kOff);
    	intakeGrip.set(Value.kOff);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

