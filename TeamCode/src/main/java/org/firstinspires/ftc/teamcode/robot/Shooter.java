package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Constants;

public class Shooter {
    private final DcMotorEx left;
    private final DcMotorEx right;
    private final PidfController controller;
    private final ElapsedTime timer = new ElapsedTime();
    private double targetVelocity;
    private double power;
    private double speed;
    private double leftSpeed;
    private double rightSpeed;

    public Shooter(HardwareMap hardwareMap) {
        left = hardwareMap.get(DcMotorEx.class, Constants.SHOOTER_LEFT);
        right = hardwareMap.get(DcMotorEx.class, Constants.SHOOTER_RIGHT);

        left.setDirection(DcMotor.Direction.REVERSE);
        right.setDirection(DcMotor.Direction.FORWARD);

        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        controller = new PidfController(
                Constants.SHOOTER_KP,
                Constants.SHOOTER_KI,
                Constants.SHOOTER_KD,
                Constants.SHOOTER_KF
        );
        timer.reset();
    }

    public void setTargetVelocity(double ticksPerSecond) {
        targetVelocity = Math.max(0, ticksPerSecond);
    }

    public void update() {
        double dt = timer.seconds();
        timer.reset();
        leftSpeed = Math.abs(left.getVelocity());
        rightSpeed = Math.abs(right.getVelocity());
        speed = feedbackSpeed();

        if (targetVelocity <= 0) {
            controller.reset();
            power = 0;
        } else {
            power = controller.calculate(targetVelocity, speed, dt);
            power = Math.max(0, Math.min(1, power));
        }
        left.setPower(power);
        right.setPower(power);
    }

    private double feedbackSpeed() {
        if (leftSpeed > 80 && rightSpeed > 80) {
            return Math.min(leftSpeed, rightSpeed);
        }
        return Math.max(leftSpeed, rightSpeed);
    }

    public double getPower() {
        return power;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getLeftSpeed() {
        return leftSpeed;
    }

    public double getRightSpeed() {
        return rightSpeed;
    }

    public boolean isAtSpeed() {
        return targetVelocity > 0
                && Math.abs(speed - targetVelocity) <= Constants.SHOOTER_VELOCITY_TOLERANCE;
    }
}
