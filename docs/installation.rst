
.. _installation:

####################
Installation
####################
The installation of OpenGSN consists in installing all of its architectural components. 
While there are many components to install this installation is straightforward.

Prerequisites
===============================
A java JDK must be installed for your environment. 
On linux this can be done by issuing the following command::
   
    $ sudo apt-get install openjdk-7-jdk


Installing the Databases
======================================
The IaaS Framework uses CouchDB for long term persistence and REDIS for
short term state keeping and interprocess communication. As such both
databases must be installed and configured properly.

On linux, you can install CouchDB on Ubuntu via the following command::
   
    $ sudo apt-get install couchdb-server

Installing REDIS is also an easy task which can be done on Ubuntu like this::

    $ sudo apt-get install redis-server

Once both databases are installed and running you will be ready to move the message queue installation.

Installing the Message Queue
======================================
Installing the message queue is also an easy task. Any AMQP message queue will 
do however RabbitMQ is the message queue of choice for production deployments. It can be installed by typing::

    $ sudo apt-get install rabbitmq-server

Installing the Framework
======================================
IaaS Framework is a container that allows developers to quickly create virtualized resources from a variety of devices. The framework has many communications libraries and tools to manage the devices and their virtualization. The framework is installed by unzipping the downloaded archived and running it::

    $ unzip iaas-framework-2.0.0-SNAPSHOT.zip 
      cd iaas-framework
      bin/start

Installing the OpenGSN Plugin
======================================
Plugins are special archives that contains Javascript files describing the behavior the framework should adopt towards different devices. This makes it easy to develop new resource extensions and proper behaviors. To install the plugin download the archive and copy it to the plugins folder of the framework::
 
    $  wget http://www.github.com/synchromedia/OpenGSN/download/plugin.zip
       cp plugin.zip iaas-framework/plugins


Installing the Services
======================================
In addition to the plugins, the services must get deployed.Installing these services consists in downloading the war file and deploying them to an application container of your choice.

* Management Services (Mandatory) *
The management services are responsible for the safe keeping of the insfrastructure, they interact with the virtual resources to provide additional services to the UI. ::
  
   $ wget http://www.github.com/synchromedia/OpenGSN/download/services.war
     cp services.war /usr/share/jetty/webapps 
 

* Controller Service (Optional) *
The controller provides intelligence for the infrastructure and will do global optimizations
in order to maximize usage of renewable energy sources. ::
 

   $ wget http://www.github.com/synchromedia/OpenGSN/download/services.war
     cp services.war /usr/share/jetty/webapps 
 