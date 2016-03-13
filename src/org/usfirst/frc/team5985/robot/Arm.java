package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

/*
 * Controls arm motor with an xBox controller.
 * If axis is greater than 0, arm moves up.
 * If axis is less than 0, arm moves down.
 * If it reaches any limit and tries to keep moving, it will automatically stop.
 */
public class Arm {
	
	private VictorSP _motor;
	private DigitalInput _limitSwitch;
	//private Encoder _encoder;
	private final double ARM_SPEED = 0.75;//1;
	private double preset = -1;
	
	public Arm(int MotorIn) 
	{
		System.out.println("Arm Constructor Called!");
    	//encoder = new Encoder(0, 1);
    	//encoder.setDistancePerPulse(10); //Can be any unit
    	_limitSwitch = new DigitalInput(3);
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
    	
    	double power = 0;
    	
    	if (driverStation.xbox.getRawButton(1))
    	{
    		preset = 1;
    	}
    	else if (driverStation.xbox.getRawButton(2))
    	{
    		preset = 2;
    	}
    	else if (driverStation.xbox.getRawButton(3))
    	{
    		preset = 3;
    	}
    	else if (driverStation.xbox.getRawButton(4))
    	{
    		preset = 4;
    	}
    	
    	if (driverStation.xbox.getRawAxis(1) > 0.25 || driverStation.xbox.getRawAxis(1) < -0.25 || preset > 0)
    	{
    		if (driverStation.xbox.getRawAxis(1) > 0.25)
        	{
        		//down
        		power = -driverStation.xbox.getRawAxis(1);
        		preset = -1;
        	}    	
       		else if (driverStation.xbox.getRawAxis(1) < -0.25)
       		{	
       			//up
       			power = -driverStation.xbox.getRawAxis(1);
       			preset = -1;
       			}
       		else if (preset == 4)
       		{
       			//up
       			power = 1;
       			preset = 4;
       			if (!_limitSwitch.get())
       			{
       				preset = -1;
       			}
       		}
       		else if (preset == 1)
       		{
       			/*if (encoder = near ground)
       			{
       				power = 0;
       			}
       			else if (encoder lower than near ground)
       			{
       				power = 1
       			}
       			else if (encoder higher than near ground)
       			{
       				power = -1
       			}
       			*/
       			preset = 1;	
       		}
       		else if (preset == 3)
       		{
       			/*if (encoder = drawbridge)
       			{
       				power = 0;
       			}
       			else if (encoder lower than drawbridge)
       			{
       				power = 1
       			}
       			else if (encoder higher than drawbridge)
       			{
       				power = -1
       			}
       			*/
       			preset = 3;	
       		}
       		else if (preset == 2)
       		{
       			/*if (encoder = low bar)
       			{
       				power = 0;
       			}
       			else if (encoder lower than low bar)
       			{
       				power = 1
       			}
       			else if (encoder higher than low bar)
       			{
       				power = -1
       			}
       			*/
       			preset = 2;	
       		}
    		if (!_limitSwitch.get() && power > 0)
    		{
    			power = 0;
    		}
    		_motor.set(power * ARM_SPEED);
    	}
    	else
    	{
    		_motor.set(0);
    	}
    	/*if (!_limitSwitch.get())
		{
    		driverStation.xbox.setRumble(RumbleType.kLeftRumble, 1);
    		driverStation.xbox.setRumble(RumbleType.kRightRumble, 1);
		}
		else
		{
			driverStation.xbox.setRumble(RumbleType.kLeftRumble, 0);
    		driverStation.xbox.setRumble(RumbleType.kRightRumble, 0);
		}*/
    }
    public boolean armUp()
	{
		return _limitSwitch.get();
	}
}