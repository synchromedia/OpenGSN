.. include:: <isonum.txt>
OpenGSN Version 2.0.0 
=====================

Description
--------------------

The OpenGSN Middleware is the middleware used for the GreenStar Network project. This middleware is a cloud computing management middleware that is
used to create green clouds. These green clouds are energy aware and also can manage and optimize this energy to lower GHG emissions. For additional 
information about the project visit : www.greenstarnetwork.com.

Licensing
-------------------
The software is released under Apache Software License version 2.0 see LICENSE file for the license.

Documentation
-------------------
Project Documentaion is available as the following guides 

  * Inocybe Platform``|trade|`` Installation Guide
  * OpenGSN Installation Guide
  * OpenGSN User Guide

In addition to these guides the Javadoc API is available at http://xxx

Prerequisites
-------------------
OpenJDK must be installed to build the project.

To install OpenJDK for Ubuntu::

    apt-get install openjdk-6-jdk

`Apache Maven <http://maven.apache.org/>`_ must be installed to build the
documentation.

To install Maven for Ubuntu::

    apt-get install maven3


Building
-----------
To build the OpenGSN java artifacts you simply need to issue the following command::

    mvn clean install

You will also need to build and install the Platform Plugin by issuing the following commands in the plugins folder::
 
    $INOCYBE_PLATFORM_LOCATION/bin/devkit package 

    $INOCYBE_PLATFORM_LOCATION/bin/devkit deploy 

Contributing
---------------
Our community welcomes all people interested in open source environmentally friendly cloud computing,
and there are no formal membership requirements. The best way to join the
community is to talk with others online using our Google Groups or Google Hangouts sessions.

We welcome all types of contributions, from blueprint designs to documentation to testing to deployment scripts.



