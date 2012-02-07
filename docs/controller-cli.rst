
.. _controller_cli:

####################
Controller Client
####################

The controller client is made to talk with the controller web service. As such it provides
a variety of commands useful to interact with this global optimizer.

The client is configured by changing the service location in the properties file::

    #Properties file for the GSN Services Client

    service.location=localhost
    service.port=8081

Controller Operations
---------------------

Connect
==========
Connects to the Controller

*Example*::

   $./run.sh connect 


Generate Plan
==============

Generates a new plan based on current data available and working conditions.

*Example*::

   $./run.sh generatePlan 

Execute Plan
============
Forces the execution of the Current Plan

*Example*::

   $./run.sh executePlan 


Generate Host List
==============
Generates a list of Hosts 

*Parameter*

   - Number of Hosts

*Example*::

   $./run.sh generateHostList 


Get host List
==============
Returns the current Host List

*Example*::

   $./run.sh getHostList


Get Link Table
==============
Returns the table describing the links

*Example*::

   $./run.sh getLinkTable


Get Log Content
==============
Returns the Log File Content

*Parameters*

   - Filename of the Log File

*Example*::

   $./run.sh getLogContent


Get/Set Mode
==============
Returns or Sets the Mode of the controller

*Example*::

   $./run.sh getMode



Get Resources From Cloud
==============
Returns resources from the cloud

*Example*::

   $./run.sh getResourcesFromCloud
