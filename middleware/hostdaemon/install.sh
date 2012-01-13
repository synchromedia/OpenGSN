#!/bin/sh
#
# Copyright 2009-2011 École de technologie supérieure,
# Communication Research Centre Canada,
# Inocybe Technologies Inc. and 6837247 CANADA Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# Install needed packages to support the host health daemon
# author @ knguyen
#

# install system liraries
sudo apt-get -y install autoconf
sudo apt-get -y install automake
sudo apt-get -y install libtool
sudo apt-get -y install uuid-dev
sudo apt-get -y install libcppunit-dev
sudo apt-get -y install build-essential
sudo apt-get -y install libssl-dev
sudo apt-get -y install libboost-python-dev
sudo apt-get -y install subversion
sudo apt-get -y install openjdk-6-jre

#create running directory
mkdir -p $HOME/hostdaemon
cd $HOME/hostdaemon

#install APR
svn co http://svn.apache.org/repos/asf/apr/apr/tags/1.3.12/ apr-1.3.12
cd apr-1.3.12
./buildconf
./configure
make
sudo make install
cd ..

# install ARP-Util
svn co http://svn.apache.org/repos/asf/apr/apr-util/tags/1.3.12/ apr-util-1.3.12
cd apr-util-1.3.12
./buildconf --with-apr=../apr-1.3.12
./configure --with-apr=/usr/local/apr/bin/apr-1-config
make
sudo make install
cd ..

# install activemq-cpp
svn co https://svn.apache.org/repos/asf/activemq/activemq-cpp/tags/activemq-cpp-2.2.6/ activemq-cpp-2.2.6
cd activemq-cpp-2.2.6
  #there is an error in autogen.sh of activemq-cpp-2.2.6, config/ needs to be created manually
mkdir -p config
./autogen.sh
./configure --with-apr=/usr/local/apr/bin/apr-1-config --with-apr-util=/usr/local/apr/bin/apu-1-config 
make
sudo make install
cd ..

# Build pyactivemq
svn co http://pyactivemq.googlecode.com/svn/tags/pyactivemq-0.1.0/
cd pyactivemq-0.1.0
sed -i 's/opt\/activemq-cpp-2.2.1\/include\/activemq-cpp-2.2.1/usr\/local\/include\/activemq-cpp-2.2.6/' setup.py
sed -i 's/opt\/activemq-cpp-2.2.1\/lib/usr\/local\/lib/' setup.py
python setup.py build
sudo python setup.py install
cd ..

# Download and run activemq
wget http://apache.mirror.rafal.ca:8080/activemq/apache-activemq/5.5.0/apache-activemq-5.5.0-bin.tar.gz
tar -xvf apache-activemq-5.5.0-bin.tar.gz
apache-activemq-5.5.0/bin/activemq start

#Download and run daemon
wget -N http://10.20.100.4/hostdaemon/daemon.py
wget -N http://10.20.100.4/hostdaemon/hostdaemon.py
wget -N http://10.20.100.4/hostdaemon/mountstorage.sh
chmod u+x mountstorage.sh
wget -N http://10.20.100.4/hostdaemon/hostdaemon.xml
sudo python hostdaemon.py start
