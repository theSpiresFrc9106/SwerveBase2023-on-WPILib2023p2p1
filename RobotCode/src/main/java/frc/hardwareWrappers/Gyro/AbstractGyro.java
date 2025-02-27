package frc.hardwareWrappers.Gyro;


public abstract class AbstractGyro {

    public abstract void reset();
    public abstract void calibrate();
    public abstract double getRate();
    public abstract double getRawAngle();
    public abstract boolean isConnected();
    public abstract double getRoll_deg();
    public abstract double getPitch_deg();
    
}
