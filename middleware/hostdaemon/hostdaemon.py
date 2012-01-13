#!/usr/bin/env python
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
# Host Daemon main class
# author @ knguyen
#

import sys, os, time, pyactivemq, subprocess, xml.dom.minidom
from daemon import Daemon
from pyactivemq import AcknowledgeMode
from threading import Event
from subprocess import call
from xml.dom.minidom import Node

# Publisher sends JMS message to a queue
class Publisher:
    def __init__(self, connectionFactory, topicName):
        connection = connectionFactory.createConnection()
        session = connection.createSession(AcknowledgeMode.AUTO_ACKNOWLEDGE)
        topic = session.createTopic(topicName)
        producer = session.createProducer(topic)
        self.connection = connection
        self.session = session
        self.producer = producer

    def publishMessages(self, msg):
        message = self.session.createTextMessage()
        message.text = msg
        self.producer.send(message)

    def finish(self):
        self.connection.close()

# Host Daemon checks host health and sends report to Host Resource
class HostDaemon(Daemon):

	def readConfig(self, cwd):
		self.CWD = cwd
		self.log = open(self.CWD + '/log.txt', 'a') 
		self.TIME_CHECK = 10
		self.TIME_RETRY = 10
		self.NRB_RETRY = 3
		self.STORAGE = '/storage'
		doc = xml.dom.minidom.parse(self.CWD + "/hostdaemon.xml")
		nodes = doc.getElementsByTagName("hostdaemon")
		if len(nodes) > 0:
		    	TIME_CHECK_node = nodes[0].getElementsByTagName("timecheck")
			if len(TIME_CHECK_node) > 0:
    				self.TIME_CHECK = int(TIME_CHECK_node[0].childNodes[0].data)
			TIME_RETRY_node = nodes[0].getElementsByTagName("timeretry")
			if len(TIME_RETRY_node) > 0:
		    		self.TIME_RETRY = int(TIME_RETRY_node[0].childNodes[0].data)
			NRB_RETRY_node = nodes[0].getElementsByTagName("nbrtries")
 			if len(NRB_RETRY_node) > 0:
		    		self.NRB_RETRY = int(NRB_RETRY_node[0].childNodes[0].data)
			STORAGE_node = nodes[0].getElementsByTagName("storagepath")
 			if len(STORAGE_node) > 0:
		    		self.STORAGE = STORAGE_node[0].childNodes[0].data

	def run(self):
		#create a JMS publisher
                url = 'tcp://localhost:61616'
                topicName = 'topic-1234'
                queue = pyactivemq.ActiveMQConnectionFactory(url)
                publisher = Publisher(queue, topicName)
		#create a log file
                self.log.write("HostDaemon starting ...\n")
		startindex = 1
		while True:
			time.sleep(self.TIME_CHECK)
                        count = 0;
			check = self.checkStorage()
			while (check < 1) and (count < self.NRB_RETRY):	
				self.mountStorage()
                       		self.log.write("HostDaemon mounting ..." + str(count + 1) + " time\n") 
                        	self.log.flush()
				time.sleep(self.TIME_RETRY)
				check = self.checkStorage()
				count = count + 1
                        if check > 0:
                        	publisher.publishMessages("OK")
                        	self.log.write("HostDaemon sending message ..." + str(startindex) + ": OK\n") 
                        else:
                        	publisher.publishMessages("ERR")
                        	self.log.write("HostDaemon sending message ..." + str(startindex) + ": ERR\n") 
			startindex = startindex + 1
                        self.log.flush()

	def mountStorage(self):
		call([self.CWD + '/mountstorage.sh'])

	def checkStorage(self):
		try:
                       	dirList = os.listdir(self.STORAGE)
			return len(dirList)
		except OSError, e: 
			self.log.write("Error: " + str(e.errno) + " - " + e.strerror + "\n")
                        self.log.flush()
			return e.errno * (-1)

if __name__ == "__main__":
	daemon = HostDaemon(os.getcwd() + '/hostdaemon.pid')
	if len(sys.argv) == 2:
		if 'start' == sys.argv[1]:
			daemon.readConfig(os.getcwd())
			daemon.start()
		elif 'stop' == sys.argv[1]:
			daemon.stop()
		elif 'restart' == sys.argv[1]:
			daemon.restart()
		else:
			print "Unknown command"
			sys.exit(2)
		sys.exit(0)
	else:
		print "usage: %s start|stop|restart" % sys.argv[0]
		sys.exit(2)

