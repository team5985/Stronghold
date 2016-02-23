package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
//import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Timer;

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
	
	Arm _arm;
	Intake _intake;
	
	//RobotDrive myRobot;
	
	Joystick stick;
	Joystick xbox;
	
	CameraServer camera1;
	//CameraServer camera2;
	
	Victor driveLeft;
	Victor driveRight;
	VictorSP _intakeMotor;
	VictorSP _armMotor;
	
	Encoder intakeEncoder;
	Encoder armEncoder;
	
	ADXRS450_Gyro _gyro;
	private static final double GYRO_GAIN = 0.008;
	double gyroTarget;
	DigitalInput lineSensor;
	DigitalInput intakeSwitch;
	
	I2C gestureSensor;
	byte[] gestureOutput;
	
	//SPI gyro;

	int autoLoopCounter;
	long periodicStartMs;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	
//    	myRobot = new RobotDrive(0,1);
    	driveLeft = new Victor(PWM_LEFT_MOTOR_CONTROLLER_PORT);
    	driveRight = new Victor(PWM_RIGHT_MOTOR_CONTROLLER_PORT);
   
    	_armMotor = new VictorSP(PWM_ARM_MOTOR_CONTROLLER_PORT);
    	
    	//gestureSensor = new I2C(I2C.Port.kOnboard, 0x39);
    	//gestureOutput = new byte[200];
    	
    	//intakeEncoder = new Encoder(2,3,false,Encoder.EncodingType.k4X);
    	//armEncoder = new Encoder(4,5,false,Encoder.EncodingType.k4X);
    	
    	stick = new Joystick(0);
        xbox =	 new Joystick(1);
        
    	_intake = new Intake(PWM_INTAKE_MOTOR_CONTROLLER_PORT, DIO_INTAKE_SWITCH_PORT);
    	
    	_arm = new Arm(_armMotor,xbox);
    	_gyro = new ADXRS450_Gyro();
    	_gyro.reset();
    	_gyro.calibrate();
    	gyroTarget = 0;
    	
//    	lineSensor = new DigitalInput(0);
    	
    	camera1 = CameraServer.getInstance();
    	camera1.setQuality(50);
    	camera1.startAutomaticCapture("cam0");
    	/*camera2 = CameraServer.getInstance();
    	camera2.setQuality(50);
    	camera2.startAutomaticCapture("cam1");*/
    	
    	//gyro = new SPI(0);
    	
    	System.out.println("Gyro:" + _gyro.getAngle());
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
    	
    	
    	
    	//intakeEncoder.reset();
    	
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
    		//set target direction to Forwards, move arm and wait till reset gyro is complete  
        	gyroTarget = 0;
    		_armMotor.set(-0.6);
    	}
    	
    	
    	
    	// drive forward for 5 seconds (from 1- 6 seconds)
    	if (currentPeriodtimeSincePeriodStartMs < 2000 && currentPeriodtimeSincePeriodStartMs > 1000)//6000 && currentPeriodtimeSincePeriodStartMs > 1000)
    	{
    		_armMotor.set(0);
    		gyroFollow(0.5); 	// drive forwards half speed
    	}
    	else // stop
    	{
    		System.out.println("Stop Gyro Drive (5 Seconds)");
			driveLeft.set(0); 	// stop robot
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
    	drive();
    	_intake.periodic(stick);
    	_arm.periodic();
    	if (stick.getRawButton(7))
    	{
    		System.out.println("Gyro RESET");
    		_gyro.reset();
    	}
    	System.out.println("Gyro:" + _gyro.getAngle());
    	System.out.println("Rate:" + _gyro.getRate());
    	/*gestureSensor.read(0x08, 1, gestureOutput); //X coord
		String output = new String(gestureOutput);
    	System.out.println("X coords" + output);
    	gestureSensor.read(0x0a, 1, gestureOutput); //Z coord
		output = new String(gestureOutput);
    	System.out.println("Z coords" + output);
    	*/
    	
    	
    	
    	//System.out.println("Arm encoder distance: " + armEncoder.getDistance());
    	//System.out.println("Intake encoder distance: " + armEncoder.getDistance());
    	System.out.println("teleopPeriodic: Stick x = " + stick.getX() + " y = " + stick.getY());
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    } 
    
    private void drive()
    {
    	// Driving code for the robot
    	
    	//Sets the speedModifier to the throttle
    	double speedModifier = (stick.getThrottle() - 1 ) / 2;
    	
    	/*double POV = stick.getPOV(0);
    	
    	if (POV == -1) 
    	{
    		//Drives normally
    	*/
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
        	double leftPower = power + -steering;
        	double rightPower = -power + -steering;
        	
        	
        	//Displays the left and right motor powers and speed modifier
        	System.out.println("speedModifier:'" + speedModifier +"'");
        	System.out.println("leftPower:'" + leftPower +"' rightPower:'"+ rightPower +"'");
        	
        	//Sends the power variables to the motors
        	driveLeft.set(leftPower);
        	driveRight.set(rightPower);
    	/*
    	 }
    	else 
    	{
    		//Gyro Drive
    		//makes the robot drive in the direction of the POV at 10% of speedModifier
    		
    		if (POV > -22.5 & POV < 22.5)
        	{
        		gyroTarget = 0;
        	}
    		else if (POV > 22.5 & POV < 67.5)
        	{
        		gyroTarget = 45;
        	}
    		else if (POV > 67.5 & POV < 112.5)
        	{
        		gyroTarget = 90;
        	}
    		else if (POV > 112.5 & POV < 157.5)
        	{
        		gyroTarget = 135;
        	}
    		else if (POV > 157.5 & POV < 202.5)
        	{
        		gyroTarget = 180;
        	}
    		else if (POV > 202.5 & POV < 247.5)
        	{
        		gyroTarget = 225;
        	}
    		else if (POV > 247.5 & POV < 292.5)
        	{
        		gyroTarget = 270;
        	}
    		else if (POV > 292.5 & POV < 337.5)
        	{
        		gyroTarget = 315;
        	}
    		gyroFollow(speedModifier * 0.1);
    	}*/
    	
    }
    
    private void gyroFollow(double basePower)
    {
    	double gyroPower = 0;
    	
    	
    	//System.out.println("Gyro:" + _gyro.getAngle());
    	
    	//Calculates how much to turn based on the current heading and the target heading
    	gyroPower = _gyro.getAngle() - gyroTarget;
    	gyroPower = gyroPower * GYRO_GAIN;
    	
    	double gyroMotorPowerLeft = basePower - gyroPower;
    	double gyroMotorPowerRight = -basePower - gyroPower;
    	
    	//Makes the motors move
    	driveLeft.set(gyroMotorPowerLeft);
    	driveRight.set(gyroMotorPowerRight);
    	
    	System.out.println("[Gyro:" + _gyro.getAngle() + "]    Motor [Left:" + gyroMotorPowerLeft + "][Right:" + gyroMotorPowerRight + "]");
    }
}