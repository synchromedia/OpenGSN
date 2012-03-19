
.. _services_cli:

####################
Services Client
####################

The services client allows users to work with the management services. To interact
with the appropriate service the service must be addressed by using it's alias:
  - cm = Cloud Manager
  - fm = Facility Manager
  - nm = Network Manager


Facility Manager Operations
===============================


Cloud Manager Operations
===============================
describeHost
-------------
*Parameters*

   - HostId : Id of the HostId

*Example*::

   $ ./run.sh cm describeInstance host123 


describeHosts
-------------

*Example*::

   $ ./run.sh cm describeHosts


createInstance
--------------

*Parameters*

   - instanceName : Name of the Instance
   - memory
   - cpu 
   - template

*Example*::

   $ ./run.sh cm destroyInstance vmname 512 1 templatename

createInstanceInHost
--------------------

*Parameters*

   - HostId : Id of the HostId
   - InstanceName: Name of the InstanceName

*Example*::

   $ ./run.sh cm destroyInstance host123 vmname

startInstance
stopInstance
rebootInstance
shutdownInstance
migrateInstance
setInstanceIPAddress
hostQuery
overrideHostPriorities
executeAction

destroyInstance
---------------
*Parameters*

   - HostId : Id of the HostId
   - InstanceName: Name of the InstanceName

*Example*::

   $ ./run.sh cm destroyInstance host123 vmname




Network Manager Operations
===============================

