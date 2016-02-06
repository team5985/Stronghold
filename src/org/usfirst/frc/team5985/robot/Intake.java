package org.usfirst.frc.team5985.robot;

import edu.wpi.first.wpilibj.VictorSP;

public class Intake {

	private VictorSP motor;
	
	public Intake(){
		//init
		motor = new VictorSP(9);
	}
	
	public void periodic(){
		
	}
	
	public void act(int action) {
		if (action == -1){
			//take in
		} else if (action == 0){
			//push out
		} else if (action == 1){
			//stop
		} else {
			// log error
		}
	}
}
