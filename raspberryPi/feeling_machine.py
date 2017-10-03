#!/usr/bin/env python

import display
import signal
import time
import FB_Status
import userMode
import soundMode
import adminMode

class Feeling_Machine:
  
  def hot_button_pressed(self):
    if self.status.Mode == 'User':
      self.userMode.HotButtonPressed()
    elif self.status.Mode == 'Sound':
      self.soundMode.HotButtonPressed()
    elif self.status.Mode == 'Admin':
      self.adminMode.HotButtonPressed()
  
  def button_pressed(self, channel):
    if self.status.Mode == 'User':
      self.userMode.ButtonPressed(channel)
    elif self.status.Mode == 'Sound':
      self.soundMode.ButtonPressed(channel)
    elif self.status.Mode == 'Admin':
      self.adminMode.ButtonPressed(channel)

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
    self.adminMode = adminMode.Admin_Mode(self.status, self.display)
    
    self.display.setDisplay('Gefuehlsboard', '2.0')

  def cleanup(self):
    self.status.cleanup()
    self.display.setDisplay('Bye bye', ':(')
    self.display.cleanup()
