package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

/*
 * Controls arm motor with an xBox controller.
 * If axis is greater than 0, arm moves up.
 * If axis is less than 0, arm moves down.
 * If it reaches any limit and tries to keep moving, it will automatically stop.
 */
public class Arm {
	
	private VictorSP _motor;
	private DigitalInput _limitSwitchUp;
	private DigitalInput _limitSwitchDown;
	//private Encoder _encoder;
	private final double ARM_SPEED = 0.75;//1;
	
	public Arm(int MotorIn) 
	{
		System.out.println("Arm Constructor Called!");
    	//encoder = new Encoder(0, 1);
    	//encoder.setDistancePerPulse(10); //Can be any unit
    	_limitSwitchUp = new DigitalInput(3);
    	_limitSwitchDown = new DigitalInput(2);
    	_motor = new VictorSP(MotorIn);
	}
	
	public void init()
	{
		//encoder.reset();
	}
	public void auto(double input)
	{
		_motor.set(input * ARM_SPEED);
	}
    public void handleEvents(DriverStation driverStation)
    {
    	
    	System.out.println("Xbox: " + driverStation.xbox.getRawAxis(1));    	
    	
    	if (driverStation.xbox.getRawAxis(1) > 0.25)// && !_limitSwitchDown.get())
    	{
    		//if trying to move down and limit switch pressed, stop moving
    		_motor.set(-driverStation.xbox.getRawAxis(1) * ARM_SPEED);
    	}    	

    	else if (driverStation.xbox.getRawAxis(1) < -0.25)// && !_limitSwitchUp.get())
    	{	
    		//if trying to move up and limit switch pressed, stop moving
    		_motor.set(-driverStation.xbox.getRawAxis(1) * ARM_SPEED);
    	}
    	else
    	{
    		//Do nothing
    		_motor.set(0);
    	}
    }
}
