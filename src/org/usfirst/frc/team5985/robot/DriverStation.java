package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverStation {

	Joystick stick;
	Joystick xbox;
	
	public void driverInit(CameraServer camera1) {
		
		stick = new Joystick(0);
        xbox =	 new Joystick(1);
        
        camera1 = CameraServer.getInstance();
    	camera1.setQuality(50);
    	camera1.startAutomaticCapture("cam0");
		
	}
	public void driverPeriodic() {
		
		//Feeds stick + xbox info to Smart Dashboard
		
		SmartDashboard.putNumber("Stick X Value (Steering)", stick.getX());
    	SmartDashboard.putNumber("Stick Y Value (Power)", stick.getY());
    	SmartDashboard.putBoolean("Stick Button 1 (Intake In)", stick.getRawButton(1));
    	SmartDashboard.putBoolean("Stick Button 2 (Intake Out)", stick.getRawButton(2));
    	SmartDashboard.putNumber("Xbox Y Value (Arm)", xbox.getRawAxis(1));

    	
    	
    	
	
	}
	
}
