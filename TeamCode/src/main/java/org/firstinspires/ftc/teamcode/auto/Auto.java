package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Shooter;

@Autonomous(name="Auto")
public class Auto extends AutoBase {
    // Дюймы, как в Pedro. Heading в градусах, linear.
    private static final double START_X = 53.33948545861298;
    private static final double START_Y = 7.834731543624155;
    private static final double START_H = 270;

    private static final double COLLECT_CONTROL_X = 35.09284116;
    private static final double COLLECT_CONTROL_Y = 36.05033557;
    private static final double COLLECT_X = 11.55425055 - 3.5 / 2.54;
    private static final double COLLECT_Y = 3.402964205;
    private static final double COLLECT_H = 180;

    private static final double SHOOT2_CONTROL_X = 11.88255033;
    private static final double SHOOT2_CONTROL_Y = 113.0659955;
    private static final double SHOOT2_X = 57.13814317;
    private static final double SHOOT2_Y = 130.3414429;
    private static final double SHOOT2_H = 90;

    private static final double PARK_X = 8.228187919;
    private static final double PARK_Y = 107.7628635;
    private static final double PARK_H = 0;

    private static final double FEED_SEC = 0.60;
    private static final int SHOOT_PAIRS = 2;
    private static final double INTAKE_SEC = 1.5;
    private static final double SPEED_HOLD_SEC = 1.0;
    private static final double FIRST_EXTRA_SEC = 0.5;
    private static final double CLOSE_PAUSE_SEC = 0.2;
    private static final double NEXT_HOLD_SEC = 0.30;

    private static final String[] NAMES = {
            "spin start",
            "shoot start",
            "to collect",
            "collect",
            "to shoot2",
            "spin shoot2",
            "shoot2",
            "to park",
            "park"
    };

    private Intake intake;
    private Shooter shooter;
    private final ElapsedTime timer = new ElapsedTime();
    private final ElapsedTime speedHold = new ElapsedTime();
    private int state = 0;
    private int entered = -1;
    private int pairIndex;
    private boolean feeding;

    @Override
    public void runOpMode() {
        initRobot();
        intake = new Intake(hardwareMap);
        shooter = new Shooter(hardwareMap);

        follower.setPose(new Pose(START_X, START_Y, Math.toRadians(START_H)));
        intake.setStopper_close();
        intake.stop();
        shooter.setTargetVelocity(0);

        telemetry.addLine("Auto");
        telemetry.update();

        waitForStart();
        if (isStopRequested()) return;

        while (opModeIsActive()) {
            follower.update();
            shooter.update();

            if (entered != state) {
                entered = state;
                timer.reset();
                enter(state);
            } else {
                loopState(state);
            }

            telemetry.addData("State", state < NAMES.length ? NAMES[state] : "done");
            telemetry.addData("Shooter target", shooter.getTargetVelocity());
            telemetry.addData("Shooter speed", shooter.getSpeed());
            telemetry.addData("Shooter power", shooter.getPower());
            telemetry.addData("X", follower.pose().x());
            telemetry.addData("Y", follower.pose().y());
            telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
            telemetry.update();
        }

        intake.stop();
        intake.setStopper_close();
        shooter.setTargetVelocity(0);
        shooter.update();
    }

    private void enter(int state) {
        switch (state) {
            case 0:
                shooter.setTargetVelocity(Constants.SHOOTER_AUTO_VELOCITY);
                intake.setStopper_close();
                intake.stop();
                holdHere();
                break;
            case 1:
                intake.setStopper_close();
                intake.stop();
                pairIndex = 0;
                feeding = false;
                break;
            case 2:
                intake.setStopper_close();
                intake.in();
                followCurve(COLLECT_CONTROL_X, COLLECT_CONTROL_Y, COLLECT_X, COLLECT_Y, COLLECT_H);
                break;
            case 3:
                holdHere();
                intake.setStopper_close();
                intake.in();
                break;
            case 4:
                intake.stop();
                shooter.setTargetVelocity(Constants.SHOOTER_AUTO_VELOCITY);
                followCurve(SHOOT2_CONTROL_X, SHOOT2_CONTROL_Y, SHOOT2_X, SHOOT2_Y, SHOOT2_H);
                break;
            case 5:
                holdHere();
                intake.setStopper_close();
                intake.stop();
                shooter.setTargetVelocity(Constants.SHOOTER_AUTO_VELOCITY);
                break;
            case 6:
                intake.setStopper_close();
                intake.stop();
                pairIndex = 0;
                feeding = false;
                break;
            case 7:
                intake.stop();
                intake.setStopper_close();
                shooter.setTargetVelocity(0);
                followLine(PARK_X, PARK_Y, PARK_H);
                break;
            default:
                intake.stop();
                intake.setStopper_close();
                shooter.setTargetVelocity(0);
                holdHere();
                break;
        }
    }

    private void loopState(int state) {
        switch (state) {
            case 0:
            case 5:
                if (shooterHeld(SPEED_HOLD_SEC)) this.state = state + 1;
                break;
            case 1:
            case 6:
                feedPair(state);
                break;
            case 2:
            case 4:
            case 7:
                if (!follower.isBusy()) this.state = state + 1;
                break;
            case 3:
                if (timer.seconds() >= INTAKE_SEC) this.state = 4;
                break;
            default:
                break;
        }
    }

    private boolean shooterHeld(double holdSec) {
        double target = shooter.getTargetVelocity();
        boolean near = target > 0
                && Math.abs(shooter.getSpeed() - target) <= Constants.SHOOTER_VELOCITY_TOLERANCE;
        if (!near) {
            speedHold.reset();
            return false;
        }
        return speedHold.seconds() >= holdSec;
    }

    private void feedPair(int state) {
        if (pairIndex >= SHOOT_PAIRS) {
            intake.stop();
            intake.setStopper_close();
            this.state = state + 1;
            return;
        }
        if (!feeding) {
            intake.setStopper_close();
            intake.stop();
            if (pairIndex > 0 && timer.seconds() < CLOSE_PAUSE_SEC) return;
            double hold = pairIndex == 0 ? SPEED_HOLD_SEC + FIRST_EXTRA_SEC : NEXT_HOLD_SEC;
            if (shooterHeld(hold)) {
                feeding = true;
                timer.reset();
            }
            return;
        }
        if (timer.seconds() < FEED_SEC) {
            intake.setStopper_open();
            intake.in();
            return;
        }
        intake.setStopper_close();
        intake.stop();
        feeding = false;
        pairIndex++;
        timer.reset();
        speedHold.reset();
    }
}
