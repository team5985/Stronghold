package org.usfirst.frc.team5985.robot;

//hey guys i almost changed this
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CameraServer;
//import edu.wpi.first.wpilibj.AnalogGyro;
//import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	
	Joystick stick;
	Joystick xbox;
	
	CameraServer camera1;
	//CameraServer camera2;
	
	Victor driveLeft;
	Victor driveRight;
	VictorSP intakeMotor;
	VictorSP armMotor;
	
	DigitalInput lineSensor;
	DigitalInput intakeLimitSwitchUp;
	DigitalInput intakeLimitSwitchDown;
	DigitalInput armLimitSwitchUp;
	DigitalInput armLimitSwitchDown;
	
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
    	
    	stick = new Joystick(0);
    	xbox = new Joystick(1);
    	
    	intakeLimitSwitchUp = new DigitalInput(3);
    	intakeLimitSwitchDown = new DigitalInput(4);
    	intakeMotor = new VictorSP(2);
    	
    	armLimitSwitchUp = new DigitalInput(1);
    	armLimitSwitchDown = new DigitalInput(2);
    	armMotor = new VictorSP(9);
    	
//    	lineSensor = new DigitalInput(0);
    	
    	camera1 = CameraServer.getInstance();
    	camera1.setQuality(50);
    	camera1.startAutomaticCapture("cam0");
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
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
    	//drive();
    	
    	//intake();
    	
    	arm();
    	
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
    	
    	System.out.println("speedModifier:'" + speedModifier +"'");
    	
    	//Gets stick values and sets variables for it
    	double steering = stick.getX() * speedModifier;
    	double power = stick.getY() * speedModifier;
    	
    	//Applies above variables to variables that will be applied to the motors
    	double leftPower = -power + -steering;
    	double rightPower = power + -steering;
    	
    	//Displays the above variables (the motor powers)
    	System.out.println("leftPower:'" + leftPower +"'");
    	System.out.println("rightPower:'" + rightPower +"'");
    			
    	//Sends the power variables to the motors
    	driveLeft.set(leftPower);
    	driveRight.set(rightPower);
    }
    
    private void intake()
    {
    	//Operates intake
    	System.out.println("Intake");
    	
    	boolean up = true;
    	
    	if (stick.getRawButton(1))
		{
			up = false;
			System.out.println("stick.getRawButton(1) == True"); 	
		}
    	//intakeMotor.set(0.0);
    	
    	if (up)

    	{
    		System.out.println("Up"); 	
    		//Move up until limit switch hit
    		if (!intakeLimitSwitchUp.get()) 
			{
    			intakeMotor.set(-0.8);
        		System.out.println("Going Up");
			}
    		else
    		{
        		System.out.println("Up Limit Reached");
        		intakeMotor.set(0.0);
    		}
    	}
    	else
    	{
    		System.out.println("Down");
        	
    		//Move down until limit switch hit
    		if (intakeLimitSwitchDown.get()) {
    			intakeMotor.set(0.8); 
        		System.out.println("Going Down");
    		}
    		else
    		{
        		System.out.println("Down Limit Reached");
        		intakeMotor.set(0.0); 
    		} //Check
    	}
    }

    private void arm()
    {
    	//Operates arm
    	
    	//left xbox joystick up/down axis = xboxArm
    	
    	double xboxArm = xbox.getRawAxis(1);
    	
    	//If up limit reached and trying to move up, stop moving
    	//If down limit reached and trying to move down, stop moving
    	
    	if (armLimitSwitchUp.get() && xboxArm < 0) 
    	{
    		xboxArm = 0;
    		System.out.println("Up Limit Reached");
    	}
    	else if (armLimitSwitchDown.get() && xboxArm > 0)
    	{	
			xboxArm = 0;
			System.out.println("Down Limit Reached");
    	}
    	else if(xboxArm < 0.1 && xboxArm > -0.1)
    	{
    	xboxArm = 0;
    	}
    	//move arm
    	
    	System.out.println(xboxArm);
    	armMotor.set(xboxArm);
    }
}