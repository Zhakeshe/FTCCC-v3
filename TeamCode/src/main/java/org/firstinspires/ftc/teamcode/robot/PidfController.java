package org.firstinspires.ftc.teamcode.robot;

public class PidfController {
    private final double kP;
    private final double kI;
    private final double kD;
    private final double kF;
    private double integral;
    private double lastError;
    private boolean hasLast;

    public PidfController(double kP, double kI, double kD, double kF) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
        this.kF = kF;
    }

    public double calculate(double target, double measured, double dt) {
        if (dt <= 0 || dt > 0.25) {
            dt = 0.02;
        }
        double error = target - measured;
        if (error > 0 && error < 300) {
            integral += error * dt;
        } else if (error < 0) {
            integral *= 0.5;
        }
        double maxIntegral = 0.15 / Math.max(kI, 1e-6);
        integral = Math.max(0, Math.min(maxIntegral, integral));
        double derivative = hasLast ? (error - lastError) / dt : 0;
        lastError = error;
        hasLast = true;
        return kF * target + kP * error + kI * integral + kD * derivative;
    }

    public void reset() {
        integral = 0;
        hasLast = false;
    }
}
