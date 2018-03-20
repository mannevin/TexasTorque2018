package org.texastorque.subsystems;

import org.texastorque.auto.AutoManager;

import org.texastorque.constants.Constants;
import org.texastorque.subsystems.Subsystem.AutoType;
import org.texastorque.torquelib.controlLoop.TorquePV;
import org.texastorque.torquelib.controlLoop.TorqueRIMP;
import org.texastorque.torquelib.controlLoop.TorqueTMP;
import org.texastorque.torquelib.util.TorqueMathUtil;

import edu.wpi.first.wpilibj.Timer;

public class Drivebase extends Subsystem {

	private static Drivebase instance;

	private double leftSpeed;
	private double rightSpeed;

	// Drive
	private double setpoint = 0;
	private double previousSetpoint = 0;
	private double previousTime;
	private double precision;

	private TorqueTMP driveTMP;
	private TorquePV leftPV;
	private TorquePV rightPV;
	private TorqueRIMP leftRIMP;
	private TorqueRIMP rightRIMP;

	// Turn
	private double turnSetpoint;
	private double currentAngle;

	public enum DriveType {
		TELEOP, AUTODRIVE, AUTOTURN, AUTOBACKUP, AUTOOVERRIDE, WAIT;
	}

	private DriveType type;

	private Drivebase() {
		init();
	}

	@Override
	public void autoInit() {
		type = DriveType.AUTODRIVE;
		init();
	}

	@Override
	public void teleopInit() {
		type = DriveType.TELEOP;
		init();
	}

	@Override
	public void disabledInit() {
		leftSpeed = 0;
		rightSpeed = 0;
	}

	private void init() {
		driveTMP = new TorqueTMP(Constants.DB_MVELOCITY.getDouble(), Constants.DB_MACCELERATION.getDouble());
		leftPV = new TorquePV();
		rightPV = new TorquePV();

		leftPV.setGains(Constants.DB_LEFT_PV_P.getDouble(), Constants.DB_LEFT_PV_V.getDouble(),
				Constants.DB_LEFT_PV_ffV.getDouble(), Constants.DB_LEFT_PV_ffA.getDouble());
		leftPV.setTunedVoltage(Constants.TUNED_VOLTAGE.getDouble());

		rightPV.setGains(Constants.DB_RIGHT_PV_P.getDouble(), Constants.DB_RIGHT_PV_V.getDouble(),
				Constants.DB_RIGHT_PV_ffV.getDouble(), Constants.DB_RIGHT_PV_ffA.getDouble());
		rightPV.setTunedVoltage(Constants.TUNED_VOLTAGE.getDouble());

		rightRIMP = new TorqueRIMP(Constants.DB_MVELOCITY.getDouble(), Constants.DB_MACCELERATION.getDouble(), 0);
		leftRIMP = new TorqueRIMP(Constants.DB_MVELOCITY.getDouble(), Constants.DB_MACCELERATION.getDouble(), 0);

		rightRIMP.setGains(Constants.DB_RIMP_P.getDouble(), Constants.DB_RIMP_V.getDouble(),
				Constants.DB_RIMP_ffV.getDouble(), Constants.DB_RIMP_ffA.getDouble());
		leftRIMP.setGains(Constants.DB_RIMP_P.getDouble(), Constants.DB_RIMP_V.getDouble(),
				Constants.DB_RIMP_ffV.getDouble(), Constants.DB_RIMP_ffA.getDouble());

		previousTime = Timer.getFPGATimestamp();
	}

	@Override
	public void disabledContinuous() {
		output();
	}

	@Override
	public void teleopContinuous() {
		switch (type) {
		case TELEOP:
			leftSpeed = i.getDBLeftSpeed();
			rightSpeed = i.getDBRightSpeed();
			break;
			
		default:
			type = DriveType.TELEOP;
			break;
		}
		output();
	}
	
	@Override
	public void autoContinuous() {
<<<<<<< HEAD
		if(type.equals(AutoType.RECORDING))
			recordingAutoContin();
		else commandAutoContin();
		output();
	}
	
	private void recordingAutoContin() {
		leftSpeed = auto.getDBLeftSpeed();
		rightSpeed = auto.getDBRightSpeed();
		
	}
	
	private void commandAutoContin() {
		double dt;
		switch (type) {
			case AUTODRIVE:
				setpoint = i.getDBDriveSetpoint();
				if (setpoint != previousSetpoint) {
					previousSetpoint = setpoint;
					precision = i.getDBPrecision();
					driveTMP.generateTrapezoid(setpoint, 0d, 0d);
					previousTime = Timer.getFPGATimestamp();
				}
				if (TorqueMathUtil.near(setpoint, f.getDBLeftDistance(), precision)
						&& TorqueMathUtil.near(setpoint, f.getDBRightDistance(), precision))
					AutoManager.interruptThread();
				dt = Timer.getFPGATimestamp() - previousTime;
				previousTime = Timer.getFPGATimestamp();
				driveTMP.calculateNextSituation(dt);
		
				targetPosition = driveTMP.getCurrentPosition();
				targetVelocity = driveTMP.getCurrentVelocity();
				targetAcceleration = driveTMP.getCurrentAcceleration();
		
				leftSpeed = leftPV.calculate(driveTMP, f.getDBLeftDistance(), f.getDBLeftRate());
				rightSpeed = rightPV.calculate(driveTMP, f.getDBRightDistance(), f.getDBRightRate());
				break;
			
			case AUTOTURN:
				turnSetpoint = i.getDBTurnSetpoint();
				currentAngle = f.getDBAngle();
			/*	if (turnSetpoint != turnPreviousSetpoint) {
					turnPreviousSetpoint = turnSetpoint;
					precision = i.getDBPrecision();
					turnTMP.generateTrapezoid(turnSetpoint, 0.0, 0.0);
					previousTime = Timer.getFPGATimestamp();
				}
				if (TorqueMathUtil.near(turnSetpoint, f.getDBAngle(), precision)) {
					AutoManager.interruptThread();
				}
					//AutoManager.interruptThread();
				dt = Timer.getFPGATimestamp() - previousTime;
				previousTime = Timer.getFPGATimestamp();
				turnTMP.calculateNextSituation(dt);
				targetAngle = turnTMP.getCurrentPosition();
				targetAngularVelocity = turnTMP.getCurrentVelocity();
				leftSpeed = turnPV.calculate(turnTMP, f.getDBLeftDistance(), f.getDBLeftRate());
				rightSpeed = -leftSpeed;
				break;
			*/
				if(!TorqueMathUtil.near(turnSetpoint, f.getDBAngle(), 3)) {
					if(turnSetpoint - currentAngle > 0) {
						leftSpeed = .37;
					} else if(turnSetpoint - currentAngle < 0) {
						leftSpeed = -.37;
					}
					rightSpeed = -leftSpeed;
				} else {
					leftSpeed = 0;
					rightSpeed = 0;
				}
				break;
				
			case AUTOBACKUP:
				leftSpeed = 0.5;
				rightSpeed = 0.5;
				break;
				
			default:
				leftSpeed = 0;
				rightSpeed = 0;
				break;
		}
=======
		switch (type) {
		case AUTODRIVE:
			autoDrive();
			break;

		case AUTOTURN:
			autoTurn();
			break;

		case AUTOBACKUP:
			leftSpeed = 0.5;
			rightSpeed = 0.5;
			break;

		default:
			leftSpeed = 0;
			rightSpeed = 0;
			break;
		}

		output();
	}

	private void autoDrive() {
		double dt = Timer.getFPGATimestamp() - previousTime;

		setpoint = i.getDBDriveSetpoint();
		if (setpoint != previousSetpoint) {
			previousSetpoint = setpoint;
			precision = i.getDBPrecision();
			driveTMP.generateTrapezoid(setpoint, 0d, 0d);
			previousTime = Timer.getFPGATimestamp();
		}

		if (TorqueMathUtil.near(setpoint, f.getDBLeftDistance(), precision)
				&& TorqueMathUtil.near(setpoint, f.getDBRightDistance(), precision)) {
			AutoManager.interruptThread();
		}

		previousTime = Timer.getFPGATimestamp();
		driveTMP.calculateNextSituation(dt);

		leftSpeed = leftPV.calculate(driveTMP, f.getDBLeftDistance(), f.getDBLeftRate());
		rightSpeed = rightPV.calculate(driveTMP, f.getDBRightDistance(), f.getDBRightRate());
	}

	private void autoTurn() {
		turnSetpoint = i.getDBTurnSetpoint();
		currentAngle = f.getDBAngle();

		if (!TorqueMathUtil.near(turnSetpoint, f.getDBAngle(), 3)) {
			if (turnSetpoint - currentAngle > 0) {
				leftSpeed = .33;
			} else if (turnSetpoint - currentAngle < 0) {
				leftSpeed = -.33;
			}

			rightSpeed = -leftSpeed;
		} else {
			leftSpeed = 0;
			rightSpeed = 0;
		}
	}

	@Override
	public void teleopContinuous() {
		switch (type) {
		case TELEOP:
			leftSpeed = i.getDBLeftSpeed();
			rightSpeed = i.getDBRightSpeed();
			break;

		default:
			type = DriveType.TELEOP;
			break;
		}

		output();
>>>>>>> lubecki-lonestar
	}

	private void output() {
		o.setDrivebaseSpeed(leftSpeed, rightSpeed);
	}

	public void setType(DriveType type) {
		this.type = type;
	}

	@Override
	public void smartDashboard() {
	}

	public static Drivebase getInstance() {
		return instance == null ? instance = new Drivebase() : instance;
	}
}
/*
@Override
public void autoContinuous() {
	double dt;
	switch (type) {
		case AUTODRIVE:
			setpoint = i.getDBDriveSetpoint();
			if (setpoint != previousSetpoint) {
				previousSetpoint = setpoint;
				precision = i.getDBPrecision();
				driveTMP.generateTrapezoid(setpoint, 0d, 0d);
				previousTime = Timer.getFPGATimestamp();
			}
			if (TorqueMathUtil.near(setpoint, f.getDBLeftDistance(), precision)
					&& TorqueMathUtil.near(setpoint, f.getDBRightDistance(), precision))
				AutoManager.interruptThread();
			dt = Timer.getFPGATimestamp() - previousTime;
			previousTime = Timer.getFPGATimestamp();
			driveTMP.calculateNextSituation(dt);
	
			leftSpeed = leftPV.calculate(driveTMP, f.getDBLeftDistance(), f.getDBLeftRate());
			rightSpeed = rightPV.calculate(driveTMP, f.getDBRightDistance(), f.getDBRightRate());
			break;
		
		case AUTOTURN:
			turnSetpoint = i.getDBTurnSetpoint();
			currentAngle = f.getDBAngle();
		/*	if (turnSetpoint != turnPreviousSetpoint) {
				turnPreviousSetpoint = turnSetpoint;
				precision = i.getDBPrecision();
				turnTMP.generateTrapezoid(turnSetpoint, 0.0, 0.0);
				previousTime = Timer.getFPGATimestamp();
			}
			if (TorqueMathUtil.near(turnSetpoint, f.getDBAngle(), precision)) {
				AutoManager.interruptThread();
			}
				//AutoManager.interruptThread();
			dt = Timer.getFPGATimestamp() - previousTime;
			previousTime = Timer.getFPGATimestamp();
			turnTMP.calculateNextSituation(dt);

			targetAngle = turnTMP.getCurrentPosition();
			targetAngularVelocity = turnTMP.getCurrentVelocity();

			leftSpeed = turnPV.calculate(turnTMP, f.getDBLeftDistance(), f.getDBLeftRate());
			rightSpeed = -leftSpeed;
			break;
		*/
	/*		if(!TorqueMathUtil.near(turnSetpoint, f.getDBAngle(), 3)) {
				if(turnSetpoint - currentAngle > 0) {
					leftSpeed = .33;
				} else if(turnSetpoint - currentAngle < 0) {
					leftSpeed = -.33;
				}
				rightSpeed = -leftSpeed;
			} else {
				leftSpeed = 0;
				rightSpeed = 0;
			}
			break;
			
		case AUTOBACKUP:
			leftSpeed = 0.5;
			rightSpeed = 0.5;
			break;
			
		default:
			leftSpeed = 0;
			rightSpeed = 0;
			break;
	}
	output();
}
*/