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
	private DigitalInput _limitSwitch;
	private Encoder _encoder;
	private final double ARM_SPEED = 0.75;//1;
	private double preset = -1;
	private double power = 0;
	private final double ARM_LOW = -0.25;
	private final double ARM_BALL = -0.22;
	private final double ARM_MID = -0.14;
	private final double ARM_HIGH = 0;
	
	
	public Arm(int MotorIn, int switchIn, int encoderIn, int encoderIn2) 
	{
		System.out.println("Arm Constructor Called!");
    	_encoder = new Encoder(encoderIn, encoderIn2);
    	_encoder.setDistancePerPulse(0.0007692307692307692); //1 distance unit ~~ 1 Revolution ~~ 1300 pulses
    	_limitSwitch = new DigitalInput(switchIn);
    	_motor = new VictorSP(MotorIn);
	}
	
	public void init()
	{
		_encoder.reset();
		preset = -1;
		power = 0;
	}
	public void auto(double encoderPosition)
	{
		armTarget(encoderPosition);
		_motor.set(power);
	}
    public void handleEvents(DriverStation driverStation)
    {
    	System.out.println("Xbox: " + driverStation.xbox.getRawAxis(1));    	
    	
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
       			armTarget(ARM_HIGH);
       			preset = 4;
       			}
       		else if (preset == 1)
       		{
       			armTarget(ARM_LOW);
       			preset = 1;	
       		}
       		else if (preset == 3)
       		{
       			armTarget(ARM_MID);
       			preset = 3;	
       		}
       		else if (preset == 2)
       		{

       			armTarget(ARM_BALL);
       			preset = 2;	
       		}
    		if ((!_limitSwitch.get() || _encoder.getDistance() > 0) && power > 0)
    		{
    			power = 0;
    		}
    		/*else if (_encoder.getDistance() > -0.05 && power > 0.25)
    			{
    				power = 0.25;
    			}*/
    		_motor.set(power * ARM_SPEED);
    		
    	}
    	else
    	{
    		_motor.set(0);
    	}
    	
    	driverStation.smartDashNum("Encoder Distance",_encoder.getDistance());
    	driverStation.smartDashNum("Arm Power", power);
    	
    }
    // Returns true if arm is all the way up
    public boolean armUp()
	{
		return _limitSwitch.get();
	}
    
    
    public void armTarget(double targetPulse) 
    {
    	double currentPulses = _encoder.getDistance();
    	if ((targetPulse - currentPulses) < 0.01 && (targetPulse - currentPulses) > -0.01)
    	{
    		power = 0;
    	}
    	else if ((targetPulse - currentPulses) > 0.01)
    	{
    		power = 1;
    	}
    	else if ((targetPulse - currentPulses) < -0.01)
    	{
    		power = -1;
    	}
    }
}