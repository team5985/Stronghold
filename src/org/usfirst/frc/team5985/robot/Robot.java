package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
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
public class Robot extends IterativeRobot {

	//Member Objects
		Drive robotDrive;
		Arm _arm;
		Intake _intake;
		CameraServer camera1;
		
		//Encoder armEncoder;
		//PBEncoder _testEncoder;
		
		
		// Constants for Robot Ports
	// PWM
	int PWM_LEFT_MOTOR_CONTROLLER_PORT = 0;
	int PWM_RIGHT_MOTOR_CONTROLLER_PORT = 9;
	int PWM_INTAKE_MOTOR_CONTROLLER_PORT = 1;
	int PWM_ARM_MOTOR_CONTROLLER_PORT = 8;
	
	// DIO Ports used on the Rio
	int DIO_INTAKE_SWITCH_PORT  = 1;
			
	int autoLoopCounter;
	long periodicStartMs;
	
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit(DriverStation driverStation) {
    	camera1 = CameraServer.getInstance();
    	driverStation.driverInit(camera1);
    	
    	robotDrive = new Drive(PWM_LEFT_MOTOR_CONTROLLER_PORT,PWM_RIGHT_MOTOR_CONTROLLER_PORT);
 	
    	//_testEncoder = new PBEncoder(PWM_ARM_MOTOR_CONTROLLER_PORT, 8, 9);
    	
    	//armEncoder = new Encoder(4,5,false,Encoder.EncodingType.k4X);
    	
    	_intake = new Intake(PWM_INTAKE_MOTOR_CONTROLLER_PORT, DIO_INTAKE_SWITCH_PORT); 	
    	_arm = new Arm(PWM_ARM_MOTOR_CONTROLLER_PORT);
    	
    	
    	    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit(Drive drive) {
		System.out.println("autonomousInit: STARTED");
    	
    	SmartDashboard.putString("test", "Test");
    	
    	autoLoopCounter = 0;
    	
    	//start time for auto period 
    	periodicStartMs = System.currentTimeMillis();
    	
    	drive.gyro.reset();
    	
    	//int gameSelector = (int) SmartDashboard.getNumber("Auto Selector");
    	//System.out.println(gameSelector);
    	
    	_arm.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic(Drive drive) {
    	
    	long currentPeriodtimeSincePeriodStartMs = System.currentTimeMillis() - periodicStartMs;
    	// First second of the auto period
    	if (currentPeriodtimeSincePeriodStartMs < 1000)
    	{
    		//Move arm and wait until reset gyro is complete  
    		_arm.auto(-0.6);	//test arm movement
    	}
    	
    	
    	
    	// drive forward for 2 seconds (from 1-3 seconds)
    	if (currentPeriodtimeSincePeriodStartMs < 3000 && currentPeriodtimeSincePeriodStartMs > 1000)
    	{
    		_arm.auto(0);
    		drive.gyroFollow(0.5, 0); 	// drive forwards half speed
    	}
    	else 
    	{
    	 	// stop robot
    		drive.auto(0);
		}
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	System.out.println("teleopInit: Called");
    	_arm.init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic(DriverStation driverStation, Drive drive) 
    {
    	//SmartDashboard.putNumber("Pulse Count:", _testEncoded.getRawCount());
    	//_testEncoder.setSpeed( xbox.getRawAxis(1) / 4 );
    	driverStation.driverPeriodic();
    	drive.processButtons(driverStation);
    	drive.teleopDrive(driverStation);
    	_intake.handleEvents(driverStation);
    	_arm.handleEvents(driverStation);
    	if (_intake.hasBoulder())
    	{
    		driverStation.xbox.setRumble(RumbleType.kLeftRumble, 1);
    		driverStation.xbox.setRumble(RumbleType.kRightRumble, 1);
    	}
    	else 
    	{
    		driverStation.xbox.setRumble(RumbleType.kLeftRumble, 0);
    		driverStation.xbox.setRumble(RumbleType.kRightRumble, 0);
    	}
    	
    	//System.out.println("Arm encoder distance: " + armEncoder.getDistance());
    	//System.out.println("Intake encoder distance: " + armEncoder.getDistance());
    	System.out.println("teleopPeriodic: Stick x = " + driverStation.stick.getX() + " y = " + driverStation.stick.getY());
    	

    	SmartDashboard.putString("Mode", "Teleop");
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    } 
    
    public void disabledInit() 
    {
    	//xbox.setRumble(RumbleType.kLeftRumble, 0);
    }

}