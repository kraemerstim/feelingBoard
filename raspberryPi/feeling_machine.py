#!/usr/bin/env python

import RPi.GPIO as GPIO
import display
import signal
import time
from pygame import mixer
import FB_Status
import userMode

class Feeling_Machine:
  GREAT_JOB_SOUND = 24
  LED = 20
  
  def hot_button_pressed(self):
    if self.status.Mode == 'User':
      self.userMode.HotButtonPressed(channel)
    else:
      pass
  
  def button_pressed(self, channel):
    if self.status.Mode == 'User':
      self.userMode.ButtonPressed(channel)
    else:
      self.testSoundButton(channel)

  def testSoundButton (self, channel):
    mixer.music.load(self.sounds[channel])
    mixer.music.play()
    self.display.setDisplay('Now Playing', self.sounds[channel])

  def rfid_id_callback(self, uidString, userName, userRole):
    if (uidString == '0'):
      GPIO.output(Feeling_Machine.LED, GPIO.LOW)
      self.display.setDisplay('Gefuehlsboard', '2.0')
    else:
      GPIO.output(Feeling_Machine.LED, GPIO.HIGH)
      self.sound_mode = (userRole == 'Sound')  
      
      self.display.setDisplay('Hallo ' + userName, 'Wie geht\'s?')
    
  def initialize(self):
    self.sound_mode = 0
    
    self.display = display.Display()
    
    self.status = FB_Status.FB_Status()
    self.status.initialize()
    self.status.start(self.rfid_id_callback)
    
    self.userMode = userMode.User_Mode(self.status, self.display)
    
    GPIO.setup(Feeling_Machine.GREAT_JOB_SOUND, GPIO.OUT)
    GPIO.setup(Feeling_Machine.LED, GPIO.OUT) 

    mixer.init()
    self.sounds = []
    self.sounds.append('sounds/weird_russian.mp3')
    self.sounds.append('sounds/letitgo.mp3')
    self.sounds.append('sounds/timetogetlaid.mp3')
    self.sounds.append('sounds/justhadsex.mp3')
    self.sounds.append('sounds/incrediblethoughts.mp3')
    
    

  def cleanup(self):
    self.status.cleanup()
    self.display.setDisplay('Bye bye', ':(')
    self.display.cleanup()
