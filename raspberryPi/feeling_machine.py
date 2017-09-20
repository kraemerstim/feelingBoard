#!/usr/bin/env python

import RPi.GPIO as GPIO
import rfidWrapper
import display
import signal
import time
import threading
from threading import Thread
import feeling_rest
from datetime import datetime, timedelta
import traceback
from pygame import mixer

class Feeling_Machine:
	BUTTON_REPEAT_DELAY = timedelta(seconds=5)
	SOUND_CARD = "33177163213230"
	RFID_HOLD_TIME = 1
	GREAT_JOB_SOUND = 24
	LED = 20
	
	def hot_button_pressed(self):
		self.makeGreatJobSound()
	
	def button_pressed(self, channel):
		if self.sound_mode:
			self.testSoundButton(channel)
		else:
			feeling = channel
			try:
				response = feeling_rest.addFeelingBoardEntry(self.RFID_uid, feeling)
				if (response and response.ok and response.headers['location']):
					feeling_rest.getEntryInfo(response.headers['location'])
				self.Button_Activate_Time = datetime.now() + Feeling_Machine.BUTTON_REPEAT_DELAY
				self.display.setDisplay('Danke ' + self.RFID_name + ' ' + str(feeling), 'fuers Mitmachen!')
			except Exception as e:
				self.display.setDisplay('Exception bei', 'addFeelingBoardEntry')
				self.Button_Activate_Time = datetime.now() + Feeling_Machine.BUTTON_REPEAT_DELAY
				traceback.print_exc()

	def makeGreatJobSound (self):
		GPIO.output(Feeling_Machine.GREAT_JOB_SOUND, GPIO.HIGH)
		time.sleep(0.1)
		GPIO.output(Feeling_Machine.GREAT_JOB_SOUND, GPIO.LOW)
		self.display.setDisplay("Test1", "Test2", 4)

	def testSoundButton (self, channel):
		mixer.music.load(self.sounds[channel-1])
		mixer.music.play()
		self.display.setDisplay('Now Playing', self.sounds[channel-1])

	def rfid_id_callback(self, uidString):
		nowTime = datetime.now()
		if (uidString != '0'):
			self.last_RFID_found_timestamp = datetime.now()
			self.setChipUID(uidString)
		else:
			durationTime = nowTime - self.last_RFID_found_timestamp
			if durationTime.seconds >= Feeling_Machine.RFID_HOLD_TIME:
				self.setChipUID(uidString)
				self.last_RFID_found_timestamp = datetime.min
			else:
				self.setChipUID(self.RFID_uid)
		
		
	def initialize(self):
		self.RFID_uid = "0"
		self.RFID_name = "Anton"
		self.Button_Activate_Time = datetime.min
		self.sound_mode = 0
		self.last_RFID_found_timestamp = datetime.min

		GPIO.setup(Feeling_Machine.GREAT_JOB_SOUND, GPIO.OUT)
		GPIO.setup(Feeling_Machine.LED, GPIO.OUT)	

		self.display = display.Display()

		mixer.init()
		self.sounds = []
		self.sounds.append('sounds/weird_russian.mp3')
		self.sounds.append('sounds/letitgo.mp3')
		self.sounds.append('sounds/timetogetlaid.mp3')
		self.sounds.append('sounds/justhadsex.mp3')
		self.sounds.append('sounds/incrediblethoughts.mp3')
		
		self.RFIDReader = rfidWrapper.RFID_Wrapper()
		self.RFIDReader.start(self.rfid_id_callback)

	def setChipUID(self, uid):
		if self.RFID_uid != uid:
			self.RFID_uid = uid
			self.rfidUidChanged()

	def rfidUidChanged(self):
		if (self.RFID_uid == '0'):
			GPIO.output(Feeling_Machine.LED, GPIO.LOW)
			self.display.setDisplay('Gefuehlsboard', '2.0')
		elif self.RFID_uid == Feeling_Machine.SOUND_CARD:
			self.sound_mode = 1
		else:
			GPIO.output(Feeling_Machine.LED, GPIO.HIGH)
			self.sound_mode = 0	
			try:
				self.RFID_name = feeling_rest.getUserNameByUid(self.RFID_uid)
			except:
				self.RFID_name = 'Error'
			if not self.RFID_name:
				self.RFID_name = 'Anton'
			self.display.setDisplay('Hallo ' + self.RFID_name, 'Wie geht\'s?')
			  
	def cleanup(self):
		self.display.setDisplay('Bye bye', ':(')
		self.RFIDReader.stop()
		self.display.cleanup()

