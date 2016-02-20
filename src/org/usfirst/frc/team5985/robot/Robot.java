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

	DigitalInput lineSensor;
	DigitalInput intakeSwitch;
	
	I2C gestureSensor;
	byte[] gestureOutput;
	
	//SPI gyro;

	int autoLoopCounter;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	
//    	myRobot = new RobotDrive(0,1);
    	driveLeft = new Victor(0);
    	driveRight = new Victor(1);
    	_intakeMotor = new VictorSP(2);
    	_armMotor = new VictorSP(3);
    	
    	//gestureSensor = new I2C(I2C.Port.kOnboard, 0x39);
    	//gestureOutput = new byte[200];
    	intakeSwitch = new DigitalInput(1);
    	//intakeEncoder = new Encoder(2,3,false,Encoder.EncodingType.k4X);
    	//armEncoder = new Encoder(4,5,false,Encoder.EncodingType.k4X);
    	
    	stick = new Joystick(0);
        xbox =	 new Joystick(1);
    	_intake = new Intake(stick, _intakeMotor, intakeSwitch);
    	_arm = new Arm(_armMotor,xbox);
    	_gyro = new ADXRS450_Gyro();
    	_gyro.reset();
    	_gyro.calibrate();
    	
//    	lineSensor = new DigitalInput(0);
    	
    	//camera1 = CameraServer.getInstance();
    	//camera1.setQuality(50);
    	//camera1.startAutomaticCapture("cam0");
    	/*camera2 = CameraServer.getInstance();
    	camera2.setQuality(50);
    	camera2.startAutomaticCapture("cam1");*/
    	
    	//gyro = new SPI(0);
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	System.out.println("autonomousInit: STARTED");
    	
    	SmartDashboard.putString("test", "Test");
    	
    	autoLoopCounter = 0;
    	
    	//intakeEncoder.reset();
    	
    	//arm.init();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
/*    	if (lineSensor.get() && autoLoopCounter < 250)
    	{
    		myRobot.drive(-0.5, 0.0); 	// drive forwards half speed
			autoLoopCounter++;
    	}
    	
    	else 
    	{
			myRobot.drive(0.0, 0.0); 	// stop robot
		}
		if (lineSensor.get())
		{
			System.out.println("True");
		}
		else
		{
			System.out.println("False");
		}*/
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	System.out.println("teleopInit: Called");
    	//arm.init();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() 
    {
    	drive();
    	_intake.periodic();
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
    	
    	//if (xbox.getRawAxis(0) < 0) arm.set(false);
    	//if (xbox.getRawAxis(0) > 0) arm.set(true);
    	
    	//System.out.println("Arm encoder distance: " + armEncoder.getDistance());
    	//System.out.println("Intake encoder distance: " + armEncoder.getDistance());
    	//System.out.println("teleopPeriodic: Stick x = " + stick.getX() + " y = " + stick.getY());
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
    	
    	//System.out.println("speedModifier:'" + speedModifier +"'");
    	
    	//Gets stick values and sets variables for it
    	double steering = stick.getX() * speedModifier;
    	double power = stick.getY() * speedModifier;
    	
    	//Applies above variables to variables that will be applied to the motors
    	double leftPower = -power + -steering;
    	double rightPower = power + -steering;
    	
    	
    	//Displays the above variables (the motor powers)
    	//System.out.println("leftPower:'" + leftPower +"'");
    	//System.out.println("rightPower:'" + rightPower +"'");
    			
    	
    	//Sends the power variables to the motors
    	driveLeft.set(leftPower);
    	driveRight.set(rightPower);
    }
}