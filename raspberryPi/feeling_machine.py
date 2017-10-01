#!/usr/bin/env python

import display
import signal
import time
import FB_Status
import userMode

class Feeling_Machine:
  
  def hot_button_pressed(self):
    if self.status.Mode == 'User':
      self.userMode.HotButtonPressed(channel)
    else:
      pass
  
  def button_pressed(self, channel):
    if self.status.Mode == 'User':
      self.userMode.ButtonPressed(channel)
    elif self.status.Mode == 'Sound':
      self.soundMode.ButtonPressed(channel)

  def rfid_id_callback(self, uidString, userName, userRole):
    if (uidString == '0'):
      self.display.setDisplay('Gefuehlsboard', '2.0')
    else:
      self.display.setDisplay('Hallo ' + userName, 'Wie geht\'s?')
    
  def initialize(self):
    self.sound_mode = 0
    
    self.display = display.Display()
    
    self.status = FB_Status.FB_Status()
    self.status.initialize()
    self.status.start(self.rfid_id_callback)
    
    self.userMode = userMode.User_Mode(self.status, self.display)
    self.soundMode = soundMode.Sound_Mode(self.status, self.display)
    
    GPIO.setup(Feeling_Machine.GREAT_JOB_SOUND, GPIO.OUT)
    GPIO.setup(Feeling_Machine.LED, GPIO.OUT) 

  def cleanup(self):
    self.status.cleanup()
    self.display.setDisplay('Bye bye', ':(')
    self.display.cleanup()
