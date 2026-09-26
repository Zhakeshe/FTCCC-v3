package org.firstinspires.ftc.teamcode.teleop;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Robot;
import org.firstinspires.ftc.teamcode.robot.Shooter;

@TeleOp(name="teleeop11")
public class Teleeop11 extends LinearOpMode {
    private Robot robot;
    private Intake intake;
    private Shooter shooter;
    private Follower follower;

    @Override
    public void runOpMode() {
        robot=new Robot(hardwareMap);
        intake=new Intake(hardwareMap);
        shooter=new Shooter(hardwareMap);
        boolean shooterOn=false;
        boolean bumperWasDown=false;
        follower=Constants.createFollower(hardwareMap);

        telemetry.addLine("teleeop11");
        telemetry.addLine("field centric");
        telemetry.update();

        waitForStart();
        follower.update();
        double fieldHeading=follower.pose().heading();

        while(opModeIsActive()) {
            follower.localizer.update();

            double y=stick(-gamepad1.left_stick_y);
            double x=stick(gamepad1.left_stick_x);
            double rx=stick(gamepad1.right_stick_x);

            double speed=Constants.DRIVE_SPEED;

            if(gamepad1.left_stick_button) {
                speed=Constants.SLOW_SPEED;
            }

            double heading=follower.pose().heading();
            double turn=rx*speed;

            double relative=wrap(heading-fieldHeading);
            double cos=Math.cos(relative);
            double sin=Math.sin(relative);
            double strafe=x*cos-y*sin;
            double forward=x*sin+y*cos;
            robot.drive.drive(forward, strafe, turn, speed);

            if(gamepad1.left_trigger>0.3) {
                intake.setStopper_open();
            }
            else {
                intake.setStopper_close();
            }
            if(gamepad1.left_bumper)  {
                intake.in();
            }else if(gamepad1.a) {
                intake.out();
            }else {
                intake.stop();
            }

            if(gamepad1.right_bumper && !bumperWasDown) {
                shooterOn=!shooterOn;
            }
            bumperWasDown=gamepad1.right_bumper;
            shooter.setTargetVelocity(shooterOn ? Constants.SHOOTER_TARGET_VELOCITY : 0);
            shooter.update();

            telemetry.addData("X",follower.pose().x());
            telemetry.addData("Y",follower.pose().y());
            telemetry.addData("Heading",
                    Math.toDegrees(follower.pose().heading()));
            telemetry.addData("Intake",gamepad1.left_stick_button ? 1 : (gamepad1.left_trigger>0.2 ? -gamepad1.left_trigger : 0));
            telemetry.addData("Shooter",shooter.getPower());
            telemetry.addData("Shooter target",shooter.getTargetVelocity());
            telemetry.addData("Shooter speed",shooter.getSpeed());
            telemetry.addData("Shooter L",shooter.getLeftSpeed());
            telemetry.addData("Shooter R",shooter.getRightSpeed());
            telemetry.update();
        }
    }

    private double stick(double value) {
        if(Math.abs(value)<Constants.STICK_DEADZONE) {
            return 0;
        }
        return value*value*value;
    }

    private double wrap(double radians) {
        while(radians>Math.PI) radians-=2*Math.PI;
        while(radians<-Math.PI) radians+=2*Math.PI;
        return radians;
    }
}
