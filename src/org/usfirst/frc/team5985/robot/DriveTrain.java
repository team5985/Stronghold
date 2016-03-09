package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;

public class DriveTrain {

	Victor driveLeft;
	Victor driveRight;
	ADXRS450_Gyro gyro;
	private static final double GYRO_GAIN = 0.008;
	double modGyroAngle = gyro.getAngle() % 360;
	//Power Preset constants
	int driveType = -1;
	
	final int DRIVE_FREE = -1;
	final int DRIVE_BRAKE = 0;
	final int DRIVE_LOW = 1;
	final int DRIVE_MEDIUM = 2;
	final int DRIVE_HIGH = 3;
		
	final double LOW_POWER = 0.2;
	final double MEDIUM_POWER = 0.4;
	final double HIGH_POWER = 0.6;
	
	public DriveTrain(int leftMotorPort, int rightMotorPort){
		
		driveLeft = new Victor(leftMotorPort);
    	driveRight = new Victor(rightMotorPort);
	}
    	public void auto(double power){
    		
    		driveLeft.set(power);
    		driveRight.set(power);
    		
    	}
    	
    	public void teleopDrive(DriverStation driverStation)
        {
        	// Driving code for the robot
        	
        	
        	//Sets the speedModifier to the throttle
        	double speedModifier = getDriveModifier(driverStation);    	
        	
        	double POV = driverStation.stick.getPOV(0);
        	double turnAngle = 0;
        	
        	if (POV == -1) 
        	{
        		//Drives normally
        	
        		//Gets stick values and sets variables for it
            	double steering = driverStation.stick.getX();
            	double power = driverStation.stick.getY();
            	
            	//corrects for the stick not being 100% zeroed
            	if (steering < 0.1 && steering > -0.1)
            	{
            		steering = 0;
            	}
            	if (power < 0.1 && power > -0.1)
            	{
            		power = 0;
            	}
            	
            	//multiplies by speedModifier
            	steering = steering * speedModifier;
            	power = power * speedModifier;
            	
            	//Applies above variables to variables that will be applied to the motors
            	double leftPower = -power + steering;
            	double rightPower = power + steering;
            	
            	
            	//Displays the left and right motor powers and speed modifier
            	driverStation.smartDashNum("SpeedModifier", speedModifier);
            	driverStation.smartDashNum("Left Power", leftPower);
            	driverStation.smartDashNum("Right Power", rightPower);
            	//Sends the power variables to the motors
            	driveLeft.set(leftPower);
            	driveRight.set(rightPower);
        	
        	 }
        	else 
        	{
        		//Gyro Drive
        		//makes the robot drive in the direction of the POV at 10% of speedModifier
        		if (POV > -22.5 & POV < 22.5)
            	{
            		turnAngle = 0;
            	}
        		else if (POV > 22.5 & POV < 67.5)
            	{
            		turnAngle = 45;
            	}
        		else if (POV > 67.5 & POV < 112.5)
            	{
            		turnAngle = 90;
            	}
        		else if (POV > 112.5 & POV < 157.5)
            	{
            		turnAngle = 135;
            	}
        		else if (POV > 157.5 & POV < 202.5)
            	{
            		turnAngle = 180;
            	}
        		else if (POV > 202.5 & POV < 247.5)
            	{
            		turnAngle = -135;
            	}
        		else if (POV > 247.5 & POV < 292.5)
            	{
            		turnAngle = -90;
            	}
        		else if (POV > 292.5 & POV < 337.5)
            	{
            		turnAngle = -45;
            	}
        		gyroTurn(speedModifier,turnAngle, 45);
        	
        		driverStation.smartDashNum("Gyro Angle", gyro.getAngle());
            	driverStation.smartDashNum("Gyro Rate", gyro.getRate());	
            	driverStation.smartDashNum("Corrected Gyro Angle:", modGyroAngle);
            	
        	}
        }
    	private double getDriveModifier(DriverStation driverStation)
        {
        	double power = 0;
        	
        	//Sets the powerPreset setting
        	switch (driveType)
        	{

    			case DRIVE_FREE:
    				power = -(driverStation.stick.getThrottle() - 1 ) / 2;
    				break;
    			
        		case DRIVE_LOW: //20% Power
        			power = LOW_POWER;
        			break;
        		
        		case DRIVE_MEDIUM: //40% Power
        			power = MEDIUM_POWER;
        			break;
        			
        		case DRIVE_HIGH: //60% Power
        			power = HIGH_POWER;
        			break;
        			
        		case DRIVE_BRAKE: //0% Power
        			power = 0;
        			break;

        		default:
        			power = -(driverStation.stick.getThrottle() - 1 ) / 2;
        		break;
        			
        	}
        	
        	return power;
        }
        public void gyroTurn(double power,double target, double threshold)
        {
        	//turns to face a gyro heading, then gyro follows when within specified number of degrees
        	
        	double steering = 0;
        	
        	if (modGyroAngle < (target - 180))
    			{target = target - 360;}
        	
        	//Calculates how much to turn based on the current heading and the target heading
        	if (modGyroAngle < (target - threshold)){
        		steering = 1;
        		driveLeft.set(-power + steering);
            	driveRight.set(power + steering);
        	}
        	else if (modGyroAngle > (target + threshold)){
        		steering = -1;
        		driveLeft.set(-power + steering);
            	driveRight.set(power + steering);
        	}
        	
        	else {
        		gyroFollow(power, target);
        	}
        	
        
        }
        public void gyroFollow(double basePower,double gyroTarget)
        {
        	//proportionally drives in the direction of a gyro heading, turning to face the right direction
        	
        	double gyroPower = 0;
        	
        	if (modGyroAngle < (gyroTarget - 180))
    			{gyroTarget = gyroTarget - 360;}
        	
        	//Calculates how much to turn based on the current heading and the target heading
        	gyroPower = modGyroAngle - gyroTarget;
        	gyroPower = gyroPower * GYRO_GAIN;
        	
        	double gyroMotorPowerLeft = basePower - gyroPower;
        	double gyroMotorPowerRight = -basePower - gyroPower;
        	
        	//Makes the motors move
        	driveLeft.set(gyroMotorPowerLeft);
        	driveRight.set(gyroMotorPowerRight);

        }
        public void processButtons(DriverStation driverStation)
        {
        	if (driverStation.stick.getRawButton(11)) driveType = DRIVE_LOW;
        	else if (driverStation.stick.getRawButton(12)) driveType = DRIVE_MEDIUM;
        	else if (driverStation.stick.getRawButton(10)) driveType = DRIVE_HIGH;
        	else if (driverStation.stick.getRawButton(3)) driveType = DRIVE_BRAKE;
        	else if (driverStation.stick.getRawButton(9)) driveType = DRIVE_FREE;
        	if (driverStation.stick.getRawButton(7))
        	{
        		System.out.println("Gyro RESET");
        		gyro.reset();
        	}
        }
        
}