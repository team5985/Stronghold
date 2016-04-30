package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import java.io.IOException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot 
{
	//Member Objects
	DriverStation driverStation;
	DriveTrain robotDrive;
	Arm _arm;
	Intake _intake;
	CameraServer camera1;
	//Constants for Autonomous
	/* Autonomous Constants 
	 * AUTO_[Defence Type]_[Speed]_[arm]
	 */
	final int AUTO_DEFAULT = 0;			//No Setting/Error				
	final int AUTO_NONE = -1; 				//No Auto
	final int AUTO_SHORT = 1;				//Short Travel only
	final int AUTO_LOW_SLOW_NO_ARM = 2;		//Rough Terrain
	final int AUTO_LOW_SLOW_ARM = 3;		//Low Bar
	final int AUTO_LOW_FAST_NO_ARM = 4;		//Rock Wall, Moat?
	final int AUTO_LOW_MED_NO_ARM = 5;		//Ramparts
		
	// Constants for Robot Ports
	
	
	// PWM
	int PWM_LEFT_MOTOR_CONTROLLER_PORT = 0;
	int PWM_RIGHT_MOTOR_CONTROLLER_PORT = 9;
	int PWM_INTAKE_MOTOR_CONTROLLER_PORT = 1;
	int PWM_ARM_MOTOR_CONTROLLER_PORT = 8;
	
	// DIO Ports used on the Rio
	int DIO_INTAKE_SWITCH_PORT  = 1;
	int DIO_ARM_SWITCH_PORT = 3;
	int DIO_ARM_ENCODER_PORT = 8;
	int DIO_ARM_ENCODER_PORT2 = 9;
	
	//Internal member variables
	int autoLoopCounter; //How long in auto?
	long periodicStartMs; 
	long gyroResetFinished;
	double autoNumber;
	double gyroHeading;
	boolean autoRun = false;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	//System.out.println("robotInit: Called!");
    	
    	camera1 = CameraServer.getInstance();
    	driverStation = new DriverStation(camera1);
    	
    	robotDrive = new DriveTrain(PWM_LEFT_MOTOR_CONTROLLER_PORT,PWM_RIGHT_MOTOR_CONTROLLER_PORT);
 	
    	//_testEncoder = new PBEncoder(PWM_ARM_MOTOR_CONTROLLER_PORT, 8, 9);
    	//armEncoder = new Encoder(4,5,false,Encoder.EncodingType.k4X);
    	
    	_intake = new Intake(PWM_INTAKE_MOTOR_CONTROLLER_PORT, DIO_INTAKE_SWITCH_PORT); 	
    	_arm = new Arm(PWM_ARM_MOTOR_CONTROLLER_PORT, DIO_ARM_SWITCH_PORT, DIO_ARM_ENCODER_PORT, DIO_ARM_ENCODER_PORT2);
    	driverStation.smartDashNum("Autonomous Program Selector",0);
    	robotDrive.gyro.calibrate();
    	autoRun = false;
    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
		System.out.println("autonomousInit: STARTED");
    	
    	autoLoopCounter = 0;
    	
    	//start time for auto period 
    	periodicStartMs = System.currentTimeMillis();
    	
    	_arm.init();
    	autoNumber = SmartDashboard.getNumber("Autonomous Program Selector");
    	gyroHeading = robotDrive.gyro.getAngle();
    	//	System.out.println("auto number: " + autoNumber);
    	autoRun = true;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() 
    {
    	
    	long currentPeriodtimeSincePeriodStartMs = System.currentTimeMillis() - periodicStartMs;

    	switch((int)autoNumber)
    	{
    	case(AUTO_NONE):
    		
    		break;
    	case(AUTO_SHORT):
    		System.out.println("Auto Program = 1: Short Drive, Low Power, No Arm");
    		autoDrive(currentPeriodtimeSincePeriodStartMs, 1000, 2400, 0.5, gyroHeading, 0);    		
    		break;
    	case(AUTO_LOW_SLOW_NO_ARM):
    		System.out.println("Auto Program = 2: No Arm Low Power Drive");
    		autoDrive(currentPeriodtimeSincePeriodStartMs, 1000, 4250, 0.5, gyroHeading, 0);
    		break;
    	case(AUTO_LOW_SLOW_ARM):
    		System.out.println("Auto Program = 3: Arm Low Power Drive");
			autoDrive(currentPeriodtimeSincePeriodStartMs, 1000, 4250, 0.5, gyroHeading, -0.25);
			break;
    	case(AUTO_LOW_FAST_NO_ARM):
    		System.out.println("Auto Program = 4: No Arm High Power Drive");
    		autoDrive(currentPeriodtimeSincePeriodStartMs, 1000, 3500, 0.9, gyroHeading, 0);
			break;
    	case(AUTO_LOW_MED_NO_ARM):
    		System.out.println("Auto Program = 5: No Arm Medium Power Drive");
    		autoDrive(currentPeriodtimeSincePeriodStartMs, 1000, 3500, 0.7, gyroHeading, 0);
			break;
    	case(AUTO_DEFAULT):
    			System.out.println("autonomousPeriodic: Auto Mode not set");
    			break;
    	default:
    		System.out.println("autonomousPeriodic: Unknown Value");
			break;
    	}
  	}
   
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	System.out.println("teleopInit: Called");
    	if (!autoRun)
    		{
    			_arm.init();
    		}
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	try
    	{
    	//SmartDashboard.putNumber("Pulse Count:", _testEncoded.getRawCount());
    	//_testEncoder.setSpeed( xbox.getRawAxis(1) / 4 );
    	driverStation.driverPeriodic();
    	robotDrive.processButtons(driverStation);
    	robotDrive.teleopDrive(driverStation);
    	_intake.handleEvents(driverStation);
    	_arm.handleEvents(driverStation);
    	
    	System.out.println("teleopPeriodic: Stick x = " + driverStation.stick.getX() + " y = " + driverStation.stick.getY());
    	

    	driverStation.smartDashBool("Boulder Switch Released", _intake.hasBoulder());
    	driverStation.smartDashBool("Arm Switch Pressed", _arm.armUp());
    	}
    	catch (Exception Err)
    	{
    		//Err.??
    		//throw new Exception (Err.getMessage());
    	}
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    } 
    
    public void disabledInit() 
    {
    	driverStation.xbox.setRumble(RumbleType.kLeftRumble, 0);
    	driverStation.xbox.setRumble(RumbleType.kRightRumble, 0);
    	
    }
    
    private void autoDrive(long timeElapsedMs, int minTime, int maxTime, double speed, double gyroTarget, double armPosition)
    {
    	long armMoveFinished = 0;
		if (timeElapsedMs < minTime)
		{
			_arm.auto(armPosition);
			armMoveFinished = timeElapsedMs;
		}
		if ((timeElapsedMs - armMoveFinished) < maxTime && (timeElapsedMs - armMoveFinished) > minTime)
    	{
    		//Gyro follow towards gyro target
			
    		robotDrive.gyroFollow(speed, gyroTarget); 	// drive forwards half speed
    	}
    	else 
    	{
    	 	// stop robot
    		robotDrive.auto(0,0);
		}
    }

}