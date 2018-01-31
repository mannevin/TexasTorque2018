package org.texastorque;

import org.texastorque.subsystems.Drivebase;
import org.texastorque.subsystems.Arm;
import org.texastorque.subsystems.Claw;

import java.util.ArrayList;

import org.texastorque.auto.AutoManager;
import org.texastorque.auto.AutoMode;
import org.texastorque.feedback.Feedback;
import org.texastorque.io.HumanInput;
import org.texastorque.io.Input;
import org.texastorque.io.InputRecorder;
import org.texastorque.io.HumanInput;
import org.texastorque.io.RobotOutput;
import org.texastorque.subsystems.Subsystem;
import org.texastorque.torquelib.base.TorqueIterative;
import org.texastorque.subsystems.Drivebase;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TorqueIterative {


	ArrayList<Subsystem> subsystems;
	private String fieldConfig;
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		fieldConfig = "";
		SmartDashboard.putNumber("AutoMode", 0);
		Input.getInstance();
		HumanInput.getInstance();
		InputRecorder.getInstance();
		RobotOutput.getInstance();
		Feedback.getInstance();
		subsystems = new ArrayList<Subsystem>(){{
			add(Drivebase.getInstance());
			add(Arm.getInstance());
			add(Claw.getInstance());
		}};
		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	
	@Override
	public void disabledInit() {
		Feedback.getInstance().reset();
	}

	@Override
	public void disabledPeriodic() {
		
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
	
		AutoManager.getInstance().resetAuto();
		
	
	}


	@Override
	public void disabledContinuous() {
		
	}
	
	@Override
	public void teleopPeriodic() {
		
	}
	
	@Override
	public void autonomousPeriodic() {
	
		
	}
	
	public void alwaysContinuous() {
		Feedback.getInstance().update();
		Feedback.getInstance().SmartDashboard();
	}
	
			
	@Override
	public void autonomousContinuous(){
		AutoManager.getInstance().getRunningMode().run();
	}
	
	@Override
	public void teleopInit() {
		InputRecorder.getInstance();
		for(Subsystem system : subsystems) {
			system.teleopInit();
			system.setInput(HumanInput.getInstance());
			
		}
	}

	@Override
	public void teleopContinuous(){
		HumanInput.getInstance().update();
		InputRecorder.getInstance().update();
		for(Subsystem s: subsystems)
			s.teleopContinuous();
		Drivebase.getInstance().teleopContinuous();
		
	}
	
}
