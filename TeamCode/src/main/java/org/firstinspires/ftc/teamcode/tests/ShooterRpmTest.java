package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Shooter;

@TeleOp(name = "Shooter RPM Test")
public class ShooterRpmTest extends LinearOpMode {
    private static final double MIN_RPM = 2000;
    private static final double MAX_RPM = 4000;
    private static final double STEP_RPM = 10;
    private static final double TICKS_PER_REV = 28;
    private static final double GEAR = 1;
    private static final double REPEAT_SEC = 0.25;

    private Intake intake;
    private Shooter shooter;
    private final ElapsedTime stepTimer = new ElapsedTime();
    private double rpm = MIN_RPM;
    private boolean stepArmed = true;

    @Override
    public void runOpMode() {
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);
        intake.setStopper_close();
        intake.stop();

        telemetry.addLine("Shooter RPM Test");
        telemetry.addData("RPM", rpm);
        telemetry.addLine("Right stick up/down = +/- 5");
        telemetry.update();

        waitForStart();
        stepTimer.reset();

        while (opModeIsActive()) {
            stepRpm();

            if (gamepad1.left_trigger > 0.3) {
                intake.setStopper_open();
            } else {
                intake.setStopper_close();
            }
            if (gamepad1.left_bumper) {
                intake.in();
            } else if (gamepad1.a) {
                intake.out();
            } else {
                intake.stop();
            }

            shooter.setTargetVelocity(ticksPerSecond(rpm));
            shooter.update();

            telemetry.addData("Target RPM", rpm);
            telemetry.addData("Measured RPM", measuredRpm(shooter.getSpeed()));
            telemetry.addData("Ticks/sec", shooter.getSpeed());
            telemetry.addData("Left", shooter.getLeftSpeed());
            telemetry.addData("Right", shooter.getRightSpeed());
            telemetry.addData("Target ticks", ticksPerSecond(rpm));
            telemetry.addData("Power", shooter.getPower());
            telemetry.update();
        }

        intake.stop();
        intake.setStopper_close();
        shooter.setTargetVelocity(0);
        shooter.update();
    }

    private void stepRpm() {
        double stick = -gamepad1.right_stick_y;
        int direction = 0;
        if (stick > 0.5) {
            direction = 1;
        } else if (stick < -0.5) {
            direction = -1;
        }

        if (direction == 0) {
            stepArmed = true;
            stepTimer.reset();
            return;
        }

        if (stepArmed || stepTimer.seconds() >= REPEAT_SEC) {
            rpm = Math.max(MIN_RPM, Math.min(MAX_RPM, rpm + direction * STEP_RPM));
            stepArmed = false;
            stepTimer.reset();
        }
    }

    private double ticksPerSecond(double targetRpm) {
        return targetRpm * TICKS_PER_REV * GEAR / 60.0;
    }

    private double measuredRpm(double ticksPerSecond) {
        return ticksPerSecond * 60.0 / (TICKS_PER_REV * GEAR);
    }
}
