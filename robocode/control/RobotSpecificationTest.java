package robocode.control;

import robocode.control.RobotSpecification;


public class RobotSpecificationTest {

    public static RobotSpecification createRobotSpecification(Object fileSpecification, String name, String author, String webpage, String version, String robocodeVersion, String jarFile, String fullClassName, String description) {
	return RobotSpecification.createHiddenHelper().createSpecification(fileSpecification, name, author, webpage, version, robocodeVersion, jarFile, fullClassName, description);
    }

}
