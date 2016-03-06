package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	// Constants for Robot Ports
	// PWM
	int PWM_LEFT_MOTOR_CONTROLLER_PORT = 0;
	int PWM_RIGHT_MOTOR_CONTROLLER_PORT = 9;
	int PWM_INTAKE_MOTOR_CONTROLLER_PORT = 1;
	int PWM_ARM_MOTOR_CONTROLLER_PORT = 8;
	
	// DIO Ports used on the Rio
	int DIO_INTAKE_SWITCH_PORT  = 1;

	//Power Preset constants
	int driveType = -1;
	
	final int DRIVE_FREE = -1;
	final int DRIVE_BRAKE = 0;
	final int DRIVE_LOW = 1;
	final int DRIVE_MEDIUM = 2;
	final int DRIVE_HIGH = 3;
		
	final double LOW_POWER = 0.2;
	final double MEDIUM_POWER = 0.4;
	final double HIGH_POWER = 0.6;
	
	//Member Objects
	Arm _arm;
	Intake _intake;
	
	Joystick stick;
	Joystick xbox;
	
	CameraServer camera1;
	
	Victor driveLeft;
	Victor driveRight;
	VictorSP _intakeMotor;
	VictorSP _armMotor;
	
	Encoder armEncoder;
	
	ADXRS450_Gyro _gyro;
	private static final double GYRO_GAIN = 0.008;
	DigitalInput lineSensor;
	DigitalInput intakeSwitch;
	
	int autoLoopCounter;
	long periodicStartMs;
	

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	driveLeft = new Victor(PWM_LEFT_MOTOR_CONTROLLER_PORT);
    	driveRight = new Victor(PWM_RIGHT_MOTOR_CONTROLLER_PORT);
   
    	_armMotor = new VictorSP(PWM_ARM_MOTOR_CONTROLLER_PORT);
    	
    	//armEncoder = new Encoder(4,5,false,Encoder.EncodingType.k4X);
    	
    	stick = new Joystick(0);
        xbox =	 new Joystick(1);
        
    	_intake = new Intake(PWM_INTAKE_MOTOR_CONTROLLER_PORT, DIO_INTAKE_SWITCH_PORT); 	
    	_arm = new Arm(_armMotor,xbox);
    	
    	_gyro = new ADXRS450_Gyro();
    	_gyro.reset();
    	_gyro.calibrate();
    	
    	camera1 = CameraServer.getInstance();
    	camera1.setQuality(50);
    	camera1.startAutomaticCapture("cam0");
    	    	
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
		System.out.println("autonomousInit: STARTED");
    	
    	SmartDashboard.putString("test", "Test");
    	
    	autoLoopCounter = 0;
    	
    	//start time for auto period 
    	periodicStartMs = System.currentTimeMillis();
    	
    	_gyro.reset();
    	
    	//int gameSelector = (int) SmartDashboard.getNumber("Auto Selector");
    	//System.out.println(gameSelector);
    	
    	_arm.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	
    	long currentPeriodtimeSincePeriodStartMs = System.currentTimeMillis() - periodicStartMs;
    	// First second of the auto period
    	if (currentPeriodtimeSincePeriodStartMs < 1000)
    	{
    		//Move arm and wait until reset gyro is complete  
    		//_armMotor.set(-0.6);
    	}
    	
    	
    	
    	// drive forward for 2 seconds (from 1-3 seconds)
    	if (currentPeriodtimeSincePeriodStartMs < 3000 && currentPeriodtimeSincePeriodStartMs > 1000)
    	{
    		//_armMotor.set(0);
    		gyroFollow(0.5,0); 	// drive forwards half speed
    	}
    	else // stop
    	{
    		//System.out.println("Gyro Drive Finished: 2 Seconds");
    	 	// stop robot
    		driveLeft.set(0);
			driveRight.set(0);
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
    public void teleopPeriodic() 
    {
    	
    	processButtons();
    	drive();
    	_intake.periodic(stick);
    	_arm.periodic();
    	if (_intake.hasBoulder())
    	{
    		xbox.setRumble(RumbleType.kLeftRumble, 1);
    	}
    	else 
    	{
    		xbox.setRumble(RumbleType.kLeftRumble, 0);
    	}
    	
    	//System.out.println("Arm encoder distance: " + armEncoder.getDistance());
    	//System.out.println("Intake encoder distance: " + armEncoder.getDistance());
    	System.out.println("teleopPeriodic: Stick x = " + stick.getX() + " y = " + stick.getY());
    	
    	SmartDashboard.putNumber("Gyro Angle:", _gyro.getAngle());
    	SmartDashboard.putNumber("Gyro Rate:", _gyro.getRate());
    	SmartDashboard.putString("Mode", "Teleop");
    	SmartDashboard.putNumber("Stick X Value", stick.getX());
    	SmartDashboard.putNumber("Stick Y Value", stick.getY());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    } 
    
    public void disabledInit() 
    {
    	xbox.setRumble(RumbleType.kLeftRumble, 0);
    }
    

    /*
     * Process Buttons:
     * Takes the input from the controller buttons and handles them appropriately
     * 
     */
    private void processButtons()
    {
    	if (stick.getRawButton(11)) driveType = DRIVE_LOW;
    	else if (stick.getRawButton(12)) driveType = DRIVE_MEDIUM;
    	else if (stick.getRawButton(10)) driveType = DRIVE_HIGH;
    	else if (stick.getRawButton(3)) driveType = DRIVE_BRAKE;
    	else if (stick.getRawButton(9)) driveType = DRIVE_FREE;
    	
    	if (stick.getRawButton(7))
    	{
    		System.out.println("Gyro RESET");
    		_gyro.reset();
    	}
    }
    
    /*
     * Gets power modifier from the throttle or power preset and sets the power variable to it
     * 
     * 
     */
    private double getDriveModifier()
    {
    	double power = 0;
    	
    	//Sets the powerPreset setting
    	switch (driveType)
    	{

			case DRIVE_FREE:
				power = -(stick.getThrottle() - 1 ) / 2;
				break;
			
    		case DRIVE_LOW: //20% Power
    			power = LOW_POWER;
    			break;
    		
    		case DRIVE_MEDIUM: //40% Power
    			power = MEDIUM_POWER;
    			break;
    			
    		case DRIVE_HIGH: //60% Power
    			power = HIGH_POWER;
    			break;
    			
    		case DRIVE_BRAKE: //0% Power
    			power = 0;
    			break;

    		default:
    			power = -(stick.getThrottle() - 1 ) / 2;
    		break;
    			
    	}
    	
    	return power;
    }
    private void drive()
    {
    	// Driving code for the robot
    	
    	
    	//Sets the speedModifier to the throttle
    	double speedModifier = getDriveModifier();    	
    	
    	double POV = stick.getPOV(0);
    	double turnAngle = 0;
    	
    	if (POV == -1) 
    	{
    		//Drives normally
    	
    		//Gets stick values and sets variables for it
        	double steering = stick.getX();
        	double power = stick.getY();
        	
        	//corrects for the stick not being 100% zeroed
        	if (steering < 0.1 && steering > -0.1)
        	{
        		steering = 0;
        	}
        	if (power < 0.1 && power > -0.1)
        	{
        		power = 0;
        	}
        	
        	//multiplies by speedModifier
        	steering = steering * speedModifier;
        	power = power * speedModifier;
        	
        	//Applies above variables to variables that will be applied to the motors
        	double leftPower = -power + steering;
        	double rightPower = power + steering;
        	
        	
        	//Displays the left and right motor powers and speed modifier
        	System.out.println("speedModifier:'" + speedModifier +"'");
        	System.out.println("leftPower:'" + leftPower +"' rightPower:'"+ rightPower +"'");
        	SmartDashboard.putNumber("Speed Modifier", speedModifier);
        	SmartDashboard.putNumber("Left Power", leftPower);
        	SmartDashboard.putNumber("Right Power", rightPower);
        	//Sends the power variables to the motors
        	driveLeft.set(leftPower);
        	driveRight.set(rightPower);
    	
    	 }
    	else 
    	{
    		//Gyro Drive
    		//makes the robot drive in the direction of the POV at 10% of speedModifier
    		
    		    		
    		if (POV > -22.5 & POV < 22.5)
        	{
        		turnAngle = 0;
        	}
    		else if (POV > 22.5 & POV < 67.5)
        	{
        		turnAngle = 45;
        		
        	}
    		else if (POV > 67.5 & POV < 112.5)
        	{
        		turnAngle = 90;
        	}
    		else if (POV > 112.5 & POV < 157.5)
        	{
        		turnAngle = 135;
        	}
    		else if (POV > 157.5 & POV < 202.5)
        	{
        		turnAngle = 180;
        	}
    		else if (POV > 202.5 & POV < 247.5)
        	{
        		turnAngle = -135;
        	}
    		else if (POV > 247.5 & POV < 292.5)
        	{
        		turnAngle = -90;
        	}
    		else if (POV > 292.5 & POV < 337.5)
        	{
        		turnAngle = -45;
        	}
    		gyroFollow(speedModifier,turnAngle);
    	}
    	
    }
    
    private void gyroFollow(double basePower,double gyroTarget)
    {
    	//proportionally drives in the direction of a gyro heading, turning to face the right direction
    	
    	double gyroPower = 0;
    	double modGyroAngle = _gyro.getAngle() % 360;
    	
    	if (modGyroAngle < (gyroTarget - 180))
			{gyroTarget = gyroTarget - 360;}
    	
    	//Calculates how much to turn based on the current heading and the target heading
    	gyroPower = modGyroAngle - gyroTarget;
    	gyroPower = gyroPower * GYRO_GAIN;
    	
    	double gyroMotorPowerLeft = basePower - gyroPower;
    	double gyroMotorPowerRight = -basePower - gyroPower;
    	
    	//Makes the motors move
    	driveLeft.set(gyroMotorPowerLeft);
    	driveRight.set(gyroMotorPowerRight);
    		
    	System.out.println("[Gyro:" + modGyroAngle + "]    Motor [Left:" + gyroMotorPowerLeft + "][Right:" + gyroMotorPowerRight + "]");
    	SmartDashboard.putNumber("Corrected Gyro Angle:", modGyroAngle);
    	
    
    }
}