package frc.sim;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import frc.Constants;
import frc.hardwareWrappers.SimDeviceBanks;
import frc.hardwareWrappers.Gyro.SimGyro;

public class SimGyroSensorModel{

    SimGyro gyroSim;
    double gyroPosReading_deg;

    // todo - Does this need to change for the NavX gyro?
    //YN: idk
    // Limit what the gyro itself can read
    // Ex for ADSRX453 - https://www.analog.com/media/en/technical-documentation/data-sheets/ADXRS453.pdf
    final double GYRO_RATE_SCALING_DEGPERSEC_PER_BIT = 1.0/80.0;
    final double GYRO_MAX_MEASURABLE_RATE_DEGPERSEC = 400.0;

    public SimGyroSensorModel(){
        gyroSim = (SimGyro) SimDeviceBanks.getSPIDevice(0);

    }

    public void resetToPose(Pose2d resetPose){
        // nothing, since gyro is rate-based?
    }

    public void update(Pose2d curRobotPose, Pose2d prevRobotPose){

        double curGyroAngle  = curRobotPose.getRotation().getDegrees();
        double prevGyroAngle = prevRobotPose.getRotation().getDegrees();
        double gyroRate = -1.0 * (curGyroAngle - prevGyroAngle)/Constants.SIM_SAMPLE_RATE_SEC; //Gyro reads backward from sim reference frames.
        
        gyroRate = Math.min(gyroRate,  GYRO_MAX_MEASURABLE_RATE_DEGPERSEC);
        gyroRate = Math.max(gyroRate, -GYRO_MAX_MEASURABLE_RATE_DEGPERSEC);

        //Round gyro rate to gyro internal scaling
        long gyroBits = Math.round(gyroRate / GYRO_RATE_SCALING_DEGPERSEC_PER_BIT);

        gyroRate = gyroBits * GYRO_RATE_SCALING_DEGPERSEC_PER_BIT;
        
        // Pass our model of what the sensor would be measuring back into the simGyro object
        // for the embedded code to interact with.
        gyroSim.simUpdate(Units.degreesToRadians(gyroRate));
    }
}