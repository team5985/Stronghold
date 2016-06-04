package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/*
 * Class: Intake
 * Controls the intake mechanism on 5985 Stronghold robot
 */

public class Intake {
	
	
	private VictorSP _motor;
	//private DigitalInput _intakeLimitSwitch;
	private I2C _I2C;
	
	private boolean ball;
	/*
	 * Constructor: pass in joystick, intake motor, and intake limit switch
	 */
	public Intake(int motorIn_PWM_Port, int switchIn_DIO_Port)
	{
		System.out.println("Intake Constructor Called!");	
		_motor = new VictorSP(motorIn_PWM_Port);
		
		//_intakeLimitSwitch = new DigitalInput(switchIn_DIO_Port);
    	
		_I2C = new I2C(I2C.Port.kOnboard,0);
		
	}
	
    /**
     * This function is called periodically during operator control
     * 
     * Operates the intake during teleop. Uses _intakeLimitSwitch to know if it has the ball.
     * Button 1 sucks in the ball, unless it has the ball already.
     * button 2 shoots the ball.
     */
	public void handleEvents(DriverStation driverStation)
	{ 	
		byte[] leftRange = new byte[1];
		byte[] rightRange = new byte[1];
		SmartDashboard.putNumber("Left Range = ",leftRange[0]);
		SmartDashboard.putNumber("Right Range = ",rightRange[0]);
		
		boolean result1 = _I2C.read(0x0c,1,leftRange);
		boolean result2 =_I2C.read(0x0e,1,rightRange);
		
		if (result1 || result2 == false){
			System.out.println("Found a signal");
		}
		else{
			System.out.println("No Signal Found.");
		}
		SmartDashboard.putBoolean("Left Read = ",!result1);
		SmartDashboard.putBoolean("Right Read = ",!result2);
		
		int range = (leftRange[0] * rightRange[0])/20; // range ~ distance in cm
		SmartDashboard.putNumber("Range = ",range);
		if (range < 20)
		{
			ball = true;
		}
		else
		{
			ball = false;
		}
		
		if (driverStation.stick.getRawButton(1) && !ball)//_intakeLimitSwitch.get())
		{
			//button 1 and boulder not completely in: suck in boulder	
			_motor.set(-0.6);
		}
		else if (driverStation.stick.getRawButton(2))
		{
			//button 2: shoot out boulder
			_motor.set(1);
		}
		else
		{
			//no buttons or button 1 and boulder completely in: stop moving
			_motor.stopMotor();
		}
		System.out.println(/*"Limit Switch: '"*/"Gesture Sensor Proximity: '" + ball + "'");//_intakeLimitSwitch.get() + "'");
	}
	
	/**
	 * REturns state of intake
	 * @return true if intake has boulder
	 */
	public boolean hasBoulder()
	{
		return ball;//_intakeLimitSwitch.get();
	}
}
