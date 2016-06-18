package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.VictorSP;


/*
 * Class: Intake
 * Controls the intake mechanism on 5985 Stronghold robot
 */

public class Intake {
	
	
	private VictorSP _motor;
	private DigitalInput _intakeLimitSwitch;
	
	
	
	/*
	 * Constructor: pass in joystick, intake motor, and intake limit switch
	 */
	public Intake(int motorIn_PWM_Port, int switchIn_DIO_Port)
	{
		System.out.println("Intake Constructor Called!");	
		_motor = new VictorSP(motorIn_PWM_Port);
		
		_intakeLimitSwitch = new DigitalInput(switchIn_DIO_Port);
    	
	}
	
    /**
     * This function is called periodically during operator control
     * 
     * Operates the intake during teleop. Uses _intakeLimitSwitch to know if it has the ball.
     * Button 1 sucks in the ball, unless it has the ball already.
     * button 2 shoots the ball.
     */
	public void handleEvents(PBDriverStation driverStation)
	{ 	
		if (driverStation.stick.getRawButton(1) && !_intakeLimitSwitch.get())
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
		System.out.println("Limit Switch: '" + _intakeLimitSwitch.get() + "'");
	}
	
	/**
	 * REturns state of intake
	 * @return true if intake has boulder
	 */
	public boolean hasBoulder()
	{
		return _intakeLimitSwitch.get();
	}
}
