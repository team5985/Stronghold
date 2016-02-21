package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
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
	public Intake(int motorIn_PWM_Port, int switchIn_DIO_Port){
		
		_motor = new VictorSP( motorIn_PWM_Port );
		
		_intakeLimitSwitch = new DigitalInput( switchIn_DIO_Port );
    	
	}
	
    /**
     * This function is called periodically during operator control
     * 
     * Operates the intake during teleop. Uses _intakeLimitSwitch to know if it has the ball.
     * Button 1 sucks in the ball, unless it has the ball already.
     * button 2 shoots the ball.
     */
	public void periodic(Joystick _stick)
	{
		// Ball not completely in Intake	
		if (_intakeLimitSwitch.get())
		{
			//Suck in ball
			if (_stick.getRawButton(1))
			{
				//1855 on the 21.01.16 changed the power of intake in from 0.4 to 0.3
				_motor.set(-0.25);
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
	
	
}
