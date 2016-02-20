package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;

/*
 * Controls arm motor with an xBox controller.
 * If axis is greater than 0, arm moves up.
 * If axis is less than 0, arm moves down.
 * If it reaches any limit, it will automatically stop moving.
 */
public class Arm {
	
	private boolean up;
	private VictorSP _motor;
	private DigitalInput _limitSwitchUp;
	private DigitalInput _limitSwitchDown;
	private Encoder _encoder;
	private final double SPEED_MODIFIER = 0.75;//0.25;
	private Joystick _xBox;
	
	public Arm(VictorSP MotorIn, Joystick XboxIn) 
	{
    	//encoder = new Encoder(0, 1);
    	//encoder.setDistancePerPulse(10); //Can be any unit
    	_limitSwitchUp = new DigitalInput(3);
    	_limitSwitchDown = new DigitalInput(2);
    	_motor = MotorIn;
    	_xBox = XboxIn;
    	up = true;
	}
	
	private double GetYAxis()
	{
		return _xBox.getRawAxis(1);
		
	}
	
/*	private double getArmPower()
	{
		if(_xBox.getRawButton(button))
	}
	*/
	public void set(boolean setUp)
	{
		up = setUp;
	}
	
	public boolean get()
	{
		return up;
	}
	
	public void init()
	{
		//encoder.reset();
	}
	
    public void periodic()
    {
    	//If up limit reached and trying to move up, stop moving
    	//If down limit reached and trying to move down, stop moving
    	
    	// May want to change to relative instead of absolute control i.e. 
    	// instead of 'up' or 'down', 'move up' and 'move down'. This will depend
    	// on motor speed and what the arm needs to do.
    	//System.out.println("XBox: " + GetYAxis());    	
    	if (GetYAxis() != 0) 
    	{
        	if (GetYAxis() > 0.5)// && !_limitSwitchUp.get())
        	{
        		_motor.set(SPEED_MODIFIER);
        	}    	
	
	    	else if (GetYAxis() < -0.5)// && !_limitSwitchDown.get())
	    	{	
				_motor.set(-SPEED_MODIFIER);
	    	}
	    	else
	    	{
	    		//Do nothing
	    		_motor.set(0);
	    	}
    	}
    	else
    	{
    		//Do nothing
    		_motor.set(0);
    	}
    	
    	
    	
    	/*
    	if (up && !_limitSwitchUp.get())
    	{
    		_motor.set(SPEED_MODIFIER);
    	}
    	else if (!up && !_limitSwitchDown.get())
    	{	
			_motor.set(-SPEED_MODIFIER);
    	}
    	else
    	{
    		System.out.println("Limit reached!");
    		_motor.set(0);
    	}*/
    }
}
