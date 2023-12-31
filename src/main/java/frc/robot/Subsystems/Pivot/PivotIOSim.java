// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Subsystems.Pivot;

import com.ctre.phoenixpro.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;

/** Add your docs here. */
public class PivotIOSim implements PivotIO {
    
    SingleJointedArmSim arm = new SingleJointedArmSim(DCMotor.getFalcon500(1), 27 * (24 / 18),
            SingleJointedArmSim.estimateMOI(Units.inchesToMeters(30), 3), Units.inchesToMeters(30), 0, 3.5, true);
    PIDController pid = new PIDController(1, 0, 0);

    public PivotIOInputsAutoLogged updateInputs() {

        PivotIOInputsAutoLogged input = new PivotIOInputsAutoLogged();

        // var motorSimState = motor.getSimState();

        input.velocityRPM = arm.getVelocityRadPerSec() * 60;
        input.currentDrawAmps = arm.getCurrentDrawAmps();
        input.temperatureCelsius = 0;
        input.motorOutputVolts = arm.getOutput(0);
        input.positionDegrees = Units.radiansToDegrees(arm.getAngleRads());

        arm.update(0.020);

        return input;

    }

    @Override
    public void setPosition(double degrees) {
        double outputVolts = pid.calculate(Units.radiansToDegrees(arm.getAngleRads()), degrees);
        arm.setInput(outputVolts);
    }

}
