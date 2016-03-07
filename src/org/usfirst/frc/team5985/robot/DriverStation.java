package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

public class DriverStation {

	Joystick stick;
	Joystick xbox;
	
	public void teleopInit(Robot robot) {
		
		stick = new Joystick(0);
        xbox =	 new Joystick(1);
        
        robot.camera1 = CameraServer.getInstance();
    	robot.camera1.setQuality(50);
    	robot.camera1.startAutomaticCapture("cam0");
		
	}
	public void teleopPeriodic() {
		
		//Feeds stick + xbox info to Smart Dashboard
		
		SmartDashboard.putNumber("Stick X Value (Steering)", stick.getX());
    	SmartDashboard.putNumber("Stick Y Value (Power)", stick.getY());
    	SmartDashboard.putBoolean("Stick Button 1 (Intake In)", stick.getRawButton(1));
    	SmartDashboard.putBoolean("Stick Button 2 (Intake Out)", stick.getRawButton(2));
    	SmartDashboard.putNumber("Xbox Y Value (Arm)", xbox.getRawAxis(1));
    	
    	
    	
	
	}
	
}
