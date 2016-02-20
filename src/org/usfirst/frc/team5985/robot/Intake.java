package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;

/*
 * Class: Intake
 * Controls the intake mechanism on 5985 Stronghold robot
 */

public class Intake {
	
	private Joystick _stick;
	private VictorSP _motor;
	private DigitalInput _intakeLimitSwitch;
	int state = 0;
	
	/*
	 * Constructor: pass in joystick, intake motor, and intake limit switch
	 */
	public Intake(Joystick stickIn, VictorSP motorIn, DigitalInput switchIn){
		_stick = stickIn; 
		_motor = motorIn;
		_intakeLimitSwitch = switchIn;
    	
	}
	
    /**
     * This function is called periodically during operator control
     * 
     * Operates the intake during teleop. Uses _intakeLimitSwitch to know if it has the ball.
     * Button 1 sucks in the ball, unless it has the ball already.
     * button 2 shoots the ball.
     */
	public void periodic()
	{
		// Ball not completely in Intake	
		if (_intakeLimitSwitch.get())
		{
			//Suck in ball
			if (_stick.getRawButton(1))
			{
				_motor.set(-0.4);
			}
			//Shoot out ball
			else if (_stick.getRawButton(2))
			{
				_motor.set(1);
			}
			//Nothing
			else
			{
				//_motor.set(0);
				_motor.stopMotor();
			}
				
		}
		//Ball completely in intake
		else if (!_intakeLimitSwitch.get())
		{
			//Shoot out ball
			if (_stick.getRawButton(2))
			{
				_motor.set(1);
			}
			//Nothing
			else
			{
				//_motor.set(0);
				_motor.stopMotor();
				
			}
		}
			
	}
	
	public int get() {
		return state;
	}
	
	public void set(int state) {
		this.state = state;
	}
	
}
