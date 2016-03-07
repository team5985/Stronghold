package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.RumbleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;

public class DriverStation {

	Joystick stick;
	Joystick xbox;
	
	CameraServer camera1;
	
	public void driverInit() {
		
		stick = new Joystick(0);
        xbox =	 new Joystick(1);
        
        camera1 = CameraServer.getInstance();
    	camera1.setQuality(50);
    	camera1.startAutomaticCapture("cam0");
		
	}
	
	public Joystick stick()
	{
		return stick;
	}
}
