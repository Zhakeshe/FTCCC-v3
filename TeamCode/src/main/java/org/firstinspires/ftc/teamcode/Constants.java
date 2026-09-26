package org.firstinspires.ftc.teamcode;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    // =========================
    // HARDWARE NAMES
    // =========================

    public static final String FRONT_LEFT = "frontLeft";
    public static final String FRONT_RIGHT = "frontRight";
    public static final String BACK_LEFT = "backLeft";
    public static final String BACK_RIGHT = "backRight";

    public static final String INTAKE = "intake";
    public static final String SHOOTER_LEFT = "shooterLeft";
    public static final String SHOOTER_RIGHT = "shooterRight";

    // public static final String LIFT = "lift";
    // public static final String CLAW = "claw";

    public static final String PINPOINT = "pinpoint";


    // =========================
    // TELEOP
    // =========================
    public static final double CM = 1.0 / 2.54;

    public static double cm(double value) {
        return value * CM;
    }

    public static final double DRIVE_SPEED = 1.0;
    public static final double SLOW_SPEED = 0.35;
    public static final double STICK_DEADZONE = 0.06;

    public static final double INTAKE_IN = 1.0;
    public static final double INTAKE_OUT = -1.0;
    public static final double SHOOTER_POWER = 1.0;
    public static final double SHOOTER_RAMP = 0.03;
    public static final double SHOOTER_TELEOP_RPM = 2570;
    public static final double SHOOTER_AUTO_RPM = 2620;
    public static final double SHOOTER_TICKS_PER_REV = 28;
    public static final double SHOOTER_TARGET_VELOCITY = SHOOTER_TELEOP_RPM * SHOOTER_TICKS_PER_REV / 60.0;
    public static final double SHOOTER_AUTO_VELOCITY = SHOOTER_AUTO_RPM * SHOOTER_TICKS_PER_REV / 60.0;
    public static final double SHOOTER_KP = 0.0018;
    public static final double SHOOTER_KI = 0.0015;
    public static final double SHOOTER_KD = 0.00001;
    public static final double SHOOTER_KF = 0.00045;
    public static final double SHOOTER_VELOCITY_TOLERANCE = 50;

    public static final double LIFT_POWER = 1.0;


    // =========================
    // SERVO
    // =========================

    public static final double CLAW_OPEN = 0.8;
    public static final double CLAW_CLOSED = 0.2;


    // ============= ============
    // LIFT ENCODER POSITIONS
    // =========================

    public static final int LIFT_HOME = 0;
    public static final int LIFT_LOW = 500;
    public static final int LIFT_MIDDLE = 1000;
    public static final int LIFT_HIGH = 2000;


    public static MecanumConfig drivetrainConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set(FRONT_LEFT);
                c.backLeftName.set(BACK_LEFT);
                c.frontRightName.set(FRONT_RIGHT);
                c.backRightName.set(BACK_RIGHT);

                c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

                c.manualBrakeMode.set(true);
            }
    );

    public static PinpointConfig localizerConfig = new PinpointConfig(
            c -> {
                c.name.set(PINPOINT);
                c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
                c.xPodOffset.set(-1.456414921077218);
                c.yPodOffset.set(-3.3368187251053456);
                c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
                c.globalDistanceUnit.set(DistanceUnit.INCH);
                c.offsetUnits.set(DistanceUnit.INCH);
            }
    );

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.6767843383060114);
                Controller secondaryTranslationalForward = Controller.proportional(0.2500537289934937);
                Controller primaryTranslationalLateral = Controller.proportional(0.3383921691530057);
                Controller secondaryTranslationalLateral = Controller.proportional(0.12502686449674685);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.10306242393124564));
                c.brake.set(Controller.proportionalFeedforward(0.08760306034155879));

                c.headingFeedback.set(Controller.proportional(4.585356311822844));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.052731679948382994, 0.005069471550732003));

                c.linearBrakeCoefficients.set(Matrix.diag(0.05514370961225634, 0.057905256156468445));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0024170890928622546, 0.0018646901024437967));

                c.maxAchievableForwardVelocity.set(75.85630666002064);
                c.maxAchievableStrafeVelocity.set(63.191262849021754);
                c.naturalForwardDeceleration.set(33.39617601445137);
                c.naturalStrafeDeceleration.set(60.41841415288795);
            }
    );

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new Follower(
                new PinpointLocalizer(hardwareMap, localizerConfig),
                new Mecanum(hardwareMap, drivetrainConfig),
                new Foresight(foresightConfig)
        );
    }

}
