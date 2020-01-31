Road Statistic Reader
==========================

Requirements
------------
This service required java 1.8+ in order to run

Running
-------
Service dependencies can be installed by running the following command in the project root directory

    gradlew clean install
    
The application can be run by running the following command in the project root directory

    gradlew clean run -Pfile=<filename>

Where filename is the relative path of the file you intend to use for an input.

An example input file has been included and can be used as follows

    gradlew clean run -Pfile=exampleInput.txt