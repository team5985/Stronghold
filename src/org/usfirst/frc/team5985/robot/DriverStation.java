package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriverStation {

	Joystick stick;
	Joystick xbox;
	
	public DriverStation(CameraServer camera1)
	{	
		try {
			stick = new Joystick(0);
			xbox = new Joystick(1);
	    }
		catch (Exception JoystickException) {
			smartDashBool("Joystick Error", true);
		}
	     
		try {
	    	camera1.setQuality(50);
	    	camera1.startAutomaticCapture("cam0");
		}
		catch (Exception Err) {
			smartDashBool("Camera Error", true);
		}
	}
	public void driverInit() {
		
	}
	public void driverPeriodic(){
		try{
			//Feeds stick + xbox info to Smart Dashboard
			smartDashNum("Stick X Value (Steering)", stick.getX());
	    	smartDashNum("Stick Y Value (Power)", stick.getY());
	    	smartDashNum("Stick Slider (Raw SpeedModifier)", -(stick.getThrottle() - 1 ) / 2);
	    	smartDashBool("Stick Button 1 (Intake In)", stick.getRawButton(1));
	    	smartDashBool("Stick Button 2 (Intake Out)", stick.getRawButton(2));
	    	smartDashNum("Xbox Y Value (Arm)", xbox.getRawAxis(1));
		} catch (Exception JoystickException){
			smartDashBool("Joystick Error", true);
		}
	}
	
	public void smartDashNum(String label, double value){
		SmartDashboard.putNumber(label, value);}
	public void smartDashBool(String label, boolean value){
		SmartDashboard.putBoolean(label, value);}
	public void smartDashString(String label, String value){
		SmartDashboard.putString(label, value);}
	public void smartDashGetNum(String label, double defaultValue){
		SmartDashboard.getNumber(label, defaultValue);}

}
