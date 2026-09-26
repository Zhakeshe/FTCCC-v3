package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class Robot {
    public Drive drive;

    // Раскомментируй после добавления устройств в Robot Configuration:
    // public Intake intake;
    // public Claw claw;
    // public Lift lift;

    public Robot(HardwareMap hardwareMap) {
        drive=new Drive(hardwareMap);

        // intake=new Intake(hardwareMap);
        // claw=new Claw(hardwareMap);
        // lift=new Lift(hardwareMap);
    }
}
