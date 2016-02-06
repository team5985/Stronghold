package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

public class Arm {
	private boolean up;
	
	private VictorSP motor;
	
	private DigitalInput limitSwitchUp;
	private DigitalInput limitSwitchDown;
	
	private Encoder encoder;
	
	private final double SPEED_MODIFIER = 0.25;
	
	public Arm() {
    	encoder = new Encoder(0, 1);
    	encoder.setDistancePerPulse(10); //Can be any unit
    	limitSwitchUp = new DigitalInput(1);
    	limitSwitchDown = new DigitalInput(2);
    	motor = new VictorSP(9);
    	up = true;
	}
	
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
		encoder.reset();
	}
	
    public void periodic()
    {
    	//If up limit reached and trying to move up, stop moving
    	//If down limit reached and trying to move down, stop moving
    	
    	// May want to change to relative instead of absolute control i.e. 
    	// instead of 'up' or 'down', 'move up' and 'move down'. This will depend
    	// on motor speed and what the arm needs to do.
    	
    	if (up && !limitSwitchUp.get())
    	{
    		motor.set(SPEED_MODIFIER);
    	}
    	else if (!up && !limitSwitchDown.get())
    	{	
			motor.set(-SPEED_MODIFIER);
    	}
    	else
    	{
    		System.out.println("Limit reached!");
    		motor.set(0);
    	}
    }
}
