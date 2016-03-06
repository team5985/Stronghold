package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.VictorSP;

/**
 * Testing how the motor controller works.
 * 
 * Determine how many pulses of the motor turns a specific number of degrees.
 * 
 * @author FRC5985
 *
 */
public class PBEncoder {
	
	private VictorSP _motor;
	private Encoder _encoder;
	

	/**
	 * The motor controller will be created and an encoder to count the motor pulses.
	 * 
	 * @param pwmMotorPort
	 * @param encoderPortA
	 * @param encoderPortB
	 */
	public PBEncoder(int pwmMotorPort, int encoderPortA, int encoderPortB) {
		
		_motor =new VictorSP(pwmMotorPort);
		
		_encoder = new Encoder(encoderPortA, encoderPortB);
		
    	_encoder.setDistancePerPulse(10); //Can be any unit
		
	}
	
	/**
	 * Reset the count of the encoder.
	 */
	public void reset()
	{
		_encoder.reset();
		
		
		
	}
	

	public int getRawCount()
	{
		return _encoder.getRaw();
	}
	
	public void setSpeed(double speed)
	{
		_motor.set(speed);
	}
	
}
